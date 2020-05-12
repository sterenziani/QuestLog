package ar.edu.itba.paw.webapp.validators.implementation;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.webapp.validators.anotation.TitleUnique;

public class TitleUniqueValidator implements ConstraintValidator<TitleUnique, String>
{
	@Autowired
	GameService gs;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TitleUniqueValidator.class);
	
    @Override
    public void initialize(TitleUnique constraint)
    {}
 
    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        try
        {
            return !gs.findByTitle(title).isPresent();
        }
        catch (Exception e)
        {
            LOGGER.error("An exception was thrown when validating game title {} was unique.", title, e);
            return false;
        }
    }
}
