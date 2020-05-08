package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ar.edu.itba.paw.webapp.validators.anotation.UserUnique;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.interfaces.service.UserService;

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
