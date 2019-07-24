package gpk.practice.spring.bootmvc.integration;

import gpk.practice.spring.bootmvc.App;
import gpk.practice.spring.bootmvc.configuration.AppConfig;
import gpk.practice.spring.bootmvc.configuration.DBTestProfileConfig;
import gpk.practice.spring.bootmvc.configuration.SecurityConfig;
import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.MessageRepository;
import gpk.practice.spring.bootmvc.repository.RoleRepository;
import gpk.practice.spring.bootmvc.service.MessageService;
import gpk.practice.spring.bootmvc.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        App.class,
        AppConfig.class,
        SecurityConfig.class,
        DBTestProfileConfig.class})
@ActiveProfiles("test")
public class DBTest {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MessageService messageService;
    @Autowired
    MessageRepository messageRepository;

    final int MESSAGES_NUM = 50;
    final int TOP_MESSAGES_NUM = 20;
    final String TEST_USER_PASSWORD = "password";
    final String MESSAGE_TEXT = "message";
    final User John = new User("example@mail.com", "John", TEST_USER_PASSWORD);
    final User Bill = new User("example@mail.com", "Bill", TEST_USER_PASSWORD);

    private void cleanDB() {
        messageService.deleteAll();
        userService.deleteAll();
    }

    private static boolean dbInitialized = false;
    @Before
    public void init() {

        if (!dbInitialized) {
            cleanDB();

            List<User> users = Arrays.asList( John, Bill );
            users.forEach(user -> userService.registerNewUserAccount(user));

            List<Message> messages = new ArrayList<>();
            for (int i = 1; i <= MESSAGES_NUM; i++) {
                messages.add(new Message(Instant.now(), MESSAGE_TEXT + i, userService.findByName(John.getName())));
            }
            messages.forEach(messageService::saveMessage);

            dbInitialized = true;
        }
    }

    @Test
    @Transactional
    public void testFindTop20Messages () {
        List<Message> topMessages = messageService.findTop20Messages();

        int messageIndex = MESSAGES_NUM;
        for (Message topMessage : topMessages) {
            assertEquals(MESSAGE_TEXT + messageIndex--, topMessage.getText());
        }
    }

    @Test
    @Transactional
    public void testFindTop20ByMessageIdLessThanOrderByMessageId() {
        final int specificId = 36;
        List<Message> allMessages = messageService.findAll();
        List<Message> messages = messageService.findTop20MessagesWIthIdLessThan(allMessages.get(specificId - 1).getMessageId());

        int messageIndex = specificId - 2; // index of message with id = (specifiedId - 1)
        for (Message message : messages) {
            assertEquals(message, allMessages.get(messageIndex--));
        }
    }

}
