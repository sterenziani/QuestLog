package ar.edu.itba.paw.webapp.validators.implementation;

import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.webapp.validators.anotation.ImageUnique;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageUniqueValidator implements ConstraintValidator<ImageUnique, MultipartFile> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUniqueValidator.class);
    @Autowired
    ImageService is;

    @Override
    public void initialize(ImageUnique imageUnique) {}

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFile == null) {
            LOGGER.debug("No image file");
            return false;
        }
        if(multipartFile.isEmpty()){
            LOGGER.debug("Image file is empty");
            return false;
        }
        try {
            return !is.findByImageName(multipartFile.getName()).isPresent();
        } catch (Exception e){
            LOGGER.debug("Image name {} already in DB", multipartFile.getName());
            return false;
        }
    }
}
