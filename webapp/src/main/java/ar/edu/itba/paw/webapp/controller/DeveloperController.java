package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Developer;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/developers")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class DeveloperController {

    @Autowired
    private DeveloperService ds;

    @Autowired
    private UserService us;

    @Autowired
    private GameService gs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;
    
    private static final int DEV_LIST_PAGE_SIZE = 30;
    private static final int PAGE_SIZE = 15;

    @RequestMapping("")
    public ModelAndView developersList(@RequestParam(required = false, defaultValue = "1", value = "page") int page)
    {
        final ModelAndView mav = new ModelAndView("explore/allDevelopers");
        List<Developer> list = ds.getDevelopers(page, DEV_LIST_PAGE_SIZE);
        int countResults = ds.countDevelopers();
        int totalPages = (countResults + DEV_LIST_PAGE_SIZE - 1)/DEV_LIST_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
        mav.addObject("developers", list);
        mav.addObject("listSize", list.size());
        return mav;
    }

    @RequestMapping("/{devId}")
    public ModelAndView developerProfile(@PathVariable("devId") long devId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("explore/developer");
        Developer d = ds.findById(devId).orElseThrow(DeveloperNotFoundException::new);
        User u = us.getLoggedUser();
        List<Game> pageResults = gs.getGamesForDeveloper(d, page, PAGE_SIZE);
    	int countResults = gs.countGamesForDeveloper(d);
		int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE;
        if(u == null)
            backlogCookieHandlerService.updateWithBacklogDetails(pageResults, backlog);
        mav.addObject("developer", d);
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("gamesInPage", pageResults);
        return mav;
    }

    @RequestMapping(value = "/{devId}", method = RequestMethod.POST)
    public ModelAndView developerProfile(@PathVariable("devId") long devId, @RequestParam long gameId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/developers/{devId}?page="+page);
    }
}
