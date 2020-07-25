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
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.PlatformDto;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;

@Path("/platforms")
@Component
public class PlatformController {

	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private PlatformService ps;

    @Autowired
    private UserService us;
    
    @Autowired
    private GameService gs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;
    
    private static final int PLATFORM_LIST_PAGE_SIZE = 20;
    private static final int PAGE_SIZE = 15;

	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listPlatforms(@QueryParam("page") @DefaultValue("1") int page)
	{
		final List<PlatformDto> platforms = ps.getPlatforms(page, PLATFORM_LIST_PAGE_SIZE).stream().map(p -> PlatformDto.fromPlatform(p, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + ps.countPlatforms()/PLATFORM_LIST_PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<PlatformDto>>(platforms) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("/{platformId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getPlatformById(@PathParam("platformId") long platformId)
	{
		final Optional<Platform> maybePlatform = ps.findById(platformId);
		if(!maybePlatform.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		return Response.ok(maybePlatform.map(p -> PlatformDto.fromPlatform(p, uriInfo)).get()).build();
	}
    
	@GET
	@Path("/{platformId}/games")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGamesByPlatform(@PathParam("platformId") long platformId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<Platform> maybePlatform = ps.findById(platformId);
		if(!maybePlatform.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GameDto> games = gs.getGamesForPlatform(maybePlatform.get(), page, PAGE_SIZE).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = 1 + gs.countGamesForPlatform(maybePlatform.get())/PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(games) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @RequestMapping("")
    public ModelAndView platformsList(@RequestParam(required = false, defaultValue = "1", value = "page") int page)
    {
        final ModelAndView mav = new ModelAndView("explore/allPlatforms");
        List<Platform> list = ps.getPlatformsWithGames(page, PLATFORM_LIST_PAGE_SIZE);
        int countResults = ps.countPlatformsWithGames();
        int totalPages = (countResults + PLATFORM_LIST_PAGE_SIZE - 1)/PLATFORM_LIST_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
        mav.addObject("platforms", list);
        mav.addObject("listSize", list.size());
        return mav;
    }

    @RequestMapping("/{platformId}")
    public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("explore/platform");
        User u = us.getLoggedUser();
        Platform p = ps.findById(platformId).orElseThrow(PlatformNotFoundException::new);
        List<Game> pageResults = gs.getGamesForPlatform(p, page, PAGE_SIZE);
    	int countResults = gs.countGamesForPlatform(p);
		int totalPages = (countResults + PAGE_SIZE - 1)/PAGE_SIZE;
        if(u == null)
            backlogCookieHandlerService.updateWithBacklogDetails(pageResults, backlog);
        mav.addObject("platform", p);
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("gamesInPage", pageResults);
		mav.addObject("listIcon", p.getLogo());
        return mav;
    }

    @RequestMapping(value = "/{platformId}", method = RequestMethod.POST)
    public ModelAndView platformProfile(@PathVariable("platformId") long platformId, @RequestParam long gameId, @RequestParam(required = false, defaultValue = "1", value = "page") int page, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/platforms/{platformId}?page="+page);
    }
}
