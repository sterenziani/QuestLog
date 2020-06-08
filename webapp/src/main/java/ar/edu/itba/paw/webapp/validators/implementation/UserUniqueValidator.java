package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.validators.anotation.UserUnique;

public class UserUniqueValidator implements ConstraintValidator<UserUnique, String>
{
	@Autowired
	private UserService us;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserUniqueValidator.class);
	
    @Override
    public void initialize(UserUnique constraint)
    {}
 
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        try
        {
            return !us.findByUsername(username).isPresent();
        }
        catch (Exception e)
        {
        	LOGGER.error("An exception was thrown when validating username {} was unique.", username, e);
            return false;
        }
    }
}
