package gpk.practice.spring.bootmvc.unit;

import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.service.DtoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DtoServiceTest {
    @Autowired
    private DtoService dtoService;

    @Before
    public void init() {
    }

    @Test
    public void testUserToUserDtoMapping() {
        User user = new User();
        user.setUsername("John");
        user.setEmail("john@john.com");
        user.setEmail("password");
        UserDto userDto = dtoService.convertToDto(user);
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertNull(user.getPassword());
    }

    @Test
    public void testUserDtoToUserMapping() {
        UserDto userDto = new UserDto();
        userDto.setUsername("John");
        userDto.setEmail("john@john.com");
        userDto.setPassword("password");
        User user = dtoService.convertToUser(userDto);
        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getPassword(), user.getPassword());
    }

    @Test
    public void testMessageDtoToMessageMapping() {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1);
        messageDto.setText("Test message text");
        messageDto.setDatetime(Instant.now());
        Message message = dtoService.convertToMessage(messageDto);
        assertEquals(messageDto.getId(), message.getId());
        assertEquals(messageDto.getText(), message.getText());
        assertEquals(messageDto.getDatetime(), message.getDatetime());
    }

    @Test
    public void testMessageToMessageDtoMapping() {
        Message message = new Message();
        message.setId(1);
        message.setText("Test message text");
        message.setDatetime(Instant.now());
        MessageDto messageDto = dtoService.convertToDto(message);
        assertEquals(messageDto.getId(), message.getId());
        assertEquals(messageDto.getText(), message.getText());
        assertEquals(messageDto.getDatetime(), message.getDatetime());
    }

}
