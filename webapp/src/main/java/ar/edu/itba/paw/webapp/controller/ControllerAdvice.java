package ar.edu.itba.paw.webapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice
{
	@Autowired
	private UserService us;
	
	@ModelAttribute("loggedUser")
	public User loggedUser()
	{
		return us.getLoggedUser();
	}
}
