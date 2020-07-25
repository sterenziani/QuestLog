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
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Publisher;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.PublisherDto;
import ar.edu.itba.paw.webapp.exception.PublisherNotFoundException;

@Path("/publishers")
@Component
public class PublisherController {

	@Context
	private UriInfo uriInfo;
	
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

	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listPublishers(@QueryParam("page") @DefaultValue("1") int page)
	{
		final List<PublisherDto> publishers = pubs.getPublishers(page, PUB_LIST_PAGE_SIZE).stream().map(p -> PublisherDto.fromPublisher(p, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + pubs.countPublishers()/PUB_LIST_PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<PublisherDto>>(publishers) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("/{publisherId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGenreById(@PathParam("publisherId") long publisherId)
	{
		final Optional<Publisher> maybePublisher = pubs.findById(publisherId);
		if(!maybePublisher.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		return Response.ok(maybePublisher.map(p -> PublisherDto.fromPublisher(p, uriInfo)).get()).build();
	}
	
	@GET
	@Path("/{publisherId}/games")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGamesByGenre(@PathParam("publisherId") long publisherId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<Publisher> maybePublisher = pubs.findById(publisherId);
		if(!maybePublisher.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GameDto> games = gs.getGamesForPublisher(maybePublisher.get(), page, PAGE_SIZE).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + gs.countGamesForPublisher(maybePublisher.get())/PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(games) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
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
