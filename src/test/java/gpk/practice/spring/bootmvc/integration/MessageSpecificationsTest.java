package gpk.practice.spring.bootmvc.integration;

import gpk.practice.spring.bootmvc.repository.MessageRepository;
import gpk.practice.spring.bootmvc.service.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
public class MessageSpecificationsTest {

    @Autowired
    private MessageService messageService;

    @Before
    public void init() {

    }
    @Test
    public void findMessagesWithIdLessThan() {
    }
}
