package ar.edu.itba.paw.webapp.validators;

import java.lang.reflect.Field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.itba.paw.interfaces.UserService;

public class UserUniqueValidator implements ConstraintValidator<UserUnique, Object>
{
	@Autowired
	UserService us;
    private String username;
 
    @Override
    public void initialize(UserUnique constraint) {
    	username = constraint.username();
    }
 
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Object usernameValue = getFieldValue(object, username);
            return !us.findByUsername(usernameValue.toString()).isPresent();
        }
        catch (Exception e)
        {
            // log error
            return false;
        }
    }
 
    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field usernameField = clazz.getDeclaredField(fieldName);
        usernameField.setAccessible(true);
        return usernameField.get(object);
    }
}
