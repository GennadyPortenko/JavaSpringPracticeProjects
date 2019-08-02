package gpk.practice.spring.bootmvc.unit;

import gpk.practice.spring.bootmvc.dto.IdDto;
import gpk.practice.spring.bootmvc.dto.MessageDto;
import gpk.practice.spring.bootmvc.dto.NewMessageDto;
import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.service.DtoService;
import gpk.practice.spring.bootmvc.service.MessageService;
import gpk.practice.spring.bootmvc.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DtoServiceTest {
    private DtoService dtoService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private MessageService messageService;

    @Autowired
    @Qualifier("messageDateTimeFormatter")
    private DateTimeFormatter messageDateTimeFormatter;

    private final String testUsername = "John";
    private final User testUser = new User("", testUsername, "");

    private final long testMessageId = 10;
    private final String testMessageText = "test message text";
    private final Message testMessage = new Message(Instant.now(), testMessageText, testUser);

    private boolean initialized = false;
    @Before
    public void init() {
            Mockito.when(userService.findByName(testUsername))
                    .thenReturn(testUser);
            Mockito.when(messageService.findById(testMessageId))
                    .thenReturn(testMessage);

            dtoService = new DtoService(modelMapper, userService, messageService, messageDateTimeFormatter);
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
    public void testNewMessageDtoToMessageMapping() {
        NewMessageDto messageDto = new NewMessageDto("test message text", testUsername, Arrays.asList(new IdDto(testMessageId), new IdDto(testMessageId)));

        Message message = dtoService.convertToMessage(messageDto);
        assertEquals(messageDto.getText(), message.getText());
        assertEquals(messageDto.getUsername(), message.getUser().getName());
        assertNotEquals(message.getMessagesToReply().size(), 0);
        for (Message messageToReply : message.getMessagesToReply()) {
            assertEquals(messageToReply.getText(), testMessage.getText());
        }

    }

    @Test
    public void testMessageToMessageDtoMapping() {
        Message message = new Message();
        message.setMessageId(1L);
        message.setDeleted(true);
        message.setText("Test message text");
        message.setDatetime(Instant.now());
        message.setUser(testUser);
        message.setMessagesToReply(Arrays.asList(new Message(Instant.now(), "some text", testUser)));
        MessageDto messageDto = dtoService.convertToDto(message);
        assertEquals(messageDto.getId(), message.getMessageId());
        assertEquals(messageDto.getText(), message.getText());
        assertEquals(messageDto.getDeleted(), message.getDeleted());
        assertEquals(messageDto.getDatetime(), messageDateTimeFormatter.format(message.getDatetime()));
        assertEquals(messageDto.getUsername(), message.getUser().getName());
        for( int i = 0; i < message.getMessagesToReply().size(); i++ ) {
            assertEquals( messageDto.getMessagesToReply().get(i).getUsername(),
                          message.getMessagesToReply().get(i).getUser().getName() );
        }
    }

}
