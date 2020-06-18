package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Genre;
import ar.edu.itba.paw.model.entity.User;
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
    private GameService                 gs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;
    
    private static final int GENRE_LIST_PAGE_SIZE = 25;
    private static final int PAGE_SIZE = 15;


    @RequestMapping("")
    public ModelAndView genresList(@RequestParam(required = false, defaultValue = "1", value = "page") int page)
    {
        final ModelAndView mav = new ModelAndView("explore/allGenres");
        List<Genre> list = gens.getGenres(page, GENRE_LIST_PAGE_SIZE);
        int countResults = gens.countGenres();
        int totalPages = (countResults + GENRE_LIST_PAGE_SIZE - 1)/GENRE_LIST_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
        mav.addObject("genres", list);
        mav.addObject("listSize", list.size());
        return mav;
    }

    @RequestMapping("/{genreId}")
    public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @RequestParam(required = false, defaultValue = "1", value = "page") int page,
    		@CookieValue(value="backlog", defaultValue="") String backlog)
    {	
        final ModelAndView mav = new ModelAndView("explore/genre");
        User u = us.getLoggedUser();
        Genre g = gens.findById(genreId).orElseThrow(GenreNotFoundException::new);
        int countResults = gs.countGamesForGenre(g);
		int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE; 
        List<Game> games = gs.getGamesForGenre(g, page, PAGE_SIZE);
       
        if(u == null)
            backlogCookieHandlerService.updateWithBacklogDetails(games, backlog);

        mav.addObject("genre", g);
        mav.addObject("gamesInPage",games);
        mav.addObject("pages", totalPages);
        mav.addObject("current", page);
        mav.addObject("listIcon", g.getLogo());
        return mav;
    }

    @RequestMapping(value = "/{genreId}", method = RequestMethod.POST)
    public ModelAndView genreProfile(@PathVariable("genreId") long genreId, @RequestParam long gameId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/genres/{genreId}?page="+page);
    }
}
