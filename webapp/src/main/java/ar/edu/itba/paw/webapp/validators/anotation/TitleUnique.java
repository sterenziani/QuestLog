package ar.edu.itba.paw.webapp.validators.anotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import ar.edu.itba.paw.webapp.validators.implementation.TitleUniqueValidator;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {TitleUniqueValidator.class})
public @interface TitleUnique
{
    String message() default "ar.edu.itba.paw.error.title.unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String gameId();
    String gameTitle();
    
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
    	TitleUnique[] value();
    }
}
