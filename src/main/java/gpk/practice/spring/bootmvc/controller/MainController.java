package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.LongPollRequest;
import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.model.LongPollSubscriber;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.Arrays;
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
    private final UserService userService;

    @RequestMapping(value="/")
    public ModelAndView messenger(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("index");
        String currentUser = securityService.getCurrentUserName();
        modelMap.put("username", currentUser);
        modelMap.put("messages", messageService.findAll().stream()
                .map(message -> dtoService.convertToDto(message)).collect(Collectors.toList()));
        return modelAndView;
    }

    @PostMapping(value="/messenger/poll")
    public DeferredResult<ResponseEntity<?>> testAjax(@RequestBody LongPollRequest request) {
        DeferredResult<ResponseEntity<?>> dr = new DeferredResult<>(LONG_POLL_TIMEOUT.longValue());
        LongPollSubscriber subscriber = new LongPollSubscriber(dr, request.getLastMessageId());
        dr.onTimeout(() -> {
            subscribersManager.abortSubscriber(subscriber);
        });
        subscribersManager.addSubscriber(subscriber);
        return dr;
    }

    @PostMapping(value="/messenger/new_message")
    public ResponseEntity<?> newMessage(@RequestBody MessageDto messageDto) {
        System.out.println("message : " + messageDto.getText());
        messageDto.setUsername(securityService.getCurrentUserName());
        Message message = dtoService.convertToMessage(messageDto);
        message.setDatetime(Instant.now());
        messageService.saveMessage(message);
        subscribersManager.broadcast(Arrays.asList(dtoService.convertToDto(message)));
        return new ResponseEntity<>(new Message(), HttpStatus.OK);
    }

}
