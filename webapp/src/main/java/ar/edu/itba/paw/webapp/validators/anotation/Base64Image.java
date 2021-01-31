package ar.edu.itba.paw.webapp.validators.anotation;

import ar.edu.itba.paw.webapp.validators.implementation.Base64ImageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {Base64ImageValidator.class})
public @interface Base64Image {
    String message() default "ar.edu.itba.paw.error.base64string.notimage";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        Base64Image[] value();
    }
}
