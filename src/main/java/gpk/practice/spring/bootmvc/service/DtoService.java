package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.dto.IdDto;
import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.dto.NewMessageDto;
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
    private final MessageService messageService;

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

    public Message convertToMessage(NewMessageDto messageDto) {
        Message message = modelMapper.map(messageDto, Message.class);
        User user = userService.findByName(messageDto.getUsername());
        if (user == null) {
            return null;
        }
        message.setUser(user);
        if (messageDto.getMessagesToReply().isEmpty()) {
            return message;
        }
        List<Message> messagesToReply = new ArrayList<>();
        for (IdDto messageToReply: messageDto.getMessagesToReply()) {
            messagesToReply.add(messageService.findById(messageToReply.getId()));
        }
        message.setMessagesToReply(messagesToReply);
        return message;
    }
}
