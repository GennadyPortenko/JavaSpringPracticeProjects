package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.service.DtoService;
import gpk.practice.spring.bootmvc.service.SecurityService;
import gpk.practice.spring.bootmvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.*;
import java.util.Set;

@Controller
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class LoginController {
    private final UserService userService;
    private final SecurityService securityService;
    private final DtoService dtoService;

    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @GetMapping(value="/registration")
    public ModelAndView registration(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("registration");
        modelMap.put("username", securityService.getCurrentUserName());
        UserDto userDto = new UserDto();
        modelAndView.addObject("user", userDto);
        return modelAndView;
    }

    @PostMapping(value="/register")
    public ModelAndView register(@ModelAttribute @Valid UserDto userDto, BindingResult bindingResult, ModelMap modelMap) {
        User user = dtoService.convertToUser(userDto);
        ModelAndView modelAndView = new ModelAndView("register");
        modelMap.put("username", securityService.getCurrentUserName());

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        // if (bindingResult.hasErrors()) {
        if ( !violations.isEmpty() ) {
            modelMap.put("registered_f", false);
            StringBuilder errorMsg = new StringBuilder("Ошибка регистрации. Пожалуйста, проверьте правильность заполнения полей. ");
            for (ConstraintViolation<UserDto> violation : violations) {
                errorMsg.append(violation.getMessage()).append(" ");
            }

            modelMap.put("error_msg", errorMsg.toString());
            return modelAndView;
        }
        if ( userService.findByName(user.getName()) != null ) {
            modelMap.put("registered_f", false);
            modelMap.put("error_msg", "Пользователь с таким логином уже существует");
            return modelAndView;
        }
        if (userService.findByEmail(user.getEmail()) != null ) {
            modelMap.put("registered_f", false);
            modelMap.put("error_msg", "Пользователь с таким email уже существует");
            return modelAndView;
        }
        try {
            userService.registerNewUserAccount(user);
            modelMap.put("registered_f", true);
            modelMap.put("username", user.getName());
        } catch (Exception /* NonUniqueResultException */ e) {
            modelMap.put("registered_f", false);
            modelMap.put("error_msg", "Ошибка регистрации.");
        }
        return modelAndView;
    }

    @GetMapping(value="/access-denied")
    public String accesDenied(ModelMap modelMap) {
        modelMap.put("username", securityService.getCurrentUserName());
        return "access-denied";
    }

    @GetMapping(value="/login")
    public ModelAndView logIn(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelMap.put("username", securityService.getCurrentUserName());
        UserDto userDto = new UserDto();
        modelAndView.addObject("user", userDto);
        return modelAndView;
    }

    @GetMapping(value="/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }
}
