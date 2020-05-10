package ar.edu.itba.paw.webapp.validators.implementation;
import ar.edu.itba.paw.webapp.validators.anotation.FieldMatch;

import java.lang.reflect.Field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
    private String baseField;
    private String matchField;
 
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldMatchValidator.class);
    
    @Override
    public void initialize(FieldMatch constraint) {
        baseField = constraint.baseField();
        matchField = constraint.matchField();
    }
 
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Object baseFieldValue = getFieldValue(object, baseField);
            Object matchFieldValue = getFieldValue(object, matchField);
            return baseFieldValue != null && baseFieldValue.equals(matchFieldValue);
        }
        catch (Exception e)
        {
            LOGGER.error("Exception thrown while validating fields were equal.");
            return false;
        }
    }
 
    private Object getFieldValue(Object object, String fieldName) throws Exception {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
 
}