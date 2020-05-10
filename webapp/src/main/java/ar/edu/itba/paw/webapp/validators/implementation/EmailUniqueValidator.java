package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.validators.anotation.EmailUnique;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String>
{
	@Autowired
	UserService us;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailUniqueValidator.class);
	
    @Override
    public void initialize(EmailUnique constraint)
    {}
 
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        try
        {
            return !us.findByEmail(email).isPresent();
        }
        catch (Exception e)
        {
            LOGGER.error("An exception was thrown when validating email {} was unique.", email, e);
            return false;
        }
    }
}
