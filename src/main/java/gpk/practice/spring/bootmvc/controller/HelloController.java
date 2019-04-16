package gpk.practice.spring.bootmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @RequestMapping(value="/hello")
    @ResponseBody
    public String foo() {
        return "Hello!";
    }

    @RequestMapping(value="/hello_params")
    public String foo(@RequestParam(required=false) String firstName,
                      @RequestParam(required=false) String lastName, ModelMap model) {
        model.put("firstName", firstName);
        model.put("lastName", lastName);
        return "hello";
    }

}
