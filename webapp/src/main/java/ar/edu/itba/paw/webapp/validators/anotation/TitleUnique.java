package ar.edu.itba.paw.webapp.validators.anotation;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import ar.edu.itba.paw.webapp.validators.implementation.TitleUniqueValidator;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {TitleUniqueValidator.class})
public @interface TitleUnique
{
    String message() default "ar.edu.itba.paw.error.title.unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
    	TitleUnique[] value();
    }
}
