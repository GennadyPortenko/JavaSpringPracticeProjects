package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.LongPollRequest;
import gpk.practice.spring.bootmvc.model.LongPollSubscriber;
import gpk.practice.spring.bootmvc.service.SubscribersManager;
import gpk.practice.spring.bootmvc.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    private final SecurityService securityService;
    private final SubscribersManager subscribersManager;
    @Value("${longpoll.timeout}")
    private Integer LONG_POLL_TIMEOUT;

    @RequestMapping(value="/")
    public ModelAndView register(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelMap.put("username", securityService.getCurrentUserName());
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

    @Scheduled(fixedRate=2000)
    public void broadcast() {
        subscribersManager.broadcast();
    }

}
