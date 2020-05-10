package ar.edu.itba.paw.webapp.controller.game;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

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

    private static final Logger         LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

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

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ModelAndView gamesList(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/games");
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
