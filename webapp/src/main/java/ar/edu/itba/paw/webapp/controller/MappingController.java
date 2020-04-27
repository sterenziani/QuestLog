package ar.edu.itba.paw.webapp.controller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import ar.edu.itba.paw.interfaces.ReviewService;
import ar.edu.itba.paw.interfaces.RunService;
import ar.edu.itba.paw.interfaces.ScoreService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;

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
	
	@Autowired
	private ReviewService revs;
	
	@Autowired
	private RunService runs;
	
	@Autowired
	private ScoreService scors;

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
		mav.addObject("cookieBacklog", backlog);
		User u = loggedUser();
		if(u == null)
		{
			mav.addObject("backlogGames", getGamesInBacklog(backlog));
			mav.addObject("popularGames", gs.getPopularGames(null));
			mav.addObject("upcomingGames", getUpcomingGames(backlog));
		}
		else
		{
			mav.addObject("backlogGames", gs.getGamesInBacklog(u));
			mav.addObject("recommendedGames", gs.getRecommendedGames(u));
			mav.addObject("popularGames", gs.getPopularGames(u));
			mav.addObject("upcomingGames", gs.getUpcomingGames(u));
		}
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowIndex(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String search, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("games");
		User u = loggedUser();
		if(u == null)
		{
			List<Game> searchResults = gs.searchByTitle(search, null);
			updateWithBacklogDetails(searchResults, backlog);
			mav.addObject("searchTerm", search);
			mav.addObject("games", searchResults);
		}
		else
		{
			mav.addObject("searchTerm", search);
			mav.addObject("games", gs.searchByTitle(search, u));
		}
		return mav;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndContinueSearch(@RequestParam String search, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		final ModelAndView mav = new ModelAndView("games");
		User u = loggedUser();
		if(u == null)
		{
			List<Game> searchResults = gs.searchByTitle(search, null);
			updateWithBacklogDetails(searchResults, backlog);
			mav.addObject("searchTerm", search);
			mav.addObject("games", searchResults);
		}
		else
		{
			mav.addObject("searchTerm", search);
			mav.addObject("games", gs.searchByTitle(search, u));
		}
		return mav;
	}
	
	@RequestMapping("/games")
	public ModelAndView gamesList(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("allGames");
		User u = loggedUser();
		if(u == null)
		{
			List<Game> games = gs.getAllGames(null);
			updateWithBacklogDetails(games, backlog);
			mav.addObject("games", games);
		}
		else
		{
			mav.addObject("games", gs.getAllGames(u));
		}
		return mav;
	}
	

	
	@RequestMapping(value = "/games/scores/{gameId}", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam("score") int scoreInput, @RequestParam("game") long gameId) 
	{
		User user = loggedUser();
		if(user == null)
			return new ModelAndView("redirect:/games/{gameId}");
		Optional <Game> game = gs.findByIdWithDetails(gameId, user);
		Optional<Score> score = scors.findScore(user, game.get());
		if (score.isPresent())
			scors.changeScore(scoreInput, user, game.get());
		else
			scors.register(user, game.get(), scoreInput);
		return new ModelAndView("redirect:/games/{gameId}");
	}
	
	@RequestMapping(value = "/createRun/run/{gameId}", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam("hours") int hours, @RequestParam("mins") int mins, @RequestParam("secs") int secs,
			@RequestParam("game") long gameId, @RequestParam("platforms") String platform, @RequestParam("playstyles") String playst) 
	{
		User user = loggedUser();
		if(user == null)
			return new ModelAndView("redirect:/games/{gameId}");
		Optional <Game> game = gs.findByIdWithDetails(gameId, user);
		Optional <Platform> plat = ps.findByName(platform);
		long time = hours*3600 + mins*60 + secs;
		Optional <Playstyle> play = runs.findPlaystyleByName(playst);
		if(game.isPresent() && plat.isPresent() && play.isPresent())
			runs.register(user, game.get(), plat.get(), play.get(), time);
		return new ModelAndView("redirect:/games/{gameId}");
	}	
	
	@RequestMapping("/createRun/{gameId}")
	public ModelAndView createRun(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		User u = loggedUser();
		if(u == null)
			return new ModelAndView("redirect:/games/{gameId}");
		final ModelAndView mav = new ModelAndView("createRun");
		Game g = gs.findByIdWithDetails(gameId, loggedUser()).orElseThrow(GameNotFoundException::new);
		mav.addObject("game",g);
		mav.addObject("playstyles",runs.getAllPlaystyles());
		return mav;
	}

	
	
	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ModelAndView gamesList(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/games");
	}
	
	@RequestMapping("/games/{gameId}")
	public ModelAndView gameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("game");
		User u = loggedUser();
		Game g = gs.findByIdWithDetails(gameId, u).orElseThrow(GameNotFoundException::new);
		mav.addObject("playAverage", runs.getAverageAllPlayStyles(g));
		mav.addObject("averageScore", scors.findAverageScore(g));
		if(u == null)
		{	
			g.setInBacklog(gameInBacklog(gameId, backlog));
			mav.addObject("game", g);
		}
		else
		{	
			mav.addObject("game", g);
			Optional<Score> sc = scors.findScore(u,g);
			if(sc.isPresent())
				mav.addObject("user_score",sc.get());
			else
				mav.addObject("user_score",null);
		}
		return mav;
	}

	@RequestMapping(value = "/games/{gameId}", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowGameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/games/{gameId}");
	}
	

	@RequestMapping("/platforms")
	public ModelAndView platformsList()
	{
		final ModelAndView mav = new ModelAndView("allPlatforms");
		mav.addObject("platforms", ps.getAllPlatforms());
		return mav;
	}
	
	@RequestMapping("/platforms/{platformId}")
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("platform");
		User u = loggedUser();
		if(u == null)
		{
			Platform p = ps.findById(platformId).orElseThrow(PlatformNotFoundException::new);
			updateWithBacklogDetails(p.getGames(), backlog);
			mav.addObject("platform", p);
		}
		else
		{
			mav.addObject("platform", ps.findById(platformId, u).orElseThrow(PlatformNotFoundException::new));
		}
		return mav;
	}
	
	@RequestMapping(value = "/platforms/{platformId}", method = RequestMethod.POST)
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/platforms/{platformId}");
	}
	
	@RequestMapping("/developers")
	public ModelAndView developersList()
	{
		final ModelAndView mav = new ModelAndView("allDevelopers");
		mav.addObject("developers", ds.getAllDevelopers());
		return mav;
	}
	
	@RequestMapping("/developers/{devId}")
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("developer");
		User u = loggedUser();
		if(u == null)
		{
			Developer d = ds.findById(devId).orElseThrow(DeveloperNotFoundException::new);
			updateWithBacklogDetails(d.getGames(), backlog);
			mav.addObject("developer", d);
		}
		else
		{
			mav.addObject("developer", ds.findById(devId, u).orElseThrow(PlatformNotFoundException::new));
		}
		return mav;
	}
	
	@RequestMapping(value = "/developers/{devId}", method = RequestMethod.POST)
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/developers/{devId}");
	}
	
	@RequestMapping("/publishers")
	public ModelAndView publishersList()
	{
		final ModelAndView mav = new ModelAndView("allPublishers");
		mav.addObject("publishers", pubs.getAllPublishers());
		return mav;
	}
	
	@RequestMapping("/publishers/{pubId}")
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("publisher");
		User u = loggedUser();
		if(u == null)
		{
			Publisher p = pubs.findById(pubId, u).orElseThrow(PublisherNotFoundException::new);
			updateWithBacklogDetails(p.getGames(), backlog);
			mav.addObject("publisher", p);
		}
		else
		{
			mav.addObject("publisher", pubs.findById(pubId, u).orElseThrow(PublisherNotFoundException::new));
		}
		return mav;
	}
	
	@RequestMapping(value = "/publishers/{pubId}", method = RequestMethod.POST)
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/publishers/{pubId}");
	}
	
	@RequestMapping("/genres")
	public ModelAndView genresList()
	{
		final ModelAndView mav = new ModelAndView("allGenres");
		mav.addObject("genres", gens.getAllGenres());
		return mav;
	}
	
	@RequestMapping("/genres/{genreId}")
	public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("genre");
		User u = loggedUser();
		if(u == null)
		{
			Genre g = gens.findById(genreId).orElseThrow(GenreNotFoundException::new);
			updateWithBacklogDetails(g.getGames(), backlog);
			mav.addObject("genre", g);
		}
		else
		{
			mav.addObject("genre", gens.findById(genreId, u).orElseThrow(GenreNotFoundException::new));
		}
		return mav;
	}
	
	@RequestMapping(value = "/genres/{genreId}", method = RequestMethod.POST)
	public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/genres/{genreId}");
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
	
	@RequestMapping("/profile")
	public ModelAndView visitOwnProfile()
	{
		final ModelAndView mav = new ModelAndView("userProfile");
		mav.addObject("user", loggedUser());
		return mav;
	}
	
	// I think this should go in Service, but the Authentication dependencies are for webapp only. Ask about that
	// If used as a ModelAttribute, Authentication is null and throws exception, but works just fine. See if we can change the if for a try-catch or something
	@ModelAttribute("loggedUser")
	public User loggedUser()
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null)
		{
			final Optional<User> user = us.findByUsername((String) auth.getName());
			return user.orElseGet(() -> null);
		}
		return null;
	}
	
	@RequestMapping("/transfer_backlog")
	public ModelAndView transferBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		transferBacklog(response, backlog, loggedUser());
		clearAnonBacklog(response);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/clear_backlog")
	public ModelAndView clearBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		clearAnonBacklog(response);
		return new ModelAndView("redirect:/");
	}
	
