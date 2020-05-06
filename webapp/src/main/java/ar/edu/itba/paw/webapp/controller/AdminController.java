package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.webapp.exception.BadImageException;
import ar.edu.itba.paw.webapp.form.GameForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class AdminController
{
	@Autowired
	private ImageService is;

	@RequestMapping("/admin")
	public ModelAndView admin()
	{
		return new ModelAndView("admin/admin");
	}

	@RequestMapping(
			value = "/admin/game",
			method = RequestMethod.GET
	)
	public ModelAndView gameEdit(@ModelAttribute("gameForm") final GameForm gameForm){
		return new ModelAndView("admin/gameForm");
	}

	@RequestMapping(
			value = "/admin/game",
			method = RequestMethod.POST
	)
	public ModelAndView gameUpdate(@Valid @ModelAttribute("gameForm") final GameForm gameForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
		if(errors.hasErrors()){
			return gameEdit(gameForm);
		}
		try {
			final Image image = is.uploadImage(gameForm.getImage().getOriginalFilename(), gameForm.getImage().getBytes());
		} catch (IOException e){
			throw new BadImageException();
		}
		return new ModelAndView("admin/gameForm");
	}
}
