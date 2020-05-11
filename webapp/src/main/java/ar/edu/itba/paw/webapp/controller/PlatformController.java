package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/platforms")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class PlatformController {

    @Autowired
    private PlatformService ps;

    @Autowired
    private UserService us;
    
    @Autowired
    private GameService gs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;
    
    private static final int PAGE_SIZE = 15;

    @RequestMapping("")
    public ModelAndView platformsList()
    {
        final ModelAndView mav = new ModelAndView("allPlatforms");
        List<Platform> list = ps.getAllPlatforms();
        mav.addObject("platforms", list);
        mav.addObject("listSize", list.size());
        return mav;
    }

    @RequestMapping("/{platformId}")
    public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("platform");
        User u = us.getLoggedUser();
        Platform p = ps.findById(platformId).orElseThrow(PlatformNotFoundException::new);
        List<Game> pageResults = gs.getGamesForPlatform(p, page, PAGE_SIZE);
    	int countResults = gs.countGamesForPlatform(p);
		int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE;
        if(u == null)
            backlogCookieHandlerService.updateWithBacklogDetails(pageResults, backlog);
        mav.addObject("platform", p);
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("gamesInPage", pageResults);
        return mav;
    }

    @RequestMapping(value = "/{platformId}", method = RequestMethod.POST)
    public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam long gameId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/platforms/{platformId}?page="+page);
    }
}
