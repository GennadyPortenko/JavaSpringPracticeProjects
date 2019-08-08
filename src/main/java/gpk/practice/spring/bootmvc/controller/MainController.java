package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.*;
import gpk.practice.spring.bootmvc.model.LongPollSubscriber;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.repository.MessageRepository;
import gpk.practice.spring.bootmvc.service.DtoService;
import gpk.practice.spring.bootmvc.service.MessageService;
import gpk.practice.spring.bootmvc.service.SecurityService;
import gpk.practice.spring.bootmvc.service.SubscribersManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    @Qualifier("messageDateTimeFormatter")
    private final DateTimeFormatter messageDateTimeFormatter;
    @Value("${longpoll.timeout}")
    private Integer LONG_POLL_TIMEOUT;
    private final SecurityService securityService;
    private final DtoService dtoService;
    private final SubscribersManager subscribersManager;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private AtomicLong lastMessageId = new AtomicLong(-1);
    private AtomicLong lastDeletedMessageId = new AtomicLong(-1);

    @RequestMapping(value="/")
    public ModelAndView messenger(HttpServletRequest request, ModelMap modelMap) {
        HttpSession session = request.getSession();
        if (session.getAttribute("username") == null) {
            request.getSession().setAttribute("username", securityService.getCurrentUserName());
        }
        ModelAndView modelAndView = new ModelAndView("index");
        List<MessageDto> messageDtos = messageService.findTop20Messages().stream()
                                         .map(dtoService::convertToDto).collect(Collectors.toList());
        Collections.reverse(messageDtos);
        modelMap.put("messages", messageDtos);
        Message lastDeleted = messageRepository.findTopDeleted();
        modelMap.put("lastDeletedMessageId", lastDeleted == null ? null : lastDeleted.getMessageId());
        return modelAndView;
    }

    @PostMapping(value="/messenger/poll")
    @CrossOrigin(origins="${host.url}", allowedHeaders = "*")
    public DeferredResult<ResponseEntity<?>> processLongPoll(@RequestBody LongPollRequest request) {
        DeferredResult<ResponseEntity<?>> dr = new DeferredResult<>(LONG_POLL_TIMEOUT.longValue());
        Long clientLastMessageId = request.getLastMessageId();
        Long clientLastDeletedMessageId = request.getLastDeletedMessageId();
        /* сразу же вернуть результат с более новыми сообщениями, если они есть */
        long lastMessageId = this.lastMessageId.get();
        if ( (lastMessageId != -1) && (clientLastMessageId != null)) {
            if (lastMessageId > clientLastMessageId) {
                List<MessageDto> messages = messageService.findAllAfterId(clientLastMessageId).stream()
                        .map(dtoService::convertToDto)
                        .collect(Collectors.toList());
                dr.setResult(new ResponseEntity<>(new LongPollResponse(LongPollResponseType.NEW_MESSAGES, messages), HttpStatus.OK));
                return dr;
            }
        }
        /* сразу же вернуть результат с более новыми удаленными сообщениями, если они есть */
        long lastDeletedMessageId = this.lastDeletedMessageId.get();
        if ((clientLastDeletedMessageId != null) && (lastDeletedMessageId != -1)) {
            if (lastDeletedMessageId != clientLastDeletedMessageId) {
                Instant clientLastDeletedMessageTime = messageService.findById(clientLastDeletedMessageId).getDatetime();
                if (messageService.findById(lastDeletedMessageId).getDatetime().compareTo(clientLastDeletedMessageTime) > 0) {
                    List<MessageDto> newDeletedMessages = messageRepository.findByDeletedGreaterThanEqual(clientLastDeletedMessageTime).stream().map(dtoService::convertToDto).collect(Collectors.toList());
                    dr.setResult(new ResponseEntity<>(new LongPollResponse(LongPollResponseType.NEW_DELETED_MESSAGES, newDeletedMessages), HttpStatus.OK));
                    return dr;
                }
            }
        }
        LongPollSubscriber subscriber = new LongPollSubscriber(dr);
        dr.onTimeout(() -> {
            /* послать по таймауту ответ за ajax-запрос с HTTP статусом NO_CONTENT*/
            subscribersManager.abortSubscriber(subscriber);
        });
        subscribersManager.addSubscriber(subscriber);

        return dr;
    }

    @PostMapping(value="/messenger/new_message")
    public ResponseEntity<?> newMessage(@RequestBody NewMessageDto newMessageDto) {
        Message savedMessage = null;

        newMessageDto.setUsername(securityService.getCurrentUserName());
        Message newMessage = dtoService.convertToMessage(newMessageDto);
        if (newMessage != null) {
            savedMessage = messageService.saveMessage(newMessage);
        }

        if (savedMessage == null) {
            return new ResponseEntity<>(new Message(), HttpStatus.NO_CONTENT);
        }
        /* разослать новое(ые) сообщение(я) всем long-poll подписчикам */
        lastMessageId.set(savedMessage.getMessageId());
        subscribersManager.broadcast(Arrays.asList(dtoService.convertToDto(savedMessage)), LongPollResponseType.NEW_MESSAGES);
        return new ResponseEntity<>(new Message() /* (empty) */, HttpStatus.OK);
    }

    @PostMapping(value="/messenger/load_more")
    public ResponseEntity<?> loadMoreMessages(@RequestBody IdDto idDto) {
        List<MessageDto> messages = messageService.findTop20MessagesWIthIdLessThan(idDto.getId())
                                      .stream().map(dtoService::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PostMapping(value="/messenger/message/delete/{messageId}")
    @ResponseBody
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Message messageToDelete = messageService.findById(messageId);
        if ( (!messageToDelete.getUser().getName().equals(session.getAttribute("username"))) ||
             (messageToDelete.getDeleted() != null ) ) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        if (!messageService.setDeleted(messageToDelete))  {
            return new ResponseEntity<>(Arrays.asList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        lastDeletedMessageId.set(messageToDelete.getMessageId());
        subscribersManager.broadcast(Arrays.asList(dtoService.convertToDto(messageToDelete)), LongPollResponseType.NEW_DELETED_MESSAGES);
        return new ResponseEntity<>(Arrays.asList(), HttpStatus.OK);
    }

}
