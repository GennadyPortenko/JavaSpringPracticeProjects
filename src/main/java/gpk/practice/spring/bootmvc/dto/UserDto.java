package gpk.practice.spring.bootmvc.dto;

import gpk.practice.spring.bootmvc.validation.constraint.ValidEmail;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotEmpty
    @ValidEmail
    String email;
    @NotEmpty
    String name;
    @NotEmpty
    String password;
}
