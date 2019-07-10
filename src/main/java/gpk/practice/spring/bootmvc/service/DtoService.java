package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DtoService {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setPassword(null);
        return userDto;
    }

    public User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public MessageDto convertToDto(Message message) {
        MessageDto messageDto = modelMapper.map(message, MessageDto.class);
        messageDto.setUsername(message.getUser().getName());
        if (message.getMessagesToReply() != null) {
            messageDto.setMessagesToReply(message.getMessagesToReply().stream().
                    map(this::convertToDto).collect(Collectors.toList()));
        }
        return messageDto;
    }

    public Message convertToMessage(MessageDto messageDto) {
        Message message = modelMapper.map(messageDto, Message.class);
        message.setUser(userService.findByName(messageDto.getUsername()));
        if (messageDto.getMessagesToReply() == null) {
            return message;
        }
        List<Message> messagesToReply = new ArrayList<>();
        for (MessageDto messageToReply : messageDto.getMessagesToReply()) {
            messagesToReply.add(convertToMessage(messageToReply));
        }
        message.setMessagesToReply(messagesToReply);
        return message;
    }
}
