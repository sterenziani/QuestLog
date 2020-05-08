package ar.edu.itba.paw.webapp.validators.anotation;
import ar.edu.itba.paw.webapp.validators.implementation.FieldMatchValidator;

import javax.validation.Payload;
import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {FieldMatchValidator.class})
public @interface FieldMatch
{
    String message() default "ar.edu.itba.paw.error.passwords.match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String baseField();
    String matchField();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        FieldMatch[] value();
    }
}