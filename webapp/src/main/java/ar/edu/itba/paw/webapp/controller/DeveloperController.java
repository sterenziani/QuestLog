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
import ar.edu.itba.paw.interfaces.service.DeveloperService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Developer;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.dto.DeveloperDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.exception.DeveloperNotFoundException;

@Path("/developers")
@Component
public class DeveloperController {
	
	@Context
	private UriInfo uriInfo;
	
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

	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listDevelopers(@QueryParam("page") @DefaultValue("1") int page)
	{
		final List<DeveloperDto> devs = ds.getDevelopers(page, DEV_LIST_PAGE_SIZE).stream().map(u -> DeveloperDto.fromDeveloper(u, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + ds.countDevelopers()/DEV_LIST_PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<DeveloperDto>>(devs) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
    
	@GET
	@Path("/{devId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getDevById(@PathParam("devId") long devId)
	{
		final Optional<Developer> maybeDev = ds.findById(devId);
		if(!maybeDev.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		return Response.ok(maybeDev.map(d -> DeveloperDto.fromDeveloper(d, uriInfo)).get()).build();
	}
	
	@GET
	@Path("/{devId}/games")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGamesByDev(@PathParam("devId") long devId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<Developer> maybeDev = ds.findById(devId);
		if(!maybeDev.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GameDto> games = gs.getGamesForDeveloper(maybeDev.get(), page, PAGE_SIZE).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + gs.countGamesForDeveloper(maybeDev.get())/PAGE_SIZE;
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
