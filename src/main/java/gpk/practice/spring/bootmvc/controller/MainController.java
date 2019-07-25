package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.IdDto;
import gpk.practice.spring.bootmvc.dto.LongPollRequest;
import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.dto.NewMessageDto;
import gpk.practice.spring.bootmvc.model.LongPollSubscriber;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.service.DtoService;
import gpk.practice.spring.bootmvc.service.MessageService;
import gpk.practice.spring.bootmvc.service.SecurityService;
import gpk.practice.spring.bootmvc.service.SubscribersManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    @Value("${longpoll.timeout}")
    private Integer LONG_POLL_TIMEOUT;
    private final SecurityService securityService;
    private final DtoService dtoService;
    private final SubscribersManager subscribersManager;
    private final MessageService messageService;
    private AtomicLong lastMessageId = new AtomicLong(-1);

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
        return modelAndView;
    }

    @PostMapping(value="/messenger/poll")
    public DeferredResult<ResponseEntity<?>> processLongPoll(@RequestBody LongPollRequest request) {
        DeferredResult<ResponseEntity<?>> dr = new DeferredResult<>(LONG_POLL_TIMEOUT.longValue());
        long clientLastMessageId = request.getLastMessageId();
        //FIXME
        if (this.lastMessageId.get() > clientLastMessageId) {
            // сразу же вернуть результат с более новыми сообщениями, если они есть

            List<MessageDto> msgs = messageService.findAllAfterId(clientLastMessageId).stream()
                                                .map(dtoService::convertToDto)
                                                .collect(Collectors.toList());
            dr.setResult(new ResponseEntity<>(msgs, HttpStatus.OK));
        } else {
            LongPollSubscriber subscriber = new LongPollSubscriber(dr, clientLastMessageId);
            dr.onTimeout(() -> {
                /* послать по таймауту ответ за ajax-запрос с HTTP статусом NO_CONTENT*/
                subscribersManager.abortSubscriber(subscriber);
            });
            subscribersManager.addSubscriber(subscriber);
        }
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
            return new ResponseEntity<>(new Message(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        /* разослать новое(ые) сообщение(я) всем long-poll подписчикам */
        lastMessageId.set(savedMessage.getMessageId());
        subscribersManager.broadcast(Arrays.asList(dtoService.convertToDto(savedMessage)));
        return new ResponseEntity<>(new Message() /* (empty) */, HttpStatus.OK);
    }

    @PostMapping(value="/messenger/load_more")
    public ResponseEntity<?> loadMoreMessages(@RequestBody IdDto idDto) {
        System.out.println("/messenger/load_more : first message if = " + idDto.getId());
        List<MessageDto> messages = messageService.findTop20MessagesWIthIdLessThan(idDto.getId())
                                      .stream().map(dtoService::convertToDto).collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

}
