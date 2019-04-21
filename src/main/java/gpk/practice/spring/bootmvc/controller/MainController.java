package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @Autowired
    SecurityService securityService;

    @RequestMapping(value="/")
    public ModelAndView register(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelMap.put("username", securityService.getCurrentUserName());
        return modelAndView;
    }

}
