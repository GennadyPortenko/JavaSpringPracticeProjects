package gpk.practice.spring.bootmvc.unit;

import gpk.practice.spring.bootmvc.dto.UserDto;
import gpk.practice.spring.bootmvc.model.User;
import gpk.practice.spring.bootmvc.service.DtoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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
        user.setPassword("password");
        assertEquals(dtoService.convertToDto(user).getPassword(), user.getPassword());
        assertEquals(dtoService.convertToDto(user).getUsername(), user.getUsername());
        assertEquals(dtoService.convertToDto(user).getEmail(), user.getEmail());
    }

    @Test
    public void testUserDtoToUserMapping() {
        UserDto userDto = new UserDto();
        userDto.setUsername("John");
        userDto.setEmail("john@john.com");
        userDto.setPassword("password");
        assertEquals(dtoService.convertToUser(userDto).getUsername(), userDto.getUsername());
        assertEquals(dtoService.convertToUser(userDto).getEmail(), userDto.getEmail());
        assertNull(dtoService.convertToUser(userDto).getPassword());
    }

}
