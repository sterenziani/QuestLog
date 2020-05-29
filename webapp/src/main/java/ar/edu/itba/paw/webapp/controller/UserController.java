package ar.edu.itba.paw.webapp.controller;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Run;
import ar.edu.itba.paw.model.Score;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.ForgotPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;

@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
public class UserController
{
	@Autowired
	private UserService us;
	
	@Autowired
	private GameService gs;
	
	@Autowired
	private RunService rs;
	
    @Autowired
    private ScoreService scors;
	
	@Autowired
	private BacklogCookieHandlerService backlogCookieHandlerService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private static final int USER_PAGE_SIZE = 20;
	private static final int BACKLOG_TEASER_PAGE_SIZE = 5;
	private static final int SCORE_TEASER_PAGE_SIZE = 10;
	private static final int RUNS_TEASER_PAGE_SIZE = 10;
	private static final int SCORES_PAGE_SIZE = 25;
	private static final int RUNS_PAGE_SIZE = 25;
	
	@RequestMapping(value = "/create", method = { RequestMethod.GET })
	public ModelAndView registerForm(@ModelAttribute("registerForm") final UserForm registerForm, HttpServletRequest request)
	{
		User user = us.getLoggedUser();
		if(user != null) {
			String referer = request.getHeader("Referer");
			if(referer == null){
				return new ModelAndView("redirect:/");
			}
			return new ModelAndView("redirect:" + referer);
		}
		return new ModelAndView("user/register");
	}
	
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public ModelAndView register(@Valid @ModelAttribute("registerForm") final UserForm registerForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) 
	{
		User user = us.getLoggedUser();
		if(user != null) {
			String referer = request.getHeader("Referer");
			if(referer == null){
				return new ModelAndView("redirect:/");
			}
			return new ModelAndView("redirect:" + referer);
		}
		if (errors.hasErrors())
			return registerForm(registerForm, request);
		LOGGER.debug("Creating new user {} with email {}.", registerForm.getUsername(), registerForm.getEmail());
		final User u = us.register(registerForm.getUsername(), registerForm.getPassword(), registerForm.getEmail(), LocaleContextHolder.getLocale());
		LOGGER.debug("User {} successfully created.", registerForm.getUsername());
		authWithAuthManager(request, u.getUsername(), registerForm.getPassword());
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request)
	{
		User user = us.getLoggedUser();
		if(user != null) {
			String referer = request.getHeader("Referer");
			if(referer == null){
				return new ModelAndView("redirect:/");
			}
			return new ModelAndView("redirect:" + referer);
		}
		return new ModelAndView("user/login");
	}
	
	@RequestMapping("/login_error")
	public ModelAndView loginError(HttpServletRequest request)
	{
		User user = us.getLoggedUser();
		if(user != null) {
			String referer = request.getHeader("Referer");
			if(referer == null){
				return new ModelAndView("redirect:/");
			}
			return new ModelAndView("redirect:" + referer);
		}
		ModelAndView mav = new ModelAndView("user/login");
		mav.addObject("error", true);
		return mav;
	}
	
	@RequestMapping(value = "/userSearch", method = RequestMethod.GET)
	public ModelAndView userSearch(@RequestParam String search, @RequestParam int page) {
		final ModelAndView mav = new ModelAndView("search/userList");
		List<User> users = us.searchByUsernamePaged(search, page, USER_PAGE_SIZE);
		int countResults = us.countUserSearchResults(search); 

		int totalPages = (countResults + USER_PAGE_SIZE - 1)/USER_PAGE_SIZE;

		mav.addObject("current",page);
		mav.addObject("users", users);
		mav.addObject("searchTerm",search);
		mav.addObject("pages", totalPages);
		mav.addObject("u", us.getLoggedUser());
		return mav;

	}
	
	@RequestMapping(value = "/userSearch", method = RequestMethod.POST)
	public ModelAndView userSearchAdmin(@RequestParam String search, @RequestParam int page, @RequestParam("pickedUser") String pickedUser) {
		
		Optional<User> u = us.findByUsername(pickedUser);
		if(u.isPresent())
			us.changeAdminStatus(u.get());
		return new ModelAndView("redirect:/userSearch?search=" + search + "&page=" + page);

	}
	
	@RequestMapping("/users/{id}")
	public ModelAndView userProfile(@PathVariable("id") long id, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("user/userProfile");
		User visitedUser = us.findById(id).orElseThrow(UserNotFoundException::new);
		User loggedUser = us.getLoggedUser();
		List<Game> gamesInPage = gs.getGamesInBacklog(visitedUser, 1, BACKLOG_TEASER_PAGE_SIZE);
		if(loggedUser == null)
		{
			backlogCookieHandlerService.updateWithBacklogDetails(gamesInPage, backlog);
		}
		mav.addObject("backlog", gamesInPage);
		mav.addObject("backlogCropped", gs.countGamesInBacklog(visitedUser) > BACKLOG_TEASER_PAGE_SIZE);
		mav.addObject("user", visitedUser);
		mav.addObject("scoresInPage", scors.findAllUserScores(visitedUser, 1, SCORE_TEASER_PAGE_SIZE));
		mav.addObject("scoresCropped", scors.countAllUserScores(visitedUser) > SCORE_TEASER_PAGE_SIZE);
		mav.addObject("runsInPage", rs.findRunsByUser(visitedUser, 1, RUNS_TEASER_PAGE_SIZE));
		mav.addObject("runsCropped", rs.countRunsByUser(visitedUser) > RUNS_TEASER_PAGE_SIZE);
		return mav;
	}
	
	@RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndReturnToUserProfile(@PathVariable("id") long id, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/users/{id}");
	}
	
