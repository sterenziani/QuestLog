package ar.edu.itba.paw.webapp.controller;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.DeveloperService;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.GenreService;
import ar.edu.itba.paw.interfaces.PlatformService;
import ar.edu.itba.paw.interfaces.PublisherService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;

@Controller
public class MappingController
{
	@Autowired
	private GameService gs;

	@Autowired
	private UserService us;
	
	@Autowired
	private PlatformService ps;
	
	@Autowired
	private DeveloperService ds;
	
	@Autowired
	private GenreService gens;
	
	@Autowired
	private PublisherService pubs;

	// This doesn't throw exception. TO DO: Find a way to put it in ErrorController
	@RequestMapping(value="/error404", method = RequestMethod.GET)
	public ModelAndView error_404()
	{
		ModelAndView m = new ModelAndView("error");
		m.addObject("msg", "Error 404");
		return m;
	}
	
	// This doesn't throw exception. TO DO: Find a way to put it in ErrorController
	@RequestMapping(value="/error400", method = RequestMethod.GET)
	public ModelAndView error_400()
	{
		ModelAndView m = new ModelAndView("error");
		m.addObject("msg", "Error 400");
		return m;
	}
	
	@RequestMapping("/")
	public ModelAndView index(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("cookieBacklog", gs.getGamesInBacklog(backlog));
		mav.addObject("backlogGames", gs.getGamesInBacklog(backlog));
		mav.addObject("upcomingGames", gs.getUpcomingGamesSimplified(backlog));
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowIndex(@RequestParam long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("backlogGames", gs.getGamesInBacklog(backlog));
		mav.addObject("upcomingGames", gs.getUpcomingGamesSimplified(backlog));
		return mav;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String search, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("games");
		mav.addObject("searchTerm", search);
		mav.addObject("games", gs.searchByTitleSimplified(search, backlog));
		return mav;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndContinueSearch(@RequestParam String search, @RequestParam long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		final ModelAndView mav = new ModelAndView("games");
		mav.addObject("searchTerm", search);
		mav.addObject("games", gs.searchByTitleSimplified(search, backlog));
		return mav;
	}
	
	@RequestMapping("/games")
	public ModelAndView gamesList(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("allGames");
		mav.addObject("games", gs.getAllGamesSimplified(backlog));
		return mav;
	}
	
	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ModelAndView gamesList(@RequestParam long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		final ModelAndView mav = new ModelAndView("allGames");
		mav.addObject("games", gs.getAllGamesSimplified(backlog));
		return mav;
	}
	
	@RequestMapping("/games/{id}")
	public ModelAndView gameProfile(@PathVariable("id") long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("game");
		mav.addObject("game", gs.findById(id, backlog).orElseThrow(GameNotFoundException::new));				
		return mav;
	}

	@RequestMapping(value = "/games/{id}", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowGameProfile(@PathVariable("id") long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		final ModelAndView mav = new ModelAndView("game");
		mav.addObject("game", gs.findById(id, backlog).orElseThrow(GameNotFoundException::new));
		return mav;
	}
	

	@RequestMapping("/platforms")
	public ModelAndView platformsList()
	{
		final ModelAndView mav = new ModelAndView("platformsList");
		mav.addObject("platforms", ps.getAllPlatforms());
		return mav;
	}
	
	@RequestMapping("/platforms/{platformId}")
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId)
	{
		final ModelAndView mav = new ModelAndView("platform");
		mav.addObject("platform", ps.findById(platformId).orElseThrow(PlatformNotFoundException::new));
		return mav;
	}
	
	@RequestMapping(value = "/platforms/{platformId}", method = RequestMethod.POST)
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		final ModelAndView mav = new ModelAndView("platform");
		mav.addObject("platform", ps.findById(platformId, backlog).orElseThrow(PlatformNotFoundException::new));
		return mav;
	}
	

	@RequestMapping("/developers")
	public ModelAndView developersList()
	{
		final ModelAndView mav = new ModelAndView("developersList");
		mav.addObject("developers", ds.getAllDevelopers());
		return mav;
	}
	
	@RequestMapping("/developers/{devId}")
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("developer");
		mav.addObject("developer", ds.findById(devId, backlog).orElseThrow(DeveloperNotFoundException::new));
		return mav;
	}
	
	@RequestMapping(value = "/developers/{devId}", method = RequestMethod.POST)
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @RequestParam long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		return new ModelAndView("redirect:/developers/{devId}");
	}
	
	@RequestMapping("/publishers")
	public ModelAndView publishersList()
	{
		final ModelAndView mav = new ModelAndView("publishersList");
		mav.addObject("publishers", pubs.getAllPublishers());
		return mav;
	}
	
	@RequestMapping("/publishers/{pubId}")
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("publisher");
		mav.addObject("publisher", pubs.findById(pubId, backlog).orElseThrow(PublisherNotFoundException::new));
		return mav;
	}
	
	@RequestMapping(value = "/publishers/{pubId}", method = RequestMethod.POST)
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @RequestParam long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		return new ModelAndView("redirect:/publishers/{pubId}");
	}
	
	@RequestMapping("/genres")
	public ModelAndView genresList()
	{
		final ModelAndView mav = new ModelAndView("genresList");
		mav.addObject("genres", gens.getAllGenres());
		return mav;
	}
	
	@RequestMapping("/genres/{id}")
	public ModelAndView genreProfile(@PathVariable("id") long id)
	{
		final ModelAndView mav = new ModelAndView("genre");
		mav.addObject("genre", gens.findById(id).orElseThrow(GenreNotFoundException::new));
		return mav;
	}
	
	@RequestMapping(value = "/genres/{genreId}", method = RequestMethod.POST)
	public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @RequestParam long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(id, response, backlog);
		final ModelAndView mav = new ModelAndView("genre");
		mav.addObject("genre", gens.findById(genreId, backlog).orElseThrow(GenreNotFoundException::new));
		return mav;
	}
	
	@RequestMapping("/explore")
	public ModelAndView explore()
	{
		final ModelAndView mav = new ModelAndView("explore");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("developers", ds.getAllDevelopers());
		mav.addObject("publishers", pubs.getAllPublishers());
		mav.addObject("genres", gens.getAllGenres());
		return mav;
	}
	
	public void clearAnonBacklog(HttpServletResponse response)
	{
		Cookie cookie = new Cookie("backlog", "");
		cookie.setPath("/");
		cookie.setMaxAge(600000);
		response.addCookie(cookie);
	}
	
	private String toggleBacklog(long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue=" ") String backlog)
	{
		String newBacklog = gs.toggleBacklog(backlog, gameId);
		Cookie cookie = new Cookie("backlog", newBacklog);
		cookie.setPath("/");
		cookie.setMaxAge(600000);
		response.addCookie(cookie);
		return newBacklog;
	}
	
	/*
	@RequestMapping(value = "/transfer_backlog", method = RequestMethod.POST)
	public ModelAndView transferBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		User u = loggedUser();
		if(u != null)
			gs.addToUserBacklog(backlog, u);
		clearAnonBacklog(response);		
		return new ModelAndView("redirect:/");
	}
	*/
}