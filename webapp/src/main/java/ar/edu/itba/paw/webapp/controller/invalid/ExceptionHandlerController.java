package ar.edu.itba.paw.webapp.controller.invalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import ar.edu.itba.paw.webapp.exception.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exception.RunsNotEnabledException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@ControllerAdvice
public class ExceptionHandlerController
{	
	@Autowired
	private UserService us;
	
	@ExceptionHandler(ImageNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NoSuchImage()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.imageNotExists");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NoSuchUser()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.userNotExists");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}
	
	@ExceptionHandler(GameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NoSuchGame()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.gameNotExists");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}
	
	@ExceptionHandler(RunsNotEnabledException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView PageNotAvailable()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.runNotAvailable");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}
	
	@ExceptionHandler(PlatformNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NoSuchPlatform()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.platformNotExists");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}
	
	@ExceptionHandler(DeveloperNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NoSuchDeveloper()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.developerNotExists");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}
	
	@ExceptionHandler(GenreNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NoSuchGenre()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.genreNotExists");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}
	
	@ExceptionHandler(PublisherNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NoSuchPublisher()
	{
		ModelAndView m = new ModelAndView("error/errorException");
		m.addObject("msg", "error.publisherNotExists");
		m.addObject("loggedUser", us.getLoggedUser());
		return m;
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView notFoundHandler()
	{
		ModelAndView m = new ModelAndView("error/error");
		m.addObject("msg", "error.500");
		return m;
	}
}