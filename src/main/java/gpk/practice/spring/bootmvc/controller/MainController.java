package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class MainController {
    private final SecurityService securityService;

    @RequestMapping(value="/")
    public ModelAndView register(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelMap.put("username", securityService.getCurrentUserName());
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    UserDto add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserDto userDto = new UserDto();
        userDto.setUsername( request.getParameter("firstName") );
        userDto.setEmail( request.getParameter("email") );
        return userDto;
    }

}
