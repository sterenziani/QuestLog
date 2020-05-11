package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Publisher;
import ar.edu.itba.paw.model.User;
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
    private BacklogCookieHandlerService backlogCookieHandlerService;

    @RequestMapping("")
    public ModelAndView publishersList()
    {
        final ModelAndView mav = new ModelAndView("allPublishers");
        List<Publisher> list = pubs.getAllPublishers();
        mav.addObject("publishers", list);
        mav.addObject("listSize", list.size());
        return mav;
    }

    @RequestMapping("/{pubId}")
    public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("publisher");
        User u = us.getLoggedUser();
        Publisher p = pubs.findById(pubId).orElseThrow(PublisherNotFoundException::new);
        if(u == null)
           // backlogCookieHandlerService.updateWithBacklogDetails(p.getGames(), backlog);
        mav.addObject("publisher", p);
        return mav;
    }

    @RequestMapping(value = "/{pubId}", method = RequestMethod.POST)
    public ModelAndView publisherProfile(@PathVariable("pubId") long pubId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/publishers/{pubId}");
    }
}
