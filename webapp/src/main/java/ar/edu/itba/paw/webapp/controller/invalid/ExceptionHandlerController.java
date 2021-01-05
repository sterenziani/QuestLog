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
import ar.edu.itba.paw.webapp.exception.ScoresNotEnabledException;
import ar.edu.itba.paw.webapp.exception.TokenNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;
import ar.edu.itba.paw.webapp.exception.ReviewsNotEnabledException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandlerController {

	@ExceptionHandler(NoHandlerFoundException.class)
	public String error404() {
		return "forward:/index.html";
	}
}
