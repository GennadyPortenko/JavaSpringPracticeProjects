package gpk.practice.spring.bootmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping(value="/")
    public String register() {
        return "index";
    }

}
