package ar.edu.itba.paw.webapp.controller.game;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Review;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.dto.DeveloperDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.GenreDto;
import ar.edu.itba.paw.webapp.dto.PlatformDto;
import ar.edu.itba.paw.webapp.dto.PublisherDto;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.ScoresNotEnabledException;

@Path("/games")
@Component
public class GameDetailController {

	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private UserService                 us;

    @Autowired
    private GameService                 gs;
    
    @Autowired
    private PlatformService				ps;

    @Autowired
    private RunService                  runs;

    @Autowired
    private ScoreService                scors;
    
    @Autowired
    private ReviewService               revs;

    @Autowired
    private BacklogCookieHandlerService backlogCookieHandlerService;

    private static final Logger         LOGGER = LoggerFactory.getLogger(GameDetailController.class);
    private static final int REVIEW_SHOWCASE_SIZE = 5;
    private static final int REVIEWS_PAGE_SIZE = 10;

    
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
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @RequestMapping("/games/{gameId}")
    public ModelAndView gameProfile(@PathVariable("gameId") long gameId, @RequestParam(required = false, defaultValue = "false", value = "reviews") boolean reviewTab, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("game/game");
        User u = us.getLoggedUser();
        Game g = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
        mav.addObject("playAverage", runs.getAverageAllPlayStyles(g));
        mav.addObject("topRuns", runs.getTopRuns(g, 5));
        mav.addObject("averageScore", scors.findAverageScore(g));
        mav.addObject("reviewsInPage", revs.findGameReviews(g, 1, REVIEW_SHOWCASE_SIZE));
        mav.addObject("reviewsCropped", revs.countReviewsForGame(g) > REVIEW_SHOWCASE_SIZE);
        mav.addObject("interactionEnabled", (g.getPlatforms().size() > 0 && g.hasReleased()));
        mav.addObject("reviewInterest", reviewTab);
        if(u == null)
        {
            g.setInBacklog(backlogCookieHandlerService.gameInBacklog(gameId, backlog));
            mav.addObject("game", g);
        }
        else
        {
            mav.addObject("game", g);
            Optional<Score> sc = scors.findScore(u,g);
            if(sc.isPresent())
                mav.addObject("user_score",sc.get());
            else
                mav.addObject("user_score",null);
            mav.addObject("user_runs", runs.findGameRuns(g, u));
            mav.addObject("userReviewsCropped", revs.countReviewsByUserAndGame(u, g) > REVIEW_SHOWCASE_SIZE);
            mav.addObject("userReviews", revs.findUserAndGameReviews(u, g, 1, REVIEW_SHOWCASE_SIZE));
        }
        return mav;
    }

    @RequestMapping(value = "/games/{gameId}", method = RequestMethod.POST)
    public ModelAndView addToBacklogAndShowGameProfile(@PathVariable("gameId") long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
        return new ModelAndView("redirect:/games/{gameId}");
    }

    @RequestMapping(value = "/games/scores/{gameId}", method = { RequestMethod.POST })
    public ModelAndView register(@RequestParam("score") int scoreInput, @RequestParam("game") long gameId, @RequestParam("removeFromBacklog") boolean removeFromBacklog)
    {
        User user = us.getLoggedUser();
        if(user == null)
            return new ModelAndView("redirect:/games/{gameId}");
        Game game = gs.findByIdWithDetails(gameId).orElseThrow(GameNotFoundException::new);
        if(game.getPlatforms().size() == 0 || !game.hasReleased())
        	throw new ScoresNotEnabledException();
        Optional<Score> score = scors.findScore(user, game);
        LOGGER.debug("Registering score {} from user {} for game {}.", scoreInput, user.getUsername(), game.getTitle());
        if (score.isPresent())
            scors.changeScore(scoreInput, user, game);
        else
            scors.register(user, game, scoreInput);
        LOGGER.debug("{}'s score for game {} successfully registered.", user.getUsername(), game.getTitle());
        if(removeFromBacklog)
        	gs.removeFromBacklog(gameId);
        return new ModelAndView("redirect:/games/{gameId}");
    }
    
    @RequestMapping("/games/{id}/reviews")
    public ModelAndView viewReviewsByUser(@PathVariable("id") long id, HttpServletResponse response, @RequestParam(required = false, defaultValue = "1", value = "page") int page)
    {
        final ModelAndView mav = new ModelAndView("game/fullReviewsList");
        Game g = gs.findById(id).orElseThrow(GameNotFoundException::new);
        List<Review> reviewsInPage = revs.findGameReviews(g, page, REVIEWS_PAGE_SIZE);
        mav.addObject("reviewsInPage", reviewsInPage);
        int countResults = revs.countReviewsForGame(g);
        int totalPages = (countResults + REVIEWS_PAGE_SIZE - 1)/REVIEWS_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("game", g);
        return mav;
    }
}
