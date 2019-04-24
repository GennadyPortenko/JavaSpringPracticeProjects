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
        assertEquals(dtoService.convertToDto(user).getUsername(), user.getUsername());
    }

    @Test
    public void testUserDtoToUserMapping() {
        UserDto userDto = new UserDto();
        userDto.setUsername("John");
        userDto.setEmail("john@john.com");
        assertEquals(dtoService.convertToUser(userDto).getUsername(), userDto.getUsername());
    }

}
