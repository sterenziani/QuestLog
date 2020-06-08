package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.validators.anotation.EmailNotUnique;

public class EmailNotUniqueValidator implements ConstraintValidator<EmailNotUnique, String>
{
	@Autowired
	private UserService us;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotUniqueValidator.class);
	
    @Override
    public void initialize(EmailNotUnique constraint)
    {}
 
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        try
        {
            return us.findByEmail(email).isPresent();
        }
        catch (Exception e)
        {
        	LOGGER.error("An exception was thrown when validating email {} was registered in the database.", email, e);
            return false;
        }
    }
}
