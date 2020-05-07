package ar.edu.itba.paw.webapp.validators.anotation;
import ar.edu.itba.paw.webapp.validators.implementation.EmailNotUniqueValidator;
import ar.edu.itba.paw.webapp.validators.implementation.EmailUniqueValidator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {EmailNotUniqueValidator.class})
public @interface EmailNotUnique
{
    String message() default "ar.edu.itba.paw.error.email.notUnique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
    	EmailNotUnique[] value();
    }
}
