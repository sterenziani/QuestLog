package ar.edu.itba.paw.webapp.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.DeveloperService;
import ar.edu.itba.paw.interfaces.GameService;
import ar.edu.itba.paw.interfaces.GenreService;
import ar.edu.itba.paw.interfaces.PlatformService;
import ar.edu.itba.paw.interfaces.PublisherService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

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
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchUser()
	{
		return new ModelAndView("404");
	}
	
	@ExceptionHandler(GameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchGame()
	{
		return new ModelAndView("404");
	}
	
	@ExceptionHandler(PlatformNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchPlatform()
	{
		return new ModelAndView("404");
	}
	
	@ExceptionHandler(DeveloperNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchDeveloper()
	{
		return new ModelAndView("404");
	}
	
	@ExceptionHandler(GenreNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchGenre()
	{
		return new ModelAndView("404");
	}
	
	@ExceptionHandler(PublisherNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView NotSuchPublisher()
	{
		return new ModelAndView("404");
	}
	
	@RequestMapping("/")
	public ModelAndView helloWorld(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		/* To be used for final version of the view
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("backlog", backlog);
		*/
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("games", gs.getAllGamesSimplified());
		mav.addObject("backlogGames", getBacklog(backlog));
		mav.addObject("upcomingGames", gs.getUpcomingGamesSimplified());
		return mav;
	}
	
	@RequestMapping("/search")
	public ModelAndView search(@RequestParam String search)
	{
		final ModelAndView mav = new ModelAndView("index");
		mav.addObject("games", gs.searchByTitleSimplified(search));
		return mav;
	}
	
	@RequestMapping("/{id}")
	public ModelAndView userProfile(@PathVariable("id") long id)
	{
		final ModelAndView mav = new ModelAndView("user");
		mav.addObject("user", us.findById(id).orElseThrow(UserNotFoundException::new));
		return mav;
	}
	
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public ModelAndView register(@RequestParam(value = "username", required = true) final String username) 
	{
		final User user = us.register(username);
		return new ModelAndView("redirect:/" + user.getId());
	}
	
	@RequestMapping("/games")
	public ModelAndView gamesList()
	{
		final ModelAndView mav = new ModelAndView("gamesList");
		mav.addObject("games", gs.getAllGames());
		return mav;
	}
	
	@RequestMapping("/games/{id}")
	public ModelAndView gameProfile(@PathVariable("id") long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("game");
		mav.addObject("game", gs.findById(id).orElseThrow(GameNotFoundException::new));
		
		if(gameNotInBacklog(id, backlog))
			addToBacklog(id, response, backlog);
		else
		{
			removeFromBacklog(id, response, backlog);
		}
		return mav;
	}
	
	@RequestMapping("/platforms")
	public ModelAndView platformsList()
	{
		final ModelAndView mav = new ModelAndView("platformsList");
		mav.addObject("platforms", ps.getAllPlatforms());
		return mav;
	}
	
	@RequestMapping("/platforms/{id}")
	public ModelAndView platformProfile(@PathVariable("id") long id)
	{
		final ModelAndView mav = new ModelAndView("platform");
		mav.addObject("platform", ps.findById(id).orElseThrow(PlatformNotFoundException::new));
		return mav;
	}
	
	@RequestMapping("/developers")
	public ModelAndView developersList()
	{
		final ModelAndView mav = new ModelAndView("developersList");
		mav.addObject("developers", ds.getAllDevelopers());
		return mav;
	}
	
	@RequestMapping("/developers/{id}")
	public ModelAndView developerProfile(@PathVariable("id") long id)
	{
		final ModelAndView mav = new ModelAndView("developer");
		mav.addObject("developer", ds.findById(id).orElseThrow(DeveloperNotFoundException::new));
		return mav;
	}
	
	@RequestMapping("/publishers")
	public ModelAndView publishersList()
	{
		final ModelAndView mav = new ModelAndView("publishersList");
		mav.addObject("publishers", pubs.getAllPublishers());
		return mav;
	}
	
	@RequestMapping("/publishers/{id}")
	public ModelAndView publisherProfile(@PathVariable("id") long id)
	{
		final ModelAndView mav = new ModelAndView("publisher");
		mav.addObject("publisher", pubs.findById(id).orElseThrow(PublisherNotFoundException::new));
		return mav;
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
	
	private boolean gameNotInBacklog(long id, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		return !(backlog.contains("-" +id +"-"));
	}
	
	
	private List<Game> getBacklog(@CookieValue(value="backlog", defaultValue="") String backlog)
	{
		List<Game> list = new ArrayList<Game>();
		String[] ids = backlog.split("-");
		for(String id : ids)
		{
			if(!id.isEmpty())
			{
				Optional<Game> g = gs.findById(Long.parseLong(id));
				if(g.isPresent())
					list.add(g.get());
			}
		}
		return list;
	}
	
	private void addToBacklog(long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		if(gameNotInBacklog(id, backlog))
		{
			Cookie cookie = new Cookie("backlog", backlog +"-" +id +"-");
			cookie.setPath("/");
			response.addCookie(cookie);	
		}
	}
	
	private void removeFromBacklog(long id, HttpServletResponse response, @CookieValue(value="backlog", defaultValue=" ") String backlog)
	{
		if(!gameNotInBacklog(id, backlog))
		{
			String newBacklog = backlog.replaceAll("-"+id+"-", "");
			Cookie cookie = new Cookie("backlog", newBacklog);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}
}