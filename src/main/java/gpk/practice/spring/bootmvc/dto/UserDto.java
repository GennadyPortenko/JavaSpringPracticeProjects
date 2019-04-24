package gpk.practice.spring.bootmvc.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotEmpty
    @Email
    @Getter
    @Setter
    String email;
    @NotEmpty
    @Getter
    @Setter
    String username;
    @NotEmpty
    @Getter
    @Setter
    String password;
}
