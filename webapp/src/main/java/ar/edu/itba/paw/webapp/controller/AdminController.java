package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
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
import java.util.List;

@Controller
public class AdminController
{
	@Autowired
	private ImageService 		is;

	@Autowired
	private PlatformService 	ps;

	@Autowired
	private DeveloperService 	ds;

	@Autowired
	private PublisherService 	pubs;

	@Autowired
	private GenreService 		gs;

	@RequestMapping("/admin")
	public ModelAndView admin()
	{
		return new ModelAndView("admin/admin");
	}

	@RequestMapping(
			value = "/admin/game/new",
			method = RequestMethod.GET
	)
	public ModelAndView newGame(@ModelAttribute("gameForm") final GameForm gameForm){
		ModelAndView mav = new ModelAndView("admin/game/gameForm");
		List<Platform> 	platforms 	= ps.getAllPlatforms();
		List<Developer> developers 	= ds.getAllDevelopers();
		List<Publisher> publishers 	= pubs.getAllPublishers();
		List<Genre> 	genres 		= gs.getAllGenres();
		mav.addObject("platforms", platforms);
		mav.addObject("developers", developers);
		mav.addObject("publishers", publishers);
		mav.addObject("genres", genres);
		return mav;
	}

	@RequestMapping(
			value = "/admin/game/new",
			method = RequestMethod.POST
	)
	public ModelAndView createGame(@Valid @ModelAttribute("gameForm") final GameForm gameForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
		if(errors.hasErrors()){
			return newGame(gameForm);
		}
		try {
			final Image image = is.uploadImage(gameForm.getCover().getOriginalFilename(), gameForm.getCover().getBytes());
		} catch (IOException e){
			throw new BadImageException();
		}
		ModelAndView mav = new ModelAndView("admin/game/gameForm");
		//mav.addObject("platforms", platforms);
		return mav;
	}

	@RequestMapping(
			value = "/admin/game/{id}/edit",
			method = RequestMethod.GET
	)
	public ModelAndView editGame(@ModelAttribute("gameForm") final GameForm gameForm){
		ModelAndView mav = new ModelAndView("admin/game/gameForm");
		//mav.addObject("platforms", platforms);
		return mav;
	}

	@RequestMapping(
			value = "/admin/game/{id}/edit",
			method = RequestMethod.POST
	)
	public ModelAndView updateGame(@Valid @ModelAttribute("gameForm") final GameForm gameForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response){
		if(errors.hasErrors()){
			return editGame(gameForm);
		}
		try {
			final Image image = is.uploadImage(gameForm.getCover().getOriginalFilename(), gameForm.getCover().getBytes());
		} catch (IOException e){
			throw new BadImageException();
		}
		ModelAndView mav = new ModelAndView("admin/game/gameForm");
		//mav.addObject("platforms", platforms);
		return mav;
	}
}
