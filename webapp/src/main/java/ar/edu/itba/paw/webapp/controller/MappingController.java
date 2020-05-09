package ar.edu.itba.paw.webapp.controller;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

import ar.edu.itba.paw.interfaces.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Playstyle;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;

@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
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
	private RunService runs;
	
	@Autowired
	private ScoreService scors;

	@Autowired
	private BacklogCookieHandlerService backlogCookieHandlerService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MappingController.class);
	
	@RequestMapping("/")
	public ModelAndView index(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		Locale locale = LocaleContextHolder.getLocale();
		System.out.println(locale.toLanguageTag());
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("cookieBacklog", backlog);
		User u = us.getLoggedUser();
		if(u == null)
		{
			mav.addObject("backlogGames", backlogCookieHandlerService.getGamesInBacklog(backlog));
			mav.addObject("upcomingGames", getUpcomingGames(backlog));
			mav.addObject("popularGames", getPopularGames(backlog));
		}
		else
		{
			mav.addObject("backlogGames", gs.getGamesInBacklog());
			mav.addObject("recommendedGames", gs.getRecommendedGames());
			mav.addObject("popularGames", gs.getPopularGames());
			mav.addObject("upcomingGames", gs.getUpcomingGames());
		}
		return mav;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowIndex(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(@RequestParam String search, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		LOGGER.debug("Searching results for term {}.", search);
		final ModelAndView mav = new ModelAndView("gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		mav.addObject("searchTerm", search);

		if(u == null)
		{
			List<Game> searchResults = gs.searchByTitle(search);
			backlogCookieHandlerService.updateWithBacklogDetails(searchResults, backlog);
			mav.addObject("games", searchResults);
			if(searchResults.isEmpty())
				mav.addObject("popularGames", getPopularGames(backlog));
		}
		else
		{
			List<Game> searchResults = gs.searchByTitle(search);
			mav.addObject("games", searchResults);
			if(searchResults.isEmpty())
				mav.addObject("popularGames", gs.getPopularGames());
		}
		LOGGER.debug("Search results for {} successfully extracted.", search);
		return mav;
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndContinueSearch(@RequestParam String search, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		final ModelAndView mav = new ModelAndView("gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		if(u == null)
		{
			List<Game> searchResults = gs.searchByTitle(search);
			backlogCookieHandlerService.updateWithBacklogDetails(searchResults, backlog);
			mav.addObject("searchTerm", search);
			mav.addObject("games", searchResults);
			if(searchResults.isEmpty())
				mav.addObject("popularGames", getPopularGames(backlog));
		}
		else
		{
			mav.addObject("searchTerm", search);
			List<Game> searchResults = gs.searchByTitle(search);
			mav.addObject("games", searchResults);
			if(searchResults.isEmpty())
				mav.addObject("popularGames", gs.getPopularGames());
		}
		return mav;
	}
	

	@RequestMapping(value = "/searchFilter", method = RequestMethod.GET)
	public ModelAndView filteredSearch(@RequestParam("hoursRight") int hoursRight, @RequestParam("minsRight") int minsRight, @RequestParam("secsRight") int secsRight,
			@RequestParam("hoursLeft") int hoursLeft, @RequestParam("minsLeft") int minsLeft, @RequestParam("secsLeft") int secsLeft,
			@RequestParam("scoreRight") int scoreRight, @RequestParam("scoreLeft") int scoreLeft,
			@RequestParam(required = false, defaultValue = "", value = "platforms") List<String> platforms, 
			@RequestParam(required = false, defaultValue = "", value = "genres") List<String> genres, @RequestParam("searchTerm") String search,
			@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		LOGGER.debug("Searching results for term {}. Advanced filters include time between {}:{}:{} and {}:{}:{} and score between {} and {}", search, hoursLeft, minsLeft, secsLeft, hoursRight, minsRight, secsRight, scoreLeft, scoreRight);
		final ModelAndView mav = new ModelAndView("gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		int timeLeft = hoursLeft*3600 + minsLeft*60 + secsLeft;
		int timeRight = hoursRight*3600 + minsRight*60 + secsRight;
		
		if(u == null)
		{	
			List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, null);
			backlogCookieHandlerService.updateWithBacklogDetails(filteredResults, backlog);
			mav.addObject("searchTerm", search);
			mav.addObject("games", filteredResults);
			if(filteredResults.isEmpty())
				mav.addObject("popularGames", getPopularGames(backlog));
		}
		else
		{
			List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, u);
			mav.addObject("searchTerm", search);
			mav.addObject("games", filteredResults);
			if(filteredResults.isEmpty())
				mav.addObject("popularGames", gs.getPopularGames());
		}
		LOGGER.debug("Search results for {} with advanced filters successfully extracted.", search);
		return mav;
	}
	
	@RequestMapping(value = "/searchFilter", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndContinueFilteredSearch(@RequestParam("hoursRight") int hoursRight, @RequestParam("minsRight") int minsRight, @RequestParam("secsRight") int secsRight,
			@RequestParam("hoursLeft") int hoursLeft, @RequestParam("minsLeft") int minsLeft, @RequestParam("secsLeft") int secsLeft,
			@RequestParam("scoreRight") int scoreRight, @RequestParam("scoreLeft") int scoreLeft,
			@RequestParam(required = false, defaultValue = "", value = "platforms") List<String> platforms, 
			@RequestParam(required = false, defaultValue = "", value = "genres") List<String> genres, @RequestParam("searchTerm") String search,
			HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog, @RequestParam long gameId)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		final ModelAndView mav = new ModelAndView("gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		int timeLeft = hoursLeft*3600 + minsLeft*60 + secsLeft;
		int timeRight = hoursRight*3600 + minsRight*60 + secsRight;
		
		if(u == null)
		{	
			List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, null);
			backlogCookieHandlerService.updateWithBacklogDetails(filteredResults, backlog);
			mav.addObject("searchTerm", search);
			mav.addObject("games", filteredResults);
			if(filteredResults.isEmpty())
				mav.addObject("popularGames", getPopularGames(backlog));
		}
		else
		{
			List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, u);
			mav.addObject("searchTerm", search);
			mav.addObject("games", filteredResults);
			if(filteredResults.isEmpty())
				mav.addObject("popularGames", gs.getPopularGames());
		}
		return mav;
	}
	
	@RequestMapping("/backlog")
	public ModelAndView backlog(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("fullBacklog");
		User u = us.getLoggedUser();
		if(u == null)
		{
			mav.addObject("backlogGames", backlogCookieHandlerService.getGamesInBacklog(backlog));
		}
		else
		{
			mav.addObject("backlogGames", gs.getGamesInBacklog());
		}
		return mav;
	}
	
	@RequestMapping("/games")
	public ModelAndView gamesList(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("allGames");
		User u = us.getLoggedUser();
		if(u == null)
		{
			List<Game> games = gs.getAllGames();
			backlogCookieHandlerService.updateWithBacklogDetails(games, backlog);
			mav.addObject("games", games);
		}
		else
		{
			mav.addObject("games", gs.getAllGames());
		}
		return mav;
	}
	
	@RequestMapping(value = "/games/scores/{gameId}", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam("score") int scoreInput, @RequestParam("game") long gameId) 
	{
		User user = us.getLoggedUser();
		if(user == null)
			return new ModelAndView("redirect:/games/{gameId}");
		Game game = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
		Optional<Score> score = scors.findScore(user, game);
		LOGGER.debug("Registering score {} from user {} for game {}.", scoreInput, user.getUsername(), game.getTitle());
		if (score.isPresent())
			scors.changeScore(scoreInput, user, game);
		else
			scors.register(user, game, scoreInput);
		LOGGER.debug("{}'s score for game {} successfully registered.", user.getUsername(), game.getTitle());
		return new ModelAndView("redirect:/games/{gameId}");
	}
	
	@RequestMapping(value = "/createRun/run/{gameId}", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam("hours") int hours, @RequestParam("mins") int mins, @RequestParam("secs") int secs,
			@RequestParam("game") long gameId, @RequestParam("platforms") String platform, @RequestParam("playstyles") String playst) 
	{
		User user = us.getLoggedUser();
		if(user == null)
			return new ModelAndView("redirect:/games/{gameId}");
		Optional <Game> game = gs.findByIdWithDetails(gameId);
		Optional <Platform> plat = ps.findByName(platform);
		long time = hours*3600 + mins*60 + secs;
		Optional <Playstyle> play = runs.findPlaystyleByName(playst);
		if(game.isPresent() && plat.isPresent() && play.isPresent())
		{
			LOGGER.debug("Registering run of {} seconds in style {} from user {} for game {} on platform {}.", time, play.get().getName(), user.getUsername(), game.get().getTitle(), plat.get().getShortName());
			runs.register(user, game.get(), plat.get(), play.get(), time);
			LOGGER.debug("Registration of run of {} by user {} successful.", game.get().getTitle(), user.getUsername());
		}
		return new ModelAndView("redirect:/games/{gameId}");
	}	
	
	@RequestMapping("/createRun/{gameId}")
	public ModelAndView createRun(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		User u = us.getLoggedUser();
		if(u == null)
			return new ModelAndView("redirect:/games/{gameId}");
		final ModelAndView mav = new ModelAndView("createRun");
		Game g = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
		mav.addObject("game", g);
		mav.addObject("playstyles",runs.getAllPlaystyles());
		return mav;
	}
	
	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public ModelAndView gamesList(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/games");
	}
	
	@RequestMapping("/games/{gameId}")
	public ModelAndView gameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("game");
		User u = us.getLoggedUser();
		Game g = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
		mav.addObject("playAverage", runs.getAverageAllPlayStyles(g));
		mav.addObject("averageScore", scors.findAverageScore(g));
		if(u == null)
		{	
			g.setInBacklog(backlogCookieHandlerService.gameInBacklog(gameId, backlog));
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
			mav.addObject("user_runs", runs.findGameRuns(g, u));
		}
		return mav;
	}

	@RequestMapping(value = "/games/{gameId}", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndShowGameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/games/{gameId}");
	}
	

	@RequestMapping("/platforms")
	public ModelAndView platformsList()
	{
		final ModelAndView mav = new ModelAndView("allPlatforms");
		List<Platform> list = ps.getAllPlatforms();
		mav.addObject("platforms", list);
		mav.addObject("listSize", list.size());
		return mav;
	}
	
	@RequestMapping("/platforms/{platformId}")
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("platform");
		User u = us.getLoggedUser();
		Platform p = ps.findById(platformId).orElseThrow(PlatformNotFoundException::new);
		if(u == null)
			backlogCookieHandlerService.updateWithBacklogDetails(p.getGames(), backlog);
		mav.addObject("platform", p);
		return mav;
	}
	
	@RequestMapping(value = "/platforms/{platformId}", method = RequestMethod.POST)
	public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/platforms/{platformId}");
	}
	
	@RequestMapping("/developers")
	public ModelAndView developersList()
	{
		final ModelAndView mav = new ModelAndView("allDevelopers");
		List<Developer> list = ds.getAllDevelopers();
		mav.addObject("developers", list);
		mav.addObject("listSize", list.size());
		return mav;
	}
	
	@RequestMapping("/developers/{devId}")
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("developer");
		Developer d = ds.findById(devId).orElseThrow(DeveloperNotFoundException::new);
		User u = us.getLoggedUser();
		if(u == null)
			backlogCookieHandlerService.updateWithBacklogDetails(d.getGames(), backlog);
		mav.addObject("developer", d);
		return mav;
	}
	
	@RequestMapping(value = "/developers/{devId}", method = RequestMethod.POST)
	public ModelAndView developerProfile(@PathVariable("devId") long devId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/developers/{devId}");
	}
	
	@RequestMapping("/publishers")
	public ModelAndView publishersList()
	{
		final ModelAndView mav = new ModelAndView("allPublishers");
		List<Publisher> list = pubs.getAllPublishers();
		mav.addObject("publishers", list);
		mav.addObject("listSize", list.size());
		return mav;
	}
	
	@RequestMapping("/publishers/{pubId}")
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("publisher");
		User u = us.getLoggedUser();
		Publisher p = pubs.findById(pubId).orElseThrow(PublisherNotFoundException::new);
		if(u == null)
			backlogCookieHandlerService.updateWithBacklogDetails(p.getGames(), backlog);
		mav.addObject("publisher", p);
		return mav;
	}
	
	@RequestMapping(value = "/publishers/{pubId}", method = RequestMethod.POST)
	public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
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
		User u = us.getLoggedUser();
		Genre g = gens.findById(genreId).orElseThrow(GenreNotFoundException::new);
		if(u == null)
			backlogCookieHandlerService.updateWithBacklogDetails(g.getGames(), backlog);
		mav.addObject("genre", g);
		return mav;
	}
	
	@RequestMapping(value = "/genres/{genreId}", method = RequestMethod.POST)
	public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
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
	@RequestMapping("/transfer_backlog")
	public ModelAndView transferBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlogCookieHandlerService.transferBacklog(response, backlog, us.getLoggedUser());
		backlogCookieHandlerService.clearAnonBacklog(response);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/clear_backlog")
	public ModelAndView clearBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlogCookieHandlerService.clearAnonBacklog(response);
		return new ModelAndView("redirect:/");
	}
	
///////////////////////////////////////////////////////////////////////

	public List<Game> getUpcomingGames(String backlog)
	{
		List<Game> list = gs.getUpcomingGames();
		backlogCookieHandlerService.updateWithBacklogDetails(list, backlog);
		return list;
	}

	public List<Game> getPopularGames(String backlog)
	{
		List<Game> list = gs.getPopularGames();
		backlogCookieHandlerService.updateWithBacklogDetails(list, backlog);
		return list;
	}
	

}