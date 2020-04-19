package ar.edu.itba.paw.webapp.controller;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;

@Controller
public class UserController
{
	@Autowired
	private UserService us;
	
	@RequestMapping(value = "/create", method = { RequestMethod.GET })
	public ModelAndView registerForm(@ModelAttribute("registerForm") final UserForm registerForm) 
	{
		return new ModelAndView("register");
	}
	
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public ModelAndView register(@Valid @ModelAttribute("registerForm") final UserForm registerForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) 
	{
		if (errors.hasErrors())
		{
			return registerForm(registerForm);
		}
		final User u = us.register(registerForm.getUsername(), registerForm.getPassword());
		// TO DO: Log in as user before redirecting
		
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping("/login")
	public ModelAndView login()
	{
		return new ModelAndView("login");
	}
	
	@RequestMapping("/users/{id}")
	public ModelAndView userProfile(@PathVariable("id") long id)
	{
		final ModelAndView mav = new ModelAndView("userProfile");
		mav.addObject("user", us.findById(id).orElseThrow(UserNotFoundException::new));
		return mav;
	}
}
