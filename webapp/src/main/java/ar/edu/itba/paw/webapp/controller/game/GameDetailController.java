package ar.edu.itba.paw.webapp.controller.game;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.webapp.dto.*;


import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import org.springframework.web.multipart.MultipartFile;

import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Playstyle;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.model.exception.BadFormatException;


@Path("games")
@Component
public class GameDetailController {

	@Context
	private UriInfo uriInfo;

    @Autowired
    private GameService                 gs;

    @Autowired
	private UserService					us;

    @Autowired
    private RunService                  runs;

    @Autowired
    private ScoreService                scors;
    
    @Autowired
    private ReviewService               revs;
    
    @Autowired
    private Validator validator;

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
	
	@POST
	@Path("/new_game")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response createGame(@Valid RegisterGameDto registerGameDto) throws BadFormatException
	{
		Set<ConstraintViolation<RegisterGameDto>> violations = validator.validate(registerGameDto);
        if (!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
		if(gs.findByTitle(registerGameDto.getTitle()).isPresent())
			return Response.status(Response.Status.CONFLICT).entity(new FormErrorDto("title", "TitleUnique.gameForm")).build();

		Map<Long, LocalDate> dates = convertDates(registerGameDto.getReleaseDates());
		if(dates == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new FormErrorDto("releaseDates", "Invalid date format")).build();
		final Game createdGame = gs.register(registerGameDto.getTitle(), null, registerGameDto.getDescription(),
				registerGameDto.getTrailer(), registerGameDto.getPlatforms(), registerGameDto.getDevelopers(),
				registerGameDto.getPublishers(), registerGameDto.getGenres(), dates);
		final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdGame.getId())).build();
		return Response.created(uri).build();
	}
	
	private Map<Long, LocalDate> convertDates(List<RegisterReleaseDto> dates)
	{
		Map<Long, LocalDate> map = new HashMap<>();
		for(RegisterReleaseDto r : dates)
		{
			try
			{
				LocalDate ld = LocalDate.parse(r.getDate());
				map.put(r.getLocale(), ld);
			}
			catch(DateTimeParseException exception)
			{
				return null;
			}
		}
		return map;
	}

	@POST
	@Path("/{gameId}/new_score")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response addScore(@Valid RegisterScoreDto registerScoreDto, @PathParam("gameId") long gameId) throws BadFormatException
	{
		Set<ConstraintViolation<RegisterScoreDto>> violations = validator.validate(registerScoreDto);
		if (!violations.isEmpty())
			return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
		System.out.println(violations);
		final Optional<Game> game = gs.findById(gameId);
		if(!game.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final Optional<User> user = us.findByUsername(registerScoreDto.getUsername());
		if(!user.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		Optional<Score> score = scors.findScore(user.get(), game.get());
		if(score.isPresent())
			scors.changeScore(registerScoreDto.getScore(), user.get(), game.get());
		else
			scors.register(user.get(), game.get(), registerScoreDto.getScore());
		final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(game.get().getId())).build();
		return Response.created(uri).build();
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
