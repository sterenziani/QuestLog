package ar.edu.itba.paw.webapp.controller;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.PlatformDto;

@Path("/platforms")
@Component
public class PlatformController {

	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private PlatformService ps;
    
    @Autowired
    private GameService gs;

	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listPlatforms(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("20") int page_size)
	{
		final List<PlatformDto> platforms = ps.getPlatforms(page, page_size).stream().map(p -> PlatformDto.fromPlatform(p, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (ps.countPlatforms() + page_size - 1) / page_size;
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
	public Response getGamesByPlatform(@PathParam("platformId") long platformId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("15") int page_size)
	{
		final Optional<Platform> maybePlatform = ps.findById(platformId);
		if(!maybePlatform.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GameDto> games = gs.getGamesForPlatform(maybePlatform.get(), page, page_size).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (gs.countGamesForPlatform(maybePlatform.get()) + page_size - 1) / page_size;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(games) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("/biggest")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getBiggestPlatforms(@QueryParam("top") @DefaultValue("5") int amount)
	{
		final List<PlatformDto> platforms = ps.getBiggestPlatforms(amount).stream().map(p -> PlatformDto.fromPlatform(p, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<PlatformDto>>(platforms) {}).build();
	}
}
