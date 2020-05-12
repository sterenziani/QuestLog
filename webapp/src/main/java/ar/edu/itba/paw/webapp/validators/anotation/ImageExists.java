package ar.edu.itba.paw.webapp.validators.anotation;
import ar.edu.itba.paw.webapp.validators.implementation.ImageExistsValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ImageExistsValidator.class})
public @interface ImageExists {
    String                     message() default "ar.edu.itba.paw.error.image.exists";
    Class<?>[]                 groups()  default {};
    Class<? extends Payload>[] payload() default {};

    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        ImageExists[] value();
    }
}