///////////////////////////////////////////////////////////////////////
	
	private String toggleBacklog(long gameId, HttpServletResponse response, String backlog)
	{
		User u = loggedUser();
		if(u == null)
		{
			if(gameInBacklog(gameId, backlog))
				backlog = removeFromBacklog(gameId, backlog);
			else
				backlog = addToBacklog(gameId, backlog);
			updateBacklogCookie(response, backlog);
		}
		else
		{
			gs.toggleBacklog(gameId, u);
		}
		return backlog;
	}
		
	private void clearAnonBacklog(HttpServletResponse response)
	{
		updateBacklogCookie(response, "");
	}
	
	private void updateBacklogCookie(HttpServletResponse response, String backlog)
	{
		Cookie cookie = new Cookie("backlog", backlog);
		cookie.setPath("/");
		cookie.setMaxAge(600000);
		response.addCookie(cookie);
	}

	
	private List<Game> getGamesInBacklog(String backlog)
	{
		List<Game> list = new ArrayList<Game>();
		String[] ids = backlog.split("-");
		for(String id : ids)
		{
			if(!id.isEmpty())
			{
				Optional<Game> g = gs.findById(Long.parseLong(id), null);
				if(g.isPresent())
				{
					list.add(g.get());
					g.get().setInBacklog(true);
				}
			}
		}
		return list;
	}
	
	private String addToBacklog(long gameId, String backlog)
	{
		if(gameInBacklog(gameId, backlog))
			return backlog;
		return backlog +"-" +gameId +"-";
	}
	
	private String removeFromBacklog(long gameId, String backlog)
	{
		return backlog.replaceAll("-"+gameId+"-", "");
	}
	
	private void transferBacklog(HttpServletResponse response, String backlog, User u)
	{
		List<Game> anonGames = getGamesInBacklog(backlog);
		for(Game g : anonGames)
			gs.addToBacklog(g.getId(), u);
		clearAnonBacklog(response);
	}
	
	public List<Game> getUpcomingGames(String backlog)
	{
		List<Game> list = gs.getUpcomingGames(null);
		updateWithBacklogDetails(list, backlog);
		return list;
	}
	
	private void updateWithBacklogDetails(Collection<Game> games, String backlog)
	{
		for(Game g : games)
			g.setInBacklog(gameInBacklog(g.getId(), backlog));
	}
	
	private boolean gameInBacklog(long gameId, String backlog)
	{
		return backlog.contains("-" +gameId +"-");
	}
}