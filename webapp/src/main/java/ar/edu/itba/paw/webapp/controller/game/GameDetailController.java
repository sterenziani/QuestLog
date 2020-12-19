package ar.edu.itba.paw.webapp.controller.game;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Playstyle;
import ar.edu.itba.paw.webapp.dto.AvgTimeDto;
import ar.edu.itba.paw.webapp.dto.DeveloperDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GenreDto;
import ar.edu.itba.paw.webapp.dto.PlatformDto;
import ar.edu.itba.paw.webapp.dto.PublisherDto;
import ar.edu.itba.paw.webapp.dto.ReleaseDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.RunDto;
import ar.edu.itba.paw.webapp.dto.ScoreDto;

@Path("games")
@Component
public class GameDetailController {

	@Context
	private UriInfo uriInfo;

    @Autowired
    private GameService                 gs;

    @Autowired
    private RunService                  runs;

    @Autowired
    private ScoreService                scors;
    
    @Autowired
    private ReviewService               revs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameDetailController.class);

	@GET
	@Path("/{gameId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGameById(@PathParam("gameId") long gameId)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		return Response.ok(maybeGame.map(u -> GameDto.fromGame(u, uriInfo)).get()).build();
	}
	
	@GET
	@Path("/{gameId}/platforms")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getPlatformsByGame(@PathParam("gameId") long gameId)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<PlatformDto> platforms = maybeGame.get().getPlatforms().stream().map(p -> PlatformDto.fromPlatform(p, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<PlatformDto>>(platforms) {}).build();
	}
	
	@GET
	@Path("/{gameId}/genres")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGenresByGame(@PathParam("gameId") long gameId)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<GenreDto> genres = maybeGame.get().getGenres().stream().map(g -> GenreDto.fromGenre(g, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<GenreDto>>(genres) {}).build();
	}
	
	@GET
	@Path("/{gameId}/publishers")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getPublishersByGame(@PathParam("gameId") long gameId)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<PublisherDto> publishers = maybeGame.get().getPublishers().stream().map(p -> PublisherDto.fromPublisher(p, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<PublisherDto>>(publishers) {}).build();
	}
	
	@GET
	@Path("/{gameId}/developers")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getDevelopersByGame(@PathParam("gameId") long gameId)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<DeveloperDto> developers = maybeGame.get().getDevelopers().stream().map(d -> DeveloperDto.fromDeveloper(d, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<DeveloperDto>>(developers) {}).build();
	}
	
	@GET
	@Path("/{gameId}/release_dates")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getReleasesByGame(@PathParam("gameId") long gameId)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ReleaseDto> releaseDates = maybeGame.get().getReleaseDates().stream().map(r -> ReleaseDto.fromRelease(r, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<ReleaseDto>>(releaseDates) {}).build();
	}
	
	@GET
	@Path("{gameId}/scores")
	public Response listScoresByGame(@PathParam("gameId") long gameId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("20") int page_size)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ScoreDto> scores = scors.findAllGameScores(maybeGame.get(), page, page_size).stream().map(s -> ScoreDto.fromScore(s, uriInfo)).collect(Collectors.toList());;
		int amount_of_pages = (scors.countAllGameScores(maybeGame.get()) + page_size - 1) / page_size;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<ScoreDto>>(scores) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("{gameId}/runs")
	public Response listRunsByGame(@PathParam("gameId") long gameId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("20") int page_size)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<RunDto> runList = runs.findAllGameRuns(maybeGame.get(), page, page_size).stream().map(r -> RunDto.fromRun(r, uriInfo)).collect(Collectors.toList());;
		int amount_of_pages = (scors.countAllGameScores(maybeGame.get()) + page_size - 1) / page_size;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<RunDto>>(runList) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("/{gameId}/average_times")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getAvgTimesByGame(@PathParam("gameId") long gameId)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		HashMap<Playstyle, String> mapa = runs.getAverageAllPlayStyles(maybeGame.get());
		List<AvgTimeDto> times = new ArrayList<AvgTimeDto>();
		for(Playstyle style : mapa.keySet())
		{
			times.add(AvgTimeDto.fromAvgTime(style, mapa.get(style), uriInfo));
		}
		return Response.ok(new GenericEntity<List<AvgTimeDto>>(times) {}).build();
	}
	
	@GET
	@Path("/{gameId}/top_runs")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getTopRunsByGame(@PathParam("gameId") long gameId, @QueryParam("top") @DefaultValue("5") int top)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<RunDto> topRuns = runs.getTopRuns(maybeGame.get(), top).stream().map(r -> RunDto.fromRun(r, uriInfo)).collect(Collectors.toList());
		return Response.ok(new GenericEntity<List<RunDto>>(topRuns) {}).build();
	}
	
	@GET
	@Path("{gameId}/reviews")
	public Response listReviewsByGame(@PathParam("gameId") long gameId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("10") int page_size)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ReviewDto> reviews = revs.findGameReviews(maybeGame.get(), page, page_size).stream().map(r -> ReviewDto.fromReview(r, uriInfo)).collect(Collectors.toList());;
		int amount_of_pages = (revs.countReviewsForGame(maybeGame.get()) + page_size - 1) / page_size;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<ReviewDto>>(reviews) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
}
