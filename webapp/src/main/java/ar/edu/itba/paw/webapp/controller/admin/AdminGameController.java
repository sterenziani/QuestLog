package ar.edu.itba.paw.webapp.controller.admin;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.exception.BadImageException;
import ar.edu.itba.paw.webapp.form.GameForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminGameController
{
	@Autowired
	private GameService			gs;

	@Autowired
	private ImageService 		is;

	@Autowired
	private PlatformService 	ps;

	@Autowired
	private DeveloperService 	ds;

	@Autowired
	private PublisherService 	pubs;

	@Autowired
	private GenreService 		gens;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminGameController.class);
	
	@RequestMapping("/admin")
	public ModelAndView admin()
	{
		return new ModelAndView("admin/admin");
	}

	@RequestMapping(value = "/admin/game/new", method = RequestMethod.GET)
	public ModelAndView newGame(@ModelAttribute("gameForm") final GameForm gameForm)
	{
		ModelAndView mav = new ModelAndView("admin/game/gameForm");
		List<Platform> 	platforms 	= ps.getAllPlatforms();
		List<Developer> developers 	= ds.getAllDevelopers();
		List<Publisher> publishers 	= pubs.getAllPublishers();
		List<Genre> 	genres 		= gens.getAllGenres();
		mav.addObject("platforms", platforms);
		mav.addObject("developers", developers);
		mav.addObject("publishers", publishers);
		mav.addObject("genres", genres);
		return mav;
	}

	@RequestMapping(value = "/admin/game/new", method = RequestMethod.POST)
	public ModelAndView createGame(@Valid @ModelAttribute("gameForm") final GameForm gameForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	{
		if(errors.hasErrors())
			return newGame(gameForm);
		final Image image;
		try
		{
			LOGGER.debug("Registering game {} to the database. Using file {} as cover.", gameForm.getTitle(), gameForm.getCover().getOriginalFilename());
			image = is.uploadImage(gameForm.getCover().getOriginalFilename(), gameForm.getCover().getBytes());
		}
		catch (IOException e)
		{
			LOGGER.error("IOException thrown when attempting to upload image {} to the database while creating game {}.", gameForm.getCover().getOriginalFilename(), gameForm.getTitle(), e);
			throw new BadImageException();
		}
		Game g = gs.register(gameForm.getTitle(), image.getImageName(), gameForm.getDescription(), gameForm.getPlatforms(), gameForm.getDevelopers(), gameForm.getPublishers(), gameForm.getGenres(), new LocalDate[0]);
		return new ModelAndView("redirect:/games/" + g.getId());
	}

	@RequestMapping(value = "/admin/game/{id}/edit", method = RequestMethod.GET)
	public ModelAndView editGame(@ModelAttribute("gameForm") final GameForm gameForm)
	{
		ModelAndView mav = new ModelAndView("admin/game/gameForm");
		//mav.addObject("platforms", platforms);
		return mav;
	}

	@RequestMapping(value = "/admin/game/{id}/edit", method = RequestMethod.POST)
	public ModelAndView updateGame(@Valid @ModelAttribute("gameForm") final GameForm gameForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	{
		if(errors.hasErrors())
			return editGame(gameForm);
		try
		{
			final Image image = is.uploadImage(gameForm.getCover().getOriginalFilename(), gameForm.getCover().getBytes());
		}
		catch (IOException e)
		{
			LOGGER.error("The image provided for game {} threw an exception", gameForm.getTitle(), e);
			throw new BadImageException();
		}
		ModelAndView mav = new ModelAndView("admin/game/gameForm");
		//mav.addObject("platforms", platforms);
		return mav;
	}
}