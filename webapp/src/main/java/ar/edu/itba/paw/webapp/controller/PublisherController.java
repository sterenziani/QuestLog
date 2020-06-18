package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Publisher;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/publishers")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class PublisherController {

    @Autowired
    private PublisherService            pubs;

    @Autowired
    private UserService                 us;

    @Autowired
    private GameService gs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;
    
    private static final int PUB_LIST_PAGE_SIZE = 30;
    private static final int PAGE_SIZE = 15;

    @RequestMapping("")
    public ModelAndView publishersList(@RequestParam(required = false, defaultValue = "1", value = "page") int page)
    {
        final ModelAndView mav = new ModelAndView("explore/allPublishers");
        List<Publisher> list = pubs.getPublishers(page, PUB_LIST_PAGE_SIZE);
        int countResults = pubs.countPublishers();
        int totalPages = (countResults + PUB_LIST_PAGE_SIZE - 1)/PUB_LIST_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
        mav.addObject("publishers", list);
        mav.addObject("listSize", list.size());
        return mav;
    }

    @RequestMapping("/{pubId}")
    public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("explore/publisher");
        User u = us.getLoggedUser();
        Publisher p = pubs.findById(pubId).orElseThrow(PublisherNotFoundException::new);
        List<Game> pageResults = gs.getGamesForPublisher(p, page, PAGE_SIZE);
    	int countResults = gs.countGamesForPublisher(p);
		int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE;
        if(u == null)
           backlogCookieHandlerService.updateWithBacklogDetails(pageResults, backlog);
        mav.addObject("publisher", p);
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("gamesInPage", pageResults);
        return mav;
    }

    @RequestMapping(value = "/{pubId}", method = RequestMethod.POST)
    public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @RequestParam long gameId, HttpServletResponse response, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/publishers/{pubId}?page="+page);
    }
}
