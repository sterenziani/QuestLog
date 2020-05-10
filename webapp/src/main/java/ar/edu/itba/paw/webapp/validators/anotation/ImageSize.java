package ar.edu.itba.paw.webapp.validators.anotation;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import ar.edu.itba.paw.webapp.validators.implementation.ImageSizeValidator;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ImageSizeValidator.class})
public @interface ImageSize {
    String                     message() default "ar.edu.itba.paw.error.image.unique";
    Class<?>[]                 groups()  default {};
    Class<? extends Payload>[] payload() default {};
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    
    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        ImageSize[] value();
    }
}
