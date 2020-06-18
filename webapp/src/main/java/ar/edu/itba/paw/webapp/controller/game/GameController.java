package ar.edu.itba.paw.webapp.controller.game;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.User;

@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class GameController {

    @Autowired
    private GameService                 gs;

    @Autowired
    private UserService                 us;

    @Autowired
    private PlatformService             ps;

    @Autowired
    private GenreService                gens;

    @Autowired
    private DeveloperService            ds;

    @Autowired
    private PublisherService            pubs;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private static final int HOME_PAGE_BACKLOG_SIZE = 10;
    private static final int PAGE_SIZE = 15;
    private static final int GENRES_IN_EXPLORE_PAGE = 25;
    private static final int PLATFORMS_IN_EXPLORE_PAGE = 18;
    private static final int DEVS_IN_EXPLORE_PAGE = 15;
    private static final int PUBLISHERS_IN_EXPLORE_PAGE = 15;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

    @RequestMapping("/")
    public ModelAndView index(@CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("index/index");
        mav.addObject("cookieBacklog", backlog);
        User u = us.getLoggedUser();
        if(u == null)
        {
            mav.addObject("backlogGames", backlogCookieHandlerService.getGamesInBacklog(backlog, 1, HOME_PAGE_BACKLOG_SIZE));
            mav.addObject("backlogCropped", backlogCookieHandlerService.countGamesInBacklog(backlog) > HOME_PAGE_BACKLOG_SIZE);
            mav.addObject("upcomingGames", getUpcomingGames(backlog));
            mav.addObject("popularGames", getPopularGames(backlog));
        }
        else
        {
            mav.addObject("backlogGames", gs.getGamesInBacklog(1, HOME_PAGE_BACKLOG_SIZE));
            mav.addObject("backlogCropped", gs.countGamesInBacklog() > HOME_PAGE_BACKLOG_SIZE);
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

    @RequestMapping("/explore")
    public ModelAndView explore()
    {
        final ModelAndView mav = new ModelAndView("explore/explore");
        mav.addObject("platforms", ps.getBiggestPlatforms(PLATFORMS_IN_EXPLORE_PAGE));
        mav.addObject("platformsCropped", ps.countPlatformsWithGames() > PLATFORMS_IN_EXPLORE_PAGE);
        mav.addObject("developers", ds.getBiggestDevelopers(DEVS_IN_EXPLORE_PAGE));
        mav.addObject("developersCropped", pubs.countPublishers() > DEVS_IN_EXPLORE_PAGE);
        mav.addObject("publishers", pubs.getBiggestPublishers(PUBLISHERS_IN_EXPLORE_PAGE));
        mav.addObject("publishersCropped", pubs.countPublishers() > PUBLISHERS_IN_EXPLORE_PAGE);
        mav.addObject("genres", gens.getGenres(1, GENRES_IN_EXPLORE_PAGE));
        mav.addObject("genresCropped", gens.countGenres() > GENRES_IN_EXPLORE_PAGE);
        return mav;
    }

	

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView filteredSearch(@RequestParam(required = false, defaultValue = "9999", value ="hoursRight") int hoursRight, @RequestParam(required = false, defaultValue = "59", value ="minsRight") int minsRight, 
			@RequestParam(required = false, defaultValue = "59", value ="secsRight") int secsRight,
			@RequestParam(required = false, defaultValue = "0", value ="hoursLeft") int hoursLeft, @RequestParam(required = false, defaultValue = "0", value ="minsLeft") int minsLeft,
			@RequestParam(required = false, defaultValue = "0", value ="secsLeft") int secsLeft,
			@RequestParam(required = false, defaultValue = "100", value ="scoreRight") int scoreRight, @RequestParam(required = false, defaultValue = "0", value ="scoreLeft") int scoreLeft,
			@RequestParam(required = false, defaultValue = "", value = "platforms") List<String> platforms, 
			@RequestParam(required = false, defaultValue = "", value = "genres") List<String> genres, @RequestParam String search,
			@CookieValue(value="backlog", defaultValue="") String backlog, @RequestParam int page)
	{
        LOGGER.debug("Searching results for term {}. Advanced filters include time between {}:{}:{} and {}:{}:{} and score between {} and {}", search, hoursLeft, minsLeft, secsLeft, hoursRight, minsRight, secsRight, scoreLeft, scoreRight);
		final ModelAndView mav = new ModelAndView("search/gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		int timeLeft = hoursLeft*3600 + minsLeft*60 + secsLeft;
		int timeRight = hoursRight*3600 + minsRight*60 + secsRight;
		int countResults = gs.countSearchResultsFiltered(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight);
		int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE; 
		mav.addObject("pages",totalPages);
		mav.addObject("current",page);
		
		mav.addObject("hoursLeft", hoursLeft);
		mav.addObject("minsLeft", minsLeft);
		mav.addObject("secsLeft", secsLeft);
		mav.addObject("hoursRight", hoursRight);
		mav.addObject("minsRight", minsRight);
		mav.addObject("secsRight", secsRight);
		mav.addObject("scoreLeft", scoreLeft);
		mav.addObject("scoreRight", scoreRight);
		mav.addObject("currentPlats", String.join(", ", platforms));
		mav.addObject("currentGens", String.join(", ", genres));
		mav.addObject("searchTerm", search);		
		if(u == null)
		{	
            if(countResults == 0) {
                mav.addObject("popularGames", getPopularGames(backlog));
                mav.addObject("games", new ArrayList<Game>());
                return mav;
            }
			List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, PAGE_SIZE);
            if(countResults == 1){
                return new ModelAndView("redirect:/games/" + filteredResults.stream().findFirst().get().getId());
            }
            mav.addObject("amountResults", countResults);
			backlogCookieHandlerService.updateWithBacklogDetails(filteredResults, backlog);
			mav.addObject("games", filteredResults);

		}
		else
		{	
            if(countResults == 0) {
                mav.addObject("popularGames", gs.getPopularGames());
                mav.addObject("games", new ArrayList<Game>());
                return mav;
            }
			List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, PAGE_SIZE);
            if(countResults == 1){
                return new ModelAndView("redirect:/games/" + filteredResults.stream().findFirst().get().getId());
            }
            mav.addObject("amountResults", countResults);
			mav.addObject("games", filteredResults);
		}
        LOGGER.debug("Search results for {} with advanced filters successfully extracted.", search);
		return mav;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndContinueFilteredSearch(@RequestParam(required = false, defaultValue = "9999", value ="hoursRight") int hoursRight, @RequestParam(required = false, defaultValue = "59", value ="minsRight") int minsRight, 
			@RequestParam(required = false, defaultValue = "59", value ="secsRight") int secsRight,
			@RequestParam(required = false, defaultValue = "0", value ="hoursLeft") int hoursLeft, @RequestParam(required = false, defaultValue = "0", value ="minsLeft") int minsLeft, 
			@RequestParam(required = false, defaultValue = "0", value ="secsLeft") int secsLeft,
			@RequestParam(required = false, defaultValue = "100", value ="scoreRight") int scoreRight, @RequestParam(required = false, defaultValue = "0", value ="scoreLeft") int scoreLeft,
			@RequestParam(required = false, defaultValue = "", value = "platforms") List<String> platforms, 
			@RequestParam(required = false, defaultValue = "", value = "genres") List<String> genres, @RequestParam String search,
			HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog, @RequestParam long gameId, @RequestParam int page)
	{
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		final ModelAndView mav = new ModelAndView("search/gameSearch");
		mav.addObject("platforms", ps.getAllPlatforms());
		mav.addObject("genres", gens.getAllGenres());
		User u = us.getLoggedUser();
		int timeLeft = hoursLeft*3600 + minsLeft*60 + secsLeft;
		int timeRight = hoursRight*3600 + minsRight*60 + secsRight;
		int countResults = gs.countSearchResultsFiltered(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight);
		int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE; 
		mav.addObject("pages",totalPages);
		mav.addObject("current",page);
		
		mav.addObject("hoursLeft", hoursLeft);
		mav.addObject("minsLeft", minsLeft);
		mav.addObject("secsLeft", secsLeft);
		mav.addObject("hoursRight", hoursRight);
		mav.addObject("minsRight", minsRight);
		mav.addObject("secsRight", secsRight);
		mav.addObject("scoreLeft", scoreLeft);
		mav.addObject("scoreRight", scoreRight);
		mav.addObject("currentPlats", String.join(", ", platforms));
		mav.addObject("currentGens", String.join(", ", genres));
		mav.addObject("searchTerm", search);
		
		if(u == null)
		{	
            if(countResults == 0) {
                mav.addObject("popularGames", getPopularGames(backlog));
                mav.addObject("games", new ArrayList<Game>());
                return mav;
            }
    		List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, PAGE_SIZE);
            if(countResults == 1){
                return new ModelAndView("redirect:/games/" + filteredResults.stream().findFirst().get().getId());
            }
            mav.addObject("amountResults", countResults);
			backlogCookieHandlerService.updateWithBacklogDetails(filteredResults, backlog);
			mav.addObject("games", filteredResults);
            
		}
		else
		{
            if(countResults == 0) {
                mav.addObject("popularGames", gs.getPopularGames());
                mav.addObject("games", new ArrayList<Game>());
                return mav;
            }
    		List<Game> filteredResults = gs.getFilteredGames(search, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, PAGE_SIZE);
            if(countResults == 1){
                return new ModelAndView("redirect:/games/" + filteredResults.stream().findFirst().get().getId());
            }
            mav.addObject("amountResults", countResults);
			mav.addObject("games", filteredResults);
		}
		return mav;
	}

    private List<Game> getUpcomingGames(String backlog)
    {
        List<Game> list = gs.getUpcomingGames();
        backlogCookieHandlerService.updateWithBacklogDetails(list, backlog);
        return list;
    }

    private List<Game> getPopularGames(String backlog)
    {
        List<Game> list = gs.getPopularGames();
        backlogCookieHandlerService.updateWithBacklogDetails(list, backlog);
        return list;
    }
}
