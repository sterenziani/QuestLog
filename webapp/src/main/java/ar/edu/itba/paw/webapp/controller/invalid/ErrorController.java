package ar.edu.itba.paw.webapp.controller.invalid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController
{
	@RequestMapping("/error400")
	public ModelAndView error_400()
	{
		ModelAndView m = new ModelAndView("error");
		m.addObject("msg", "error.400");
		return m;
	}
	
	@RequestMapping("/error404")
	public ModelAndView error_404()
	{
		ModelAndView m = new ModelAndView("error");
		m.addObject("msg", "error.404");
		return m;
	}
	
	@RequestMapping("/error403")
	public ModelAndView forbidden()
	{
		ModelAndView m = new ModelAndView("error");
		m.addObject("msg", "error.403");
		return m;
	}
}
