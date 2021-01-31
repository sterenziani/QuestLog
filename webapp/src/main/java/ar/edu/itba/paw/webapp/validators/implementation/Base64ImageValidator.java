package ar.edu.itba.paw.webapp.validators.implementation;

import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.validators.anotation.Base64Image;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;

public class Base64ImageValidator implements ConstraintValidator<Base64Image, String> {

    private final static long MAX_MBS = 4000000;

    @Autowired
    private ImageService is;

    @Override
    public void initialize(Base64Image constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        if((value.length() * 3/(double) 4) > MAX_MBS){
            return false;
        }
        return is.isImage(value);
    }
}
