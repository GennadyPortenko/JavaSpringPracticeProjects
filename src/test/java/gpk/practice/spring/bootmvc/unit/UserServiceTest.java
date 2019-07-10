package gpk.practice.spring.bootmvc.unit;

import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.repository.RoleRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	UserRepository mockUserRepository;
	@Mock
	RoleRepository mockRoleRepository;
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	private UserService userService;
	private User user;

	@Before
	public void init() {
        userService = new UserService(mockUserRepository, mockRoleRepository, bCryptPasswordEncoder);

		user = new User();
		user.setEmail("john@mail.com");
		user.setName("John");

		when(mockUserRepository.findByEmail(anyString())).thenReturn(user);
		when(mockUserRepository.findByName(anyString())).thenReturn(user);
	}

	@Test
	public void givenUserService_whenFindByEmail_thenReturnsUser() {
		final String email = "john@mail.com";
		assertEquals(userService.findByEmail(email).getEmail(), user.getEmail());
	}

	@Test
	public void givenUserService_whenFindByName_thenReturnsUser() {
		final String username = "John";
		assertEquals(userService.findByName(username).getName(), user.getName());
	}

}
