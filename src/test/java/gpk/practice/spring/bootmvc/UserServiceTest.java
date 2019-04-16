package gpk.practice.spring.bootmvc;

import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.UserRepository;
import gpk.practice.spring.bootmvc.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Mock
	UserRepository mockUserRepository;

	private UserService userService;
	private User user;

	@Before
	public void init() {
        userService = new UserService(mockUserRepository);

		user = new User();
		user.setEmail("john@mail.com");
		user.setLogin("John");

		when(mockUserRepository.findByEmail(anyString())).thenReturn(user);
	}

	@Test
	public void testFindByEmail() {
		final String email = "john@mail.com";
		assertEquals(userService.findByEmail(email).getEmail(), user.getEmail());
	}

}
