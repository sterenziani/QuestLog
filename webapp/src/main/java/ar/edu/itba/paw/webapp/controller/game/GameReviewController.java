package ar.edu.itba.paw.webapp.controller.game;
import java.time.LocalDate;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import ar.edu.itba.paw.webapp.exception.GameNotFoundException;
import ar.edu.itba.paw.webapp.exception.PlatformNotFoundException;
import ar.edu.itba.paw.webapp.exception.ReviewNotFoundException;
import ar.edu.itba.paw.webapp.exception.ReviewsNotEnabledException;
import ar.edu.itba.paw.webapp.form.ReviewForm;

@RequestMapping("/reviews")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class GameReviewController
{
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
