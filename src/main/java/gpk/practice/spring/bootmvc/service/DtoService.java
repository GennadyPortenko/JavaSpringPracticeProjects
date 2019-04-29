package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DtoService {
    private final ModelMapper modelMapper;

    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    public User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public MessageDto convertToDto(Message message) {
        return modelMapper.map(message, MessageDto.class);
    }

    public Message convertToMessage(MessageDto messageDto) {
        return modelMapper.map(messageDto, Message.class);
    }
}
