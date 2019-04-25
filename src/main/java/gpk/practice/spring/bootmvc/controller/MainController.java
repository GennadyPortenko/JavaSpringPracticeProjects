package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    private final SecurityService securityService;

    @RequestMapping(value="/")
    public ModelAndView register(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelMap.put("username", securityService.getCurrentUserName());
        return modelAndView;
    }

    @PostMapping(value="/messenger/poll")
    public UserDto testAjax(@RequestBody UserDto request) {
        System.out.println("userName : " + request.getUsername());
        System.out.println("email: " + request.getEmail());
        return request;
    }

}
