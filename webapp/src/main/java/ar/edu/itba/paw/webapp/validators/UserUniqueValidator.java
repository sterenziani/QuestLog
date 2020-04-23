package ar.edu.itba.paw.webapp.validators;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.interfaces.UserService;

public class UserUniqueValidator implements ConstraintValidator<UserUnique, String>
{
	@Autowired
	UserService us;
	
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
            // log error
            return false;
        }
    }
}
