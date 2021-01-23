package ar.edu.itba.paw.webapp.controller.game;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.webapp.dto.GameDto;

@Path("games")
@Component
public class GameController {

	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private GameService                 gs;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    
	private static final String PAGINATION_CURR_PAGE_HEADER = "Current-Page";
	private static final String PAGINATION_PAGE_COUNT_HEADER = "Page-Count";
	private static final String PAGINATION_TOTAL_COUNT_HEADER = "Total-Count";


	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listUsers(@Context HttpServletRequest request, @QueryParam("page") @DefaultValue("1") int page,
							@QueryParam("searchTerm") @DefaultValue("") String searchTerm,
							@QueryParam("hoursRight") @DefaultValue("9999") int hoursRight,
							@QueryParam("minsRight") @DefaultValue("59") int minsRight, 
							@QueryParam("secsRight") @DefaultValue("59") int secsRight,
							@QueryParam("hoursLeft") @DefaultValue("0") int hoursLeft,
							@QueryParam("minsLeft") @DefaultValue("0") int minsLeft,
							@QueryParam("secsLeft") @DefaultValue("0") int secsLeft,
							@QueryParam("scoreRight") @DefaultValue("100") int scoreRight,
							@QueryParam("scoreLeft") @DefaultValue("0") int scoreLeft,
							@QueryParam("platforms") List<String> platforms, 
							@QueryParam("genres") List<String> genres,
							@QueryParam("page_size") @DefaultValue("15") int page_size)
	{
		int timeLeft = hoursLeft*3600 + minsLeft*60 + secsLeft;
		int timeRight = hoursRight*3600 + minsRight*60 + secsRight;
		int countResults = gs.countSearchResultsFiltered(searchTerm, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight);
		final List<GameDto> searchResults = gs.getFilteredGames(searchTerm, genres, platforms, scoreLeft, scoreRight, timeLeft, timeRight, page, page_size)
				.stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		
		int amount_of_pages = (countResults + page_size - 1)/page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(searchResults) {});
		resp.header(PAGINATION_CURR_PAGE_HEADER, page);
		resp.header(PAGINATION_PAGE_COUNT_HEADER, amount_of_pages);
		resp.header(PAGINATION_TOTAL_COUNT_HEADER, countResults);
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).queryParam("page_size", page_size).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).queryParam("page_size", page_size).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).queryParam("page_size", page_size).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).queryParam("page_size", page_size).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("/upcoming")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getUpcomingGames()
	{
        List<GameDto> list = gs.getUpcomingGames().stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<GameDto>>(list) {}).build();
	}
	
	@GET
	@Path("/popular")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getPopularGames()
	{
        List<GameDto> list = gs.getPopularGames().stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<GameDto>>(list) {}).build();
	}
}
