package ar.edu.itba.paw.webapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.paw.interfaces.UserService;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String>
{
	@Autowired
	UserService us;
	
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
            // log error
            return false;
        }
    }
}
