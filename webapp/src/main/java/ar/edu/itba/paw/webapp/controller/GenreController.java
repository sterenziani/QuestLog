package ar.edu.itba.paw.webapp.controller;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Genre;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GenreDto;
import ar.edu.itba.paw.webapp.exception.GenreNotFoundException;

@Path("/genres")
@Component
public class GenreController {

	@Context
	private UriInfo uriInfo;
	
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

	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listGenres(@QueryParam("page") @DefaultValue("1") int page)
	{
		final List<GenreDto> genres = gens.getGenres(page, GENRE_LIST_PAGE_SIZE).stream().map(g -> GenreDto.fromGenre(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + gens.countGenres()/GENRE_LIST_PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GenreDto>>(genres) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("/{genreId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGenreById(@PathParam("genreId") long genreId)
	{
		final Optional<Genre> maybeGenre = gens.findById(genreId);
		if(!maybeGenre.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		return Response.ok(maybeGenre.map(g -> GenreDto.fromGenre(g, uriInfo)).get()).build();
	}
	
	@GET
	@Path("/{genreId}/games")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGamesByGenre(@PathParam("genreId") long genreId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<Genre> maybeGenre = gens.findById(genreId);
		if(!maybeGenre.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GameDto> games = gs.getGamesForGenre(maybeGenre.get(), page, PAGE_SIZE).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + gs.countGamesForGenre(maybeGenre.get())/PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(games) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
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