	@RequestMapping("/profile")
	public ModelAndView visitOwnProfile()
	{
		final ModelAndView mav = new ModelAndView("user/userProfile");
		User u = us.getLoggedUser();
		List<Game> gamesInPage = gs.getGamesInBacklog(u, 1, BACKLOG_TEASER_PAGE_SIZE);
		mav.addObject("backlog", gamesInPage);
		mav.addObject("backlogCropped", gs.countGamesInBacklog() > BACKLOG_TEASER_PAGE_SIZE);
		mav.addObject("user", us.findById(u.getId()).orElseThrow(UserNotFoundException::new));
		mav.addObject("scoresInPage", scors.findAllUserScores(u, 1, SCORE_TEASER_PAGE_SIZE));
		mav.addObject("scoresCropped", scors.countAllUserScores(u) > SCORE_TEASER_PAGE_SIZE);
		mav.addObject("runsInPage", rs.findRunsByUser(u, 1, RUNS_TEASER_PAGE_SIZE));
		mav.addObject("runsCropped", rs.countRunsByUser(u) > RUNS_TEASER_PAGE_SIZE);
		return mav;
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndReturnToUserProfile(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/profile");
	}
	
	@RequestMapping(value = "/forgotPassword", method = { RequestMethod.GET })
	public ModelAndView forgotPassword(@ModelAttribute("forgotPasswordForm") final ForgotPasswordForm forgotPasswordForm) 
	{
		return new ModelAndView("user/forgotPassword");
	}
	
	@RequestMapping(value = "/forgotPassword", method = { RequestMethod.POST })
	public ModelAndView forgotPassword(@Valid @ModelAttribute("forgotPasswordForm") final ForgotPasswordForm forgotPasswordForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) 
	{
		if (errors.hasErrors())
			return forgotPassword(forgotPasswordForm);
		User u = us.findByEmail(forgotPasswordForm.getEmail()).orElseThrow(UserNotFoundException::new);
		String token = UUID.randomUUID().toString();
		LOGGER.debug("Creating Password Reset Token for {}.", u.getUsername());
		us.createPasswordResetTokenForUser(u, token);
		final ModelAndView mav = new ModelAndView("user/forgotPassword");
		mav.addObject("emailSent", true);
		return mav;
	}
	
	@RequestMapping("/changePassword")
	public ModelAndView showChangePasswordPage(@ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm, @RequestParam("token") String token)
	{
	    String result = us.validatePasswordResetToken(token);
	    if(result != null)
	    {
	    	LOGGER.debug("Password Reset Token is invalid. Redirecting to 404 page");
	    	return new ModelAndView("redirect:/error404");
	    }
	    else
	    {
	    	final ModelAndView mav = new ModelAndView("user/updatePassword");
	        mav.addObject("token", token);
	        return mav;
	    }
	}
	
	@RequestMapping(value="/changePassword", method = { RequestMethod.POST })
	public ModelAndView savePassword(@Valid @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	{
		String result = us.validatePasswordResetToken(changePasswordForm.getToken());
		if(errors.hasErrors())
			return showChangePasswordPage(changePasswordForm, changePasswordForm.getToken());
	    if(result != null)
	    {
	    	LOGGER.debug("Password Reset Token is invalid. Redirecting to 404 page");
	    	return new ModelAndView("redirect:/error404");
	    }
	    User user = us.getUserByPasswordResetToken(changePasswordForm.getToken()).orElseThrow(UserNotFoundException::new);
	    LOGGER.debug("Updating password for user {}.", user.getUsername());
        us.changeUserPassword(user, changePasswordForm.getPassword());
        LOGGER.debug("Password successfully updated for user {}.", user.getUsername());
        LOGGER.debug("Updating password for user {}.", user.getUsername());
        authWithAuthManager(request, user.getUsername(), changePasswordForm.getPassword());
        us.updateLocale(user, LocaleContextHolder.getLocale());
        return new ModelAndView("redirect:/");
	}

	public void authWithAuthManager(HttpServletRequest request, String username, String password)
	{
	    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
	    authToken.setDetails(new WebAuthenticationDetails(request));
	    Authentication authentication = authenticationManager.authenticate(authToken);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.debug("User {} automatically logged in.", username);
	}
	
    @RequestMapping("/users/{id}/scores")
    public ModelAndView viewScoresByUser(@PathVariable("id") long id, HttpServletResponse response, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("user/fullScoreList");
        User visitedUser = us.findById(id).orElseThrow(UserNotFoundException::new);
        List<Score> scoresInPage = scors.findAllUserScores(visitedUser, page, SCORES_PAGE_SIZE);
        mav.addObject("scoresInPage", scoresInPage);
        int countResults = scors.countAllUserScores(visitedUser);
        int totalPages = (countResults + SCORES_PAGE_SIZE - 1)/SCORES_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("user", visitedUser);
        return mav;
    }
    
    @RequestMapping("/users/{id}/runs")
    public ModelAndView viewRunsByUser(@PathVariable("id") long id, HttpServletResponse response, @RequestParam(required = false, defaultValue = "1", value = "page") int page, @CookieValue(value="backlog", defaultValue="") String backlog)
    {
        final ModelAndView mav = new ModelAndView("user/fullRunsList");
        User visitedUser = us.findById(id).orElseThrow(UserNotFoundException::new);
        List<Run> runsInPage = rs.findRunsByUser(visitedUser, page, RUNS_PAGE_SIZE);
        mav.addObject("runsInPage", runsInPage);
        int countResults = rs.countRunsByUser(visitedUser);
        int totalPages = (countResults + RUNS_PAGE_SIZE - 1)/RUNS_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("user", visitedUser);
        return mav;
    }
}
