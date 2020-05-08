package ar.edu.itba.paw.webapp.validators.implementation;
import ar.edu.itba.paw.webapp.validators.anotation.FieldMatch;

import java.lang.reflect.Field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
    private String baseField;
    private String matchField;
 
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
            // log error
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