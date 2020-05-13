package ar.edu.itba.paw.webapp.controller;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

@RequestMapping("/backlog")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class BacklogController {

    @Autowired
    private UserService us;
    
    @Autowired
    private GameService gs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;
    
    private static final int PAGE_SIZE = 15;

    @RequestMapping("")
    public ModelAndView backlog(@RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("user/fullBacklog");
        User u = us.getLoggedUser();
        if(u == null)
        {
            mav.addObject("gamesInPage", backlogCookieHandlerService.getGamesInBacklog(backlog, page, PAGE_SIZE));
            int countResults = backlogCookieHandlerService.countGamesInBacklog(backlog);
            int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE;
    		mav.addObject("pages", totalPages);
    		mav.addObject("current", page);
        }
        else
        {
            mav.addObject("gamesInPage", gs.getGamesInBacklog(page, PAGE_SIZE));
            int countResults = gs.countGamesInBacklog();
            int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE;
    		mav.addObject("pages", totalPages);
    		mav.addObject("current", page);
        }
        return mav;
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ModelAndView backlog(@RequestParam long gameId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/backlog/?page="+page);
    }
    
    @RequestMapping("/{userId}")
    public ModelAndView backlog(@PathVariable("userId") long userId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("user/fullBacklog");
        User visitedUser = us.findById(userId).orElseThrow(UserNotFoundException::new);
        User u = us.getLoggedUser();
        List<Game> gamesInPage = gs.getGamesInBacklog(visitedUser, page, PAGE_SIZE);
        if(u == null)
        {
        	backlogCookieHandlerService.updateWithBacklogDetails(gamesInPage, backlog);
        }
        mav.addObject("gamesInPage", gamesInPage);
        int countResults = gs.countGamesInBacklog(visitedUser);
        int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("visitedUser", visitedUser);
        return mav;
    }
    
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public ModelAndView backlog(@PathVariable("userId") long userId, @RequestParam long gameId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/backlog/{userId}?page="+page);
    }
    
    @RequestMapping("/transfer")
    public ModelAndView transferBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlogCookieHandlerService.transferBacklog(response, backlog, us.getLoggedUser());
        backlogCookieHandlerService.clearAnonBacklog(response);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/clear")
    public ModelAndView clearBacklog(HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlogCookieHandlerService.clearAnonBacklog(response);
        return new ModelAndView("redirect:/");
    }
}
