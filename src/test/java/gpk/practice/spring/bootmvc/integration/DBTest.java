package gpk.practice.spring.bootmvc.integration;

import gpk.practice.spring.bootmvc.model.Message;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.RoleRepository;
import gpk.practice.spring.bootmvc.service.MessageService;
import gpk.practice.spring.bootmvc.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DBTest {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MessageService messageService;

    private User user;
    private final String TEST_USER_PASSWORD = "password";

    private boolean dbInitialized = false;
    @Before
    public void init() {
        /*
        if (!dbInitialized) {
            // messageService.deleteAll();
            // userService.deleteAll();
            List<User> users = Arrays.asList(
                    new User("example@mail.com", "Bill", TEST_USER_PASSWORD),
                    new User("example@mail.com", "John", TEST_USER_PASSWORD),
                    new User("example@mail.com", "George", TEST_USER_PASSWORD)
            );
            users.forEach(user -> userService.registerNewUserAccount(user));

            List<Message> messages = Arrays.asList(new Message(Instant.now(), "message text", userService.findByName("George")),
                                                   new Message(Instant.now(), "message text", userService.findByName("George"))
            );
            for (Message message : messages) {
                messageService.saveMessage(message);
            }

            dbInitialized = true;
        }
        */
    }

    @Test
    public void test () {}

}
