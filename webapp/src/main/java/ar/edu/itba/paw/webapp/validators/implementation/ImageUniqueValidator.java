package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.webapp.validators.anotation.ImageUnique;

public class ImageUniqueValidator implements ConstraintValidator<ImageUnique, MultipartFile> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUniqueValidator.class);
    @Autowired
    ImageService is;

    @Override
    public void initialize(ImageUnique imageUnique) {}

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
        try
        {
        	if(!is.findByImageName(multipartFile.getOriginalFilename()).isPresent())
        		return true;
        	LOGGER.debug("Rejecting form. Image filename {} is already taken in the database.", multipartFile.getOriginalFilename());
            return false;
        } catch (Exception e){
            LOGGER.error("Exception thrown while looking up if image of name {} exists in DB.", multipartFile.getName(), e);
            return false;
        }
    }
}
