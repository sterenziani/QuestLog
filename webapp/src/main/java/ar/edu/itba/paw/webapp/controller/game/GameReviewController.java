package ar.edu.itba.paw.webapp.controller.game;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.webapp.exception.ReviewNotFoundException;

@RequestMapping("/reviews")
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class GameReviewController
{
	@Autowired
	private UserService us;
	
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
			if(referer == null){
				return new ModelAndView("redirect:/");
			}
			return new ModelAndView("redirect:" + referer);
		}
		else
			return new ModelAndView("redirect:/error403");
	}
}
