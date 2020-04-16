package ar.edu.itba.paw.webapp.controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@ControllerAdvice
public class ErrorController
{
	
	@RequestMapping("/403")
	public ModelAndView forbidden()
	{
		return new ModelAndView("403");
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchUser()
	{
		ModelAndView m = new ModelAndView("404");
		m.addObject("msg", "error.userNotExists");
		return m;
	}
	
	@ExceptionHandler(GameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchGame()
	{
		ModelAndView m = new ModelAndView("404");
		m.addObject("msg", "error.gameNotExists");
		return m;
	}
	
	@ExceptionHandler(PlatformNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchPlatform()
	{
		ModelAndView m = new ModelAndView("404");
		m.addObject("msg", "error.platformNotExists");
		return m;
	}
	
	@ExceptionHandler(DeveloperNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchDeveloper()
	{
		ModelAndView m = new ModelAndView("404");
		m.addObject("msg", "error.developerNotExists");
		return m;
	}
	
	@ExceptionHandler(GenreNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchGenre()
	{
		ModelAndView m = new ModelAndView("404");
		m.addObject("msg", "error.genreNotExists");
		return m;
	}
	
	@ExceptionHandler(PublisherNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchPublisher()
	{
		ModelAndView m = new ModelAndView("404");
		m.addObject("msg", "error.publisherNotExists");
		return m;
	}
}