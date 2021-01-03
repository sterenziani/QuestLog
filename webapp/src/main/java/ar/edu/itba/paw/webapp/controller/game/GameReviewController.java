package ar.edu.itba.paw.webapp.controller.game;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.Review;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.model.exception.BadFormatException;
import ar.edu.itba.paw.webapp.dto.RegisterReviewDto;
import ar.edu.itba.paw.webapp.dto.RegisterScoreDto;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.ReviewNotFoundException;
import ar.edu.itba.paw.webapp.exception.ReviewsNotEnabledException;
import ar.edu.itba.paw.webapp.form.ReviewForm;

@Path("/games")
@Component
public class GameReviewController
{
	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private Validator validator;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private GameService gs;
	
	@Autowired
	private PlatformService ps;
	
	@Autowired
	private ScoreService ss;
	
    @Autowired
    private ReviewService revs;
    
	@POST
	@Path("/{gameId}/new_review")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response addReview(@Valid RegisterReviewDto registerReviewDto, @PathParam("gameId") long gameId) throws BadFormatException
	{
		User loggedUser = us.getLoggedUser();
		if(loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		Set<ConstraintViolation<RegisterReviewDto>> violations = validator.validate(registerReviewDto);
		if (!violations.isEmpty())
			return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
		final Optional<Game> game = gs.findById(gameId);
		if(!game.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        if(game.get().getPlatforms().size() == 0 || !game.get().hasReleased())
        	return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
		final Optional<Platform> platform = ps.findById(registerReviewDto.getPlatform());
		if(!platform.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		if(!game.get().getPlatforms().contains(platform.get()))
			return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

		Optional<Score> optScore = ss.findScore(loggedUser, game.get());
		if(optScore.isPresent())
			ss.changeScore(registerReviewDto.getScore(), loggedUser, game.get());
		else
			ss.register(loggedUser, game.get(), registerReviewDto.getScore());
		final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(game.get().getId())).build();
		revs.register(loggedUser, game.get(), platform.get(), registerReviewDto.getScore(), registerReviewDto.getBody(), LocalDate.now());
		return Response.created(uri).build();
	}
    
    /////////////////////////////////////////////////////////////////////////////////
    
	@RequestMapping(value = "/{review_id}/delete", method = RequestMethod.POST)
	public ModelAndView deleteReview(@PathVariable("review_id") final long reviewId, HttpServletRequest request)
	{
		Review r = revs.findReviewById(reviewId).orElseThrow(ReviewNotFoundException::new);
		if(us.getLoggedUser().equals(r.getUser()) || us.getLoggedUser().getAdminStatus())
		{
			revs.deleteReview(r);
			String referer = request.getHeader("Referer");
			if(referer == null)
				return new ModelAndView("redirect:/");
			return new ModelAndView("redirect:" + referer);
		}
		return new ModelAndView("redirect:/error403");
	}

	@RequestMapping(value = "/create/{game_id}", method = RequestMethod.GET)
	public ModelAndView writeReview(@PathVariable("game_id") long id, @ModelAttribute("reviewForm") final ReviewForm reviewForm, HttpServletRequest request)
	{
		ModelAndView mav = new ModelAndView("game/reviewForm");
		Game game = gs.findById(id).orElseThrow(GameNotFoundException::new);
        if(game.getPlatforms().size() == 0 || !game.hasReleased())
        	throw new ReviewsNotEnabledException();
		User user = us.getLoggedUser();
		Optional<Score> optScore = ss.findScore(user, game);
		Score score = null;
		if(optScore.isPresent())
			score = optScore.get();
		mav.addObject("game", game);
		mav.addObject("user_score", score);
		return mav;
	}
	
	@RequestMapping(value = "/create/{game_id}", method = RequestMethod.POST)
	public ModelAndView saveReview(@PathVariable("game_id") long id, @Valid @ModelAttribute("reviewForm") final ReviewForm reviewForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	{
		if(errors.hasErrors())
		{
			return writeReview(id, reviewForm, request);
		}
		User u = us.getLoggedUser();
		Game g = gs.findById(id).orElseThrow(GameNotFoundException::new);
        if(g.getPlatforms().size() == 0 || !g.hasReleased())
        	throw new ReviewsNotEnabledException();
        if (reviewForm.isRemoveFromBacklog()){
        	gs.removeFromBacklog(id);
		}
		Platform p = ps.findById(reviewForm.getPlatform()).orElseThrow(PlatformNotFoundException::new);
		Optional<Score> optScore = ss.findScore(u, g);
		if(optScore.isPresent())
			ss.changeScore(reviewForm.getScore(), u, g);
		else
			ss.register(u, g, reviewForm.getScore());
		revs.register(u, g, p, reviewForm.getScore(), reviewForm.getBody(), LocalDate.now());
		return new ModelAndView("redirect:/games/{game_id}?reviews=true");
	}
}
