package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Genre;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/genres")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class GenreController {

    @Autowired
    private GenreService                gens;

    @Autowired
    private UserService                 us;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

    @RequestMapping("")
    public ModelAndView genresList()
    {
        final ModelAndView mav = new ModelAndView("allGenres");
        mav.addObject("genres", gens.getAllGenres());
        return mav;
    }

    @RequestMapping("/genre={genreId}&page={page}")
    public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @PathVariable("page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {	
    	int pageSize = 15;
        final ModelAndView mav = new ModelAndView("genre");
        User u = us.getLoggedUser();
        Genre g = gens.findById(genreId).orElseThrow(GenreNotFoundException::new);
        int countResults = gens.countGames(g);
		int totalPages = (countResults + pageSize - 1)/pageSize; 
        List<Game> games = gens.getAllGamesPaged(g, page, pageSize);
       
        if(u == null)
            backlogCookieHandlerService.updateWithBacklogDetails(games, backlog);
        mav.addObject("genre", g);
        mav.addObject("games",games);
        mav.addObject("pages", totalPages);
        mav.addObject("current", page);
        return mav;
    }

    @RequestMapping(value = "/{genreId}", method = RequestMethod.POST)
    public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/genres/{genreId}");
    }
}
