package gpk.practice.spring.bootmvc.controller;

import gpk.practice.spring.bootmvc.dto.info.UserInfoDto;
import gpk.practice.spring.bootmvc.dto.info.UsersInfoDto;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.service.MessageService;
import gpk.practice.spring.bootmvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InfoController {
    final UserService userService;
    final MessageService messageService;

    @GetMapping(value="/info/users")
    @ResponseBody
    public ResponseEntity<UsersInfoDto> usersInfo() {
        return new ResponseEntity<>(new UsersInfoDto(userService.getUserNames(), messageService.getNumberOfMessages()), HttpStatus.OK);
    }

    @GetMapping(value="/info/user/{userName}")
    @ResponseBody
    public ResponseEntity<UserInfoDto> userInfo(@PathVariable String userName) {
        User user = userService.findByName(userName);
        if (user == null) {
            return new ResponseEntity<>(new UserInfoDto( "", 0L), HttpStatus.OK );
        }
        return new ResponseEntity<>(new UserInfoDto( userName, messageService.getNumberOfMessagesOfUser(userName) ), HttpStatus.OK);
    }

}
