package gpk.practice.spring.bootmvc.unit;

import gpk.practice.spring.bootmvc.dto.UserDto;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationTest {

    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    static final String NAME = "name";
    static final String PASSWORD = "password";

    @Test
    public void testUserDtoValidation() {
        Stream.of( // valid emails
            "j@j.com", "j@1.com", "1@j.com", "1@1.com",
            "john12345@john12345.com", "john_john@john_john.com",
            "john@12345john12345.com",
            "john12345@john.com",
            "john@john.com"
        ).map(email -> new UserDto(email, NAME, PASSWORD)).forEach( userDto -> {
            Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
            List<Object> invalidValues= violations.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toList());
            assertFalse(invalidValues.contains(userDto.getEmail()));
        });

        Stream.of( // invalid emails
            "john@john12345", "john@ john.com", "john @john.com", "john @john. com",
            "john12345@a", "john@john", "john@", "@",
            "@john"
        ).map(email -> new UserDto(email, NAME, PASSWORD)).forEach( userDto -> {
            Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
            List<Object> invalidValues= violations.stream().map(ConstraintViolation::getInvalidValue).collect(Collectors.toList());
            assertTrue(invalidValues.contains(userDto.getEmail()));
        });

    }
}
