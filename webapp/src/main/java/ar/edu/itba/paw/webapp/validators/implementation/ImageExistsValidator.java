package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.paw.webapp.validators.anotation.ImageExists;

public class ImageExistsValidator implements ConstraintValidator<ImageExists, MultipartFile> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageExistsValidator.class);;

    @Override
    public void initialize(ImageExists imageExists) {}

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFile == null)
        {
            LOGGER.debug("Rejecting form. No image file.");
            return false;
        }
        if(multipartFile.isEmpty())
        {
            LOGGER.debug("Rejecting form. Image file is empty.");
            return false;
        }
		return true;
    }
}
