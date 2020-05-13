package ar.edu.itba.paw.webapp.validators.implementation;
import java.lang.reflect.Field;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.webapp.validators.anotation.TitleUnique;

public class TitleUniqueValidator implements ConstraintValidator<TitleUnique, Object>
{
	@Autowired
	GameService gs;
	
	private String gameId;
	private String gameTitle;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TitleUniqueValidator.class);
	
    @Override
    public void initialize(TitleUnique constraint)
    {
    	gameId = constraint.gameId();
    	gameTitle = constraint.gameTitle();
    }
 
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context)
    {
        try
        {
            Object gameIdValue = getFieldValue(object, gameId);
            Object gameTitleValue = getFieldValue(object, gameTitle);
            Long id = (Long) gameIdValue;
            String title = gameTitleValue.toString();
            if(id == 0)
            {
                LOGGER.debug("Validating if game title {} is already taken before registering new game", id);
            	return !gs.findByTitle(gameTitleValue.toString()).isPresent();
            }
            if(gameTitleValue != null)
            {
            	LOGGER.debug("Validating if game title {} is already taken by a game without id {}", title, id);
            	Optional<Game> opt = gs.findByTitle(gameTitleValue.toString());
            	if(opt.isPresent())
            	{
            		LOGGER.debug("Game title {} is in use by game of ID {}", title, id);
            		return opt.get().getId() == id;
            	}
            }
        	LOGGER.debug("Game title {} is available. Form is validated.", title, id);
        	return true;
        }
        catch (Exception e)
        {
        	LOGGER.error("An exception was thrown when validating game title {} was unique.", gameTitle, e);
            return false;
        }
    }
    
    private Object getFieldValue(Object object, String fieldName) throws Exception
    {
        Class<?> clazz = object.getClass();
        Field passwordField = clazz.getDeclaredField(fieldName);
        passwordField.setAccessible(true);
        return passwordField.get(object);
    }
}
