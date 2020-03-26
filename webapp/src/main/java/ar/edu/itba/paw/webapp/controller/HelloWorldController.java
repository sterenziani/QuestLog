package ar.edu.itba.paw.webapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@Controller
public class HelloWorldController
{
	@Autowired
	private UserService us;
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchUser()
	{
		return new ModelAndView("404");
	}
	
	@RequestMapping("/")
	public ModelAndView helloWorld()
	{
		final ModelAndView mav = new ModelAndView("index");
		return mav;
	}
	
	@RequestMapping("/{id}")
	public ModelAndView userProfile(@PathVariable("id") long id)
	{
		final ModelAndView mav = new ModelAndView("user");
		mav.addObject("user", us.findById(id).orElseThrow(() -> new UserNotFoundException()));
		return mav;
	}
	
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam(value = "username", required = true) final String username) 
	{
		final User user = us.register(username);
		return new ModelAndView("redirect:/" + user.getId());
	}
}