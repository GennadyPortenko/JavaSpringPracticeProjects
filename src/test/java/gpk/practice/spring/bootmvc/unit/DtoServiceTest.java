package gpk.practice.spring.bootmvc.unit;

import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.service.DtoService;
import gpk.practice.spring.bootmvc.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DtoServiceTest {
    private DtoService dtoService = null;

    @Autowired
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    private final String testUsername = "John";
    private User testUser = null;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        testUser = new User();
        testUser.setName(testUsername);
        Mockito.when(userService.findByName(testUsername))
                .thenReturn(testUser);

        dtoService = new DtoService(modelMapper, userService); // for userDto mock usage by DtoService
    }

    @Test
    public void testUserToUserDtoMapping() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@john.com");
        user.setPassword("password");
        UserDto userDto = dtoService.convertToDto(user);
        assertEquals(userDto.getName(), userDto.getName());
        assertEquals(userDto.getEmail(), userDto.getEmail());
        assertNull(userDto.getPassword());
    }

    @Test
    public void testUserDtoToUserMapping() {
        UserDto userDto = new UserDto();
        userDto.setName("John");
        userDto.setEmail("john@john.com");
        userDto.setPassword("password");
        User user = dtoService.convertToUser(userDto);
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getName(), user.getName());
    }

    @Test
    public void testMessageDtoToMessageMapping() {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(1L);
        messageDto.setText("Test message text");
        messageDto.setDatetime(Instant.now());
        messageDto.setUsername(testUsername);
        List<MessageDto> messagesDtoToReply = new ArrayList<>(
                Arrays.asList(new MessageDto(1, Instant.now(), "message 1", testUsername),
                              new MessageDto(1, Instant.now(), "message 1", testUsername)
                ));
        messageDto.setMessagesToReply(messagesDtoToReply);

        Message message = dtoService.convertToMessage(messageDto);
        assertEquals(messageDto.getId(), message.getMessageId());
        assertEquals(messageDto.getText(), message.getText());
        assertEquals(messageDto.getDatetime(), message.getDatetime());
        assertEquals(messageDto.getUsername(), message.getUser().getName());
        List<Message> messagesToReply = new ArrayList<>();
        for(MessageDto messageDtoToReply : messageDto.getMessagesToReply()) {
            messagesToReply.add(dtoService.convertToMessage(messageDtoToReply));
        }
        assertEquals(messagesToReply, message.getMessagesToReply());
    }

    @Test
    public void testMessageToMessageDtoMapping() {
        Message message = new Message();
        message.setMessageId(1L);
        message.setText("Test message text");
        message.setDatetime(Instant.now());
        message.setUser(testUser);
        message.setMessagesToReply(Arrays.asList(new Message(0, Instant.now(), "some text",
                                                             testUser, null)));
        MessageDto messageDto = dtoService.convertToDto(message);
        assertEquals(messageDto.getId(), message.getMessageId());
        assertEquals(messageDto.getText(), message.getText());
        assertEquals(messageDto.getDatetime(), message.getDatetime());
        assertEquals(messageDto.getUsername(), message.getUser().getName());
        for( int i = 0; i < message.getMessagesToReply().size(); i++ ) {
            assertEquals( messageDto.getMessagesToReply().get(i).getUsername(),
                          message.getMessagesToReply().get(i).getUser().getName() );
        }
    }

}
