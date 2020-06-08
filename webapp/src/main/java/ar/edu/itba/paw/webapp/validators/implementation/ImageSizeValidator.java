package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ar.edu.itba.paw.webapp.validators.anotation.ImageSize;

public class ImageSizeValidator implements ConstraintValidator<ImageSize, MultipartFile>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageSizeValidator.class);
    
    private int min;
    private int max;

    @Override
    public void initialize(ImageSize constraint)
    {
        min = constraint.min();
        max = constraint.max();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext)
    {
    	try
    	{
            if(multipartFile.getSize() > max)
            {
                LOGGER.debug("Rejecting form. Image exceeds maximum size of {} bytes.", max);
                return false;
            }
            if(multipartFile.getSize() < min)
            {
                LOGGER.debug("Rejecting form. Image is under minimum size of {} bytes.", min);
                return false;
            }
            return true;
    	}
    	catch(Exception e)
    	{
    		LOGGER.error("Error when validating image size.", e);
    		return false;
    	}
    }
}
