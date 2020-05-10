package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/backlog")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class BacklogController {

    @Autowired
    private UserService                 us;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

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
