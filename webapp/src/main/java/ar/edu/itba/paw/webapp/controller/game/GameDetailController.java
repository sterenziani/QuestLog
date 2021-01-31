package ar.edu.itba.paw.webapp.controller.game;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Playstyle;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.model.exception.BadFormatException;
import ar.edu.itba.paw.webapp.controller.AnonBacklogHelper;
import ar.edu.itba.paw.webapp.dto.AvgTimeDto;
import ar.edu.itba.paw.webapp.dto.DeveloperDto;
import ar.edu.itba.paw.webapp.dto.FormErrorDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GenreDto;
import ar.edu.itba.paw.webapp.dto.PlatformDto;
import ar.edu.itba.paw.webapp.dto.PublisherDto;
import ar.edu.itba.paw.webapp.dto.RegisterGameDto;
import ar.edu.itba.paw.webapp.dto.RegisterReleaseDto;
import ar.edu.itba.paw.webapp.dto.RegisterScoreDto;
import ar.edu.itba.paw.webapp.dto.ReleaseDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.RunDto;
import ar.edu.itba.paw.webapp.dto.ScoreDto;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(GameDetailController.class);
    
	private static final String PAGINATION_CURR_PAGE_HEADER = "Current-Page";
	private static final String PAGINATION_PAGE_COUNT_HEADER = "Page-Count";

	@GET
	@Path("/{gameId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getGameById(@PathParam("gameId") long gameId, @QueryParam("backlog") @DefaultValue("") String backlog)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		GameDto g = GameDto.fromGame(maybeGame.get(), uriInfo);
        if(us.getLoggedUser() == null)
        	AnonBacklogHelper.updateDto(g, backlog);
		return Response.ok(g).build();
	}
	
	@POST
	@Path("/new_game")
	@Consumes(value = { MediaType.APPLICATION_JSON })
	public Response createGame(@Valid RegisterGameDto registerGameDto) throws BadFormatException
	{
		User loggedUser = us.getLoggedUser();
        if(loggedUser == null || !loggedUser.getAdminStatus())
        	return Response.status(Response.Status.UNAUTHORIZED).build();
        
		Set<ConstraintViolation<RegisterGameDto>> violations = validator.validate(registerGameDto);
        if (!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
		if(gs.findByTitle(registerGameDto.getTitle()).isPresent())
			return Response.status(Response.Status.CONFLICT).entity(new FormErrorDto("title", "TitleUnique.gameForm")).build();
		Map<Long, LocalDate> dates = convertDates(registerGameDto.getReleaseDates());
		if(dates == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new FormErrorDto("releaseDates", "Invalid date format")).build();
		final Game createdGame = gs.register(registerGameDto.getTitle(), registerGameDto.getCover(), registerGameDto.getDescription(),
				registerGameDto.getTrailer(), registerGameDto.getPlatforms(), registerGameDto.getDevelopers(),
				registerGameDto.getPublishers(), registerGameDto.getGenres(), dates);
		final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdGame.getId())).build();
		return Response.created(uri).build();
	}
	
	@PUT
	@Path("/{gameId}")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response editGame(@PathParam("gameId") long gameId, @Valid RegisterGameDto registerGameDto) throws BadFormatException
	{
		User loggedUser = us.getLoggedUser();
        if(loggedUser == null || !loggedUser.getAdminStatus())
        	return Response.status(Response.Status.UNAUTHORIZED).build();
        
		Set<ConstraintViolation<RegisterGameDto>> violations = validator.validate(registerGameDto);
        if (!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
        Optional<Game> gameWithThatName = gs.findByTitle(registerGameDto.getTitle());
		if(gameWithThatName.isPresent() && gameWithThatName.get().getId() != gameId)
			return Response.status(Response.Status.CONFLICT).entity(new FormErrorDto("title", "TitleUnique.gameForm")).build();

		Map<Long, LocalDate> dates = convertDates(registerGameDto.getReleaseDates());
		if(dates == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new FormErrorDto("releaseDates", "Invalid date format")).build();
		gs.update(gameId, registerGameDto.getTitle(), null, registerGameDto.getDescription(), registerGameDto.getTrailer(), registerGameDto.getPlatforms(), registerGameDto.getDevelopers(),
				registerGameDto.getPublishers(), registerGameDto.getGenres(), dates);
		Optional<Game> updatedGame = gs.findById(gameId);
		return Response.ok(updatedGame.map(u -> GameDto.fromGame(u, uriInfo)).get()).build();
	}
	
	@DELETE
	@Path("/{gameId}")
	@Produces(value = { MediaType.APPLICATION_JSON, })
	public Response deleteUserById(@PathParam("gameId") final long gameId)
	{
		User loggedUser = us.getLoggedUser();
        if(loggedUser == null || !loggedUser.getAdminStatus())
        	return Response.status(Response.Status.UNAUTHORIZED).build();
		gs.removeById(gameId);
		LOGGER.debug("Removing game of ID {}", gameId);
		return Response.noContent().build(); // Da código 204 en vez de 404
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
		User loggedUser = us.getLoggedUser();
		if(loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		Set<ConstraintViolation<RegisterScoreDto>> violations = validator.validate(registerScoreDto);
		if (!violations.isEmpty())
			return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
		final Optional<Game> game = gs.findById(gameId);
		if(!game.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();

		Optional<Score> score = scors.findScore(loggedUser, game.get());
		if(score.isPresent())
			scors.changeScore(registerScoreDto.getScore(), loggedUser, game.get());
		else
			scors.register(loggedUser, game.get(), registerScoreDto.getScore());
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
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<ScoreDto>>(scores) {});
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
	@Path("{gameId}/runs")
	public Response listRunsByGame(@PathParam("gameId") long gameId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("20") int page_size)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<RunDto> runList = runs.findAllGameRuns(maybeGame.get(), page, page_size).stream().map(r -> RunDto.fromRun(r, uriInfo)).collect(Collectors.toList());;
		int amount_of_pages = (scors.countAllGameScores(maybeGame.get()) + page_size - 1) / page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<RunDto>>(runList) {});
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
	public Response listReviewsByGame(@PathParam("gameId") long gameId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("5") int page_size)
	{
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ReviewDto> reviews = revs.findGameReviews(maybeGame.get(), page, page_size).stream().map(r -> ReviewDto.fromReview(r, uriInfo)).collect(Collectors.toList());;
		int amount_of_pages = (revs.countReviewsForGame(maybeGame.get()) + page_size - 1) / page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<ReviewDto>>(reviews) {});
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
