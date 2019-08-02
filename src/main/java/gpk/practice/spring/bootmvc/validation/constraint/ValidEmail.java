package gpk.practice.spring.bootmvc.validation.constraint;

import gpk.practice.spring.bootmvc.validation.validator.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EmailValidator.class)
@Target( { ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "Неверный формат email.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
