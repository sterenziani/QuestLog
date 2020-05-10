package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Developer;
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
    private DeveloperService            ds;

    @Autowired
    private UserService                 us;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

    @RequestMapping("")
    public ModelAndView developersList()
    {
        final ModelAndView mav = new ModelAndView("allDevelopers");
        List<Developer> list = ds.getAllDevelopers();
        mav.addObject("developers", list);
        mav.addObject("listSize", list.size());
        return mav;
    }

    @RequestMapping("/{devId}")
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

    @RequestMapping(value = "/{devId}", method = RequestMethod.POST)
    public ModelAndView developerProfile(@PathVariable("devId") long devId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/developers/{devId}");
    }
}
