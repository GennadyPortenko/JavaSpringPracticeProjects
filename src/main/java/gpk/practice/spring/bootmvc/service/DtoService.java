package gpk.practice.spring.bootmvc.service;

import gpk.practice.spring.bootmvc.dto.UserDto;
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
        return userDto;
    }

    public User convertToUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }
}
