package gpk.practice.spring.bootmvc.validation.validator;

import gpk.practice.spring.bootmvc.validation.constraint.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return email.matches("[\\w]+@[\\w]+\\.[\\D]+");
    }

}
