package ar.edu.itba.paw.webapp.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.dto.AnonBacklogDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.UserDto;

@Path("backlog")
@Component
public class BacklogController {

	@Context
	private UriInfo uriInfo;
    
    @Autowired
    private UserService us;
    
    @Autowired
    private GameService gs;
    
	private static final String PAGINATION_CURR_PAGE_HEADER = "Current-Page";
	private static final String PAGINATION_PAGE_COUNT_HEADER = "Page-Count";
  
    @PUT
    @Path("/{gameId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response addGameToBacklog(@PathParam("gameId")int gameId, @QueryParam("backlog") @DefaultValue("") String backlog)
    {
    	Optional<Game> game = gs.findById(gameId);
    	if(!game.isPresent())
    		return Response.status(Response.Status.NOT_FOUND).build();
    	User loggedUser = us.getLoggedUser();
    	if(loggedUser == null)
    	{
    		String resp = backlog;
    		resp = addToBacklog(gameId, backlog);
    		return Response.ok(AnonBacklogDto.fromCookie(resp, uriInfo)).build();
    	}
    	gs.addToBacklog(gameId);
    	return Response.ok(game.map(g -> GameDto.fromGame(g, uriInfo)).get()).build();
    }
    
    @DELETE
    @Path("/{gameId}")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response removeGameFromBacklog(@PathParam("gameId")int gameId, @QueryParam("backlog") @DefaultValue("") String backlog)
    {
    	Optional<Game> game = gs.findById(gameId);
    	if(!game.isPresent())
    		return Response.status(Response.Status.NOT_FOUND).build();
    	User loggedUser = us.getLoggedUser();
    	if(loggedUser == null)
    	{
    		String resp = backlog;
    		resp = removeFromBacklog(gameId, backlog);
    		return Response.ok(AnonBacklogDto.fromCookie(resp, uriInfo)).build();
    	}
    	gs.removeFromBacklog(gameId);
    	return Response.ok(game.map(g -> GameDto.fromGame(g, uriInfo)).get()).build();
    }
    
    @PUT
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response transferBacklog(@QueryParam("backlog") @DefaultValue("") String backlog)
    {
    	User loggedUser = us.getLoggedUser();
    	if(loggedUser == null)
    		return Response.status(Response.Status.UNAUTHORIZED).build();
        List<Game> anonGames = getGamesInBacklog(backlog);
        for(Game g : anonGames)
            gs.addToBacklog(g.getId());
        return Response.ok(UserDto.fromUser(loggedUser, uriInfo)).build();
    }
    
    @GET
    public Response readBacklog(@QueryParam("backlog") @DefaultValue("") String backlog, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("15") int page_size)
    {
    	User loggedUser = us.getLoggedUser();
    	List<GameDto> games;
    	int amount_of_pages;
    	if(loggedUser == null)
    	{
    		games = getGamesInBacklog(backlog, page, page_size).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
    		amount_of_pages = (countGamesInBacklog(backlog) + page_size - 1) / page_size;
    	}
    	else
    	{
    		games = gs.getGamesInBacklog(loggedUser, page, page_size).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
    		amount_of_pages = (gs.countGamesInBacklog(loggedUser) + page_size - 1) / page_size;
    	}
    	if(amount_of_pages == 0)
    		amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(games) {});
		resp.header(PAGINATION_CURR_PAGE_HEADER, page);
		resp.header(PAGINATION_PAGE_COUNT_HEADER, amount_of_pages);
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).queryParam("page_size", page_size).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).queryParam("page_size", page_size).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).queryParam("page_size", page_size).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).queryParam("page_size", page_size).build(), "next");
		return resp.build();
    }
    
    //////////////// PRIVATE //////////////////////////
	
    private String addToBacklog(long gameId, String backlog)
    {
        if(gameInBacklog(gameId, backlog))
        {
            return backlog;
        }
        return backlog +"-" +gameId +"-";
    }

    private String removeFromBacklog(long gameId, String backlog)
    {
        return backlog.replaceAll("-"+gameId+"-", "");
    }
    
    private boolean gameInBacklog(long gameId, String backlog)
    {
        return backlog.contains("-" +gameId +"-");
    }
    
    private int countGamesInBacklog(String backlog)
    {
    	if(backlog.isEmpty())
    		return 0;
        String copy = backlog.replace("--", "-");
        String[] ids = copy.split("-");
        return ids.length - 1;
    }
    
    private List<Game> getGamesInBacklog(String backlog)
    {
        List<Game> list = new ArrayList<Game>();
        String[] ids = backlog.split("-");
        for(String id : ids)
        {
            if(!id.isEmpty())
            {
                Optional<Game> g = gs.findById(Long.parseLong(id));
                if(g.isPresent())
                {
                    list.add(g.get());
                    g.get().setInBacklog(true);
                }
            }
        }
        return list;
    }
    
    private List<Game> getGamesInBacklog(String backlog, int page, int pageSize)
    {
        List<Game> list = new ArrayList<Game>();
        String copy = backlog.replace("--", "-");
        String[] ids = copy.split("-");
        int base = 1 + pageSize*(page-1);
        if(base > ids.length-1)
        	return Collections.emptyList();
        int limit = pageSize*page;
        if(limit >= ids.length)
        	limit = ids.length-1;
        for(int i=base; i <= limit; i++)
        {
            Optional<Game> g = gs.findById(Long.parseLong(ids[i]));
            if(g.isPresent())
            {
                list.add(g.get());
                g.get().setInBacklog(true);
            }
        }
        return list;
    }
}
