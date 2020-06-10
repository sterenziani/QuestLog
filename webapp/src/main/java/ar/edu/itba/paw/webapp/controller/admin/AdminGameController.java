package ar.edu.itba.paw.webapp.controller.admin;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.interfaces.service.RegionService;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Region;
import ar.edu.itba.paw.model.exception.BadFormatException;
import ar.edu.itba.paw.webapp.exception.BadImageException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.form.GameForm;

@Controller
public class AdminGameController
{
	@Autowired
	private GameService			gs;

	@Autowired
	private PlatformService 	ps;

	@Autowired
	private DeveloperService 	ds;

	@Autowired
	private PublisherService 	pubs;

	@Autowired
	private GenreService 		gens;

	@Autowired
	private RegionService 		rs;

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminGameController.class);
	
	@RequestMapping(value = "/admin/game/new", method = RequestMethod.GET)
	public ModelAndView newGame(@ModelAttribute("gameForm") final GameForm gameForm)
	{
		ModelAndView mav = new ModelAndView("admin/gameForm");
		List<Platform> 	platforms 	= ps.getAllPlatforms();
		List<Developer> developers 	= ds.getAllDevelopers();
		List<Publisher> publishers 	= pubs.getAllPublishers();
		List<Genre> 	genres 		= gens.getAllGenres();
		List<Region>	regions		= rs.getAllRegions();
		mav.addObject("allPlatforms", platforms);
		mav.addObject("allDevelopers", developers);
		mav.addObject("allPublishers", publishers);
		mav.addObject("allGenres", genres);
		mav.addObject("allRegions", regions);
		return mav;
	}

	@RequestMapping(value = "/admin/game/new", method = RequestMethod.POST)
	public ModelAndView createGame(@Valid @ModelAttribute("gameForm") final GameForm gameForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	{
		if(errors.hasErrors())
			return newGame(gameForm);
		try
		{
			LOGGER.debug("Registering game {} to the database. Using file {} as cover.", gameForm.getTitle(), gameForm.getCover().getOriginalFilename());
			final Game g = gs.register(gameForm.getTitle(), gameForm.getCover(), gameForm.getDescription(), gameForm.getPlatforms(), gameForm.getDevelopers(), gameForm.getPublishers(), gameForm.getGenres(), gameForm.getReleaseDates());
			LOGGER.debug("Game {} successfully registered!", gameForm.getTitle());
			return new ModelAndView("redirect:/games/" + g.getId());
		}
		catch (BadFormatException e)
		{
			LOGGER.error("IOException thrown when attempting to upload image {} to the database while creating game {}.", gameForm.getCover().getOriginalFilename(), gameForm.getTitle(), e);
			throw new BadImageException();
		}
	}

	@RequestMapping(value = "/admin/game/{game_id}/edit", method = RequestMethod.GET)
	public ModelAndView editGame(@PathVariable("game_id") long id, @ModelAttribute("gameForm") GameForm gameForm)
	{
		ModelAndView mav = new ModelAndView("admin/gameForm");
		Optional<Game> optg 		= gs.findByIdWithDetails(id);
		if(!optg.isPresent())
			throw new GameNotFoundException();
		Game g 						= optg.get();
		gameForm 					= new GameForm(g);
		List<Platform> 	platforms 	= ps.getAllPlatforms();
		List<Developer> developers 	= ds.getAllDevelopers();
		List<Publisher> publishers 	= pubs.getAllPublishers();
		List<Genre> 	genres 		= gens.getAllGenres();
		List<Region>	regions		= rs.getAllRegions();
		mav.addObject("gameId", id);
		mav.addObject("gameForm", gameForm);
		mav.addObject("allPlatforms", platforms);
		mav.addObject("allDevelopers", developers);
		mav.addObject("allPublishers", publishers);
		mav.addObject("allGenres", genres);
		mav.addObject("allRegions", regions);
		mav.addObject("editMode", true);
		return mav;
	}

	public ModelAndView errorEditGame(@PathVariable("game_id") long id, @ModelAttribute("gameForm") GameForm gameForm)
	{
		ModelAndView mav = new ModelAndView("admin/gameForm");
		List<Platform> 	platforms 	= ps.getAllPlatforms();
		List<Developer> developers 	= ds.getAllDevelopers();
		List<Publisher> publishers 	= pubs.getAllPublishers();
		List<Genre> 	genres 		= gens.getAllGenres();
		List<Region>	regions		= rs.getAllRegions();
		mav.addObject("gameForm", gameForm);
		mav.addObject("allPlatforms", platforms);
		mav.addObject("allDevelopers", developers);
		mav.addObject("allPublishers", publishers);
		mav.addObject("allGenres", genres);
		mav.addObject("allRegions", regions);
		mav.addObject("editMode", true);
		return mav;
	}

	@RequestMapping(value = "/admin/game/{game_id}/edit", method = RequestMethod.POST)
	public ModelAndView updateGame(@PathVariable("game_id") long id, @Valid @ModelAttribute("gameForm") final GameForm gameForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	{
		if(errors.hasErrors())
			return errorEditGame(id, gameForm);
		try
		{
			gs.update(id, gameForm.getTitle(), gameForm.getCover(), gameForm.getDescription(), gameForm.getPlatforms(), gameForm.getDevelopers(), gameForm.getPublishers(), gameForm.getGenres(), gameForm.getReleaseDates());
			return new ModelAndView("redirect:/games/" + id);
		}
		catch (BadFormatException e)
		{
			LOGGER.error("The image provided for game {} threw an exception", gameForm.getTitle(), e);
			throw new BadImageException();
		}
	}

	@RequestMapping(value = "/admin/game/{id}/delete", method = RequestMethod.POST)
	public ModelAndView deleteGame(@PathVariable("id") final long id, HttpServletRequest request)
	{
		gs.removeById(id);
		String referer = request.getHeader("Referer");
		if(referer == null){
			return new ModelAndView("redirect:/");
		}
		return new ModelAndView("redirect:" + referer);
	}

	@RequestMapping(value = "/admin/game/{id}/delete/fromdetails", method = RequestMethod.POST)
	public ModelAndView deleteFromDetailsGame(@PathVariable("id") final long id, HttpServletRequest request)
	{
		gs.removeById(id);
		return new ModelAndView("redirect:/");
	}
}
