package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import ar.edu.itba.paw.webapp.validators.anotation.EmailNotUnique;
import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.paw.interfaces.service.UserService;

public class EmailNotUniqueValidator implements ConstraintValidator<EmailNotUnique, String>
{
	@Autowired
	UserService us;
	
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
            // log error
            return false;
        }
    }
}
