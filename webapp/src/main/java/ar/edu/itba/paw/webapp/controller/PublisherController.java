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
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PublisherService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Publisher;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.PublisherDto;

@Path("/publishers")
@Component
public class PublisherController {

	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private PublisherService pubs;

    @Autowired
    private GameService gs;
    
    @Autowired
	private UserService us;
    
	private static final String PAGINATION_CURR_PAGE_HEADER = "Current-Page";
	private static final String PAGINATION_PAGE_COUNT_HEADER = "Page-Count";

	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listPublishers(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("30") int page_size, @QueryParam("searchTerm") @DefaultValue("") String searchTerm)
	{
		final List<PublisherDto> publishers = pubs.searchByName(searchTerm, page, page_size).stream().map(p -> PublisherDto.fromPublisher(p, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (pubs.countByName(searchTerm, page, page_size) + page_size - 1) / page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<PublisherDto>>(publishers) {});
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
	
	@GET
	@Path("/{publisherId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getPublisherById(@PathParam("publisherId") long publisherId)
	{
		final Optional<Publisher> maybePublisher = pubs.findById(publisherId);
		if(!maybePublisher.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		return Response.ok(maybePublisher.map(p -> PublisherDto.fromPublisher(p, uriInfo)).get()).build();
	}
	
	@GET
	@Path("/{publisherId}/games")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGamesByPublisher(@PathParam("publisherId") long publisherId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("15") int page_size, @QueryParam("backlog") @DefaultValue("") String backlog)
	{
		final Optional<Publisher> maybePublisher = pubs.findById(publisherId);
		if(!maybePublisher.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GameDto> games = gs.getGamesForPublisher(maybePublisher.get(), page, page_size).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
        if(us.getLoggedUser() == null)
        	AnonBacklogHelper.updateList(games, backlog);
		int amount_of_pages = (gs.countGamesForPublisher(maybePublisher.get()) + page_size - 1) / page_size;
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
	
	@GET
	@Path("/biggest")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getBiggestPublishers(@QueryParam("top") @DefaultValue("20") int amount)
	{
		final List<PublisherDto> publishers = pubs.getBiggestPublishers(amount).stream().map(p -> PublisherDto.fromPublisher(p, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<PublisherDto>>(publishers) {}).build();
	}
}
