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
import ar.edu.itba.paw.interfaces.service.GenreService;
import ar.edu.itba.paw.model.entity.Genre;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GenreDto;

@Path("/genres")
@Component
public class GenreController {

	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private GenreService                gens;
    
    @Autowired
    private GameService                 gs;
    
	private static final String PAGINATION_CURR_PAGE_HEADER = "Current-Page";
	private static final String PAGINATION_PAGE_COUNT_HEADER = "Page-Count";

	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listGenres(@QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("25") int page_size)
	{
		final List<GenreDto> genres = gens.getGenres(page, page_size).stream().map(g -> GenreDto.fromGenre(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (gens.countGenres() + page_size - 1) / page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GenreDto>>(genres) {});
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
	public Response getGamesByGenre(@PathParam("genreId") long genreId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("15") int page_size)
	{
		final Optional<Genre> maybeGenre = gens.findById(genreId);
		if(!maybeGenre.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GameDto> games = gs.getGamesForGenre(maybeGenre.get(), page, page_size).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (gs.countGamesForGenre(maybeGenre.get()) + page_size - 1) / page_size;
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
}
