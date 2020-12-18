package ar.edu.itba.paw.webapp.controller;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;
import ar.edu.itba.paw.webapp.dto.EditUserLocaleDto;
import ar.edu.itba.paw.webapp.dto.EditUserPasswordDto;
import ar.edu.itba.paw.webapp.dto.FormErrorDto;
import ar.edu.itba.paw.webapp.dto.GameDto;
import ar.edu.itba.paw.webapp.dto.RegisterDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.RunDto;
import ar.edu.itba.paw.webapp.dto.ScoreDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;

/*
@Controller
@ComponentScan("ar.edu.itba.paw.webapp.component")
*/
@Path("users")
@Component
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
    private ReviewService revs;
	
	@Autowired
	private BacklogCookieHandlerService backlogCookieHandlerService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Autowired
    private Validator validator;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private static final int USER_PAGE_SIZE = 20;
	private static final int BACKLOG_TEASER_PAGE_SIZE = 5;
	private static final int BACKLOG_PAGE_SIZE = 15;
	private static final int SCORE_TEASER_PAGE_SIZE = 10;
	private static final int RUNS_TEASER_PAGE_SIZE = 10;
	private static final int REVIEWS_TEASER_PAGE_SIZE = 3;
	private static final int SCORES_PAGE_SIZE = 25;
	private static final int RUNS_PAGE_SIZE = 25;
	private static final int REVIEWS_PAGE_SIZE = 10;
	
	@Context
	private UriInfo uriInfo;
	
	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listUsers(@Context HttpServletRequest request, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("searchTerm") @DefaultValue("") String searchTerm)
	{
		final List<UserDto> allUsers = us.searchByUsernamePaged(searchTerm, page, USER_PAGE_SIZE).stream().map(u -> UserDto.fromUser(u, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (us.countUserSearchResults(searchTerm) + USER_PAGE_SIZE - 1) / USER_PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<UserDto>>(allUsers) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@POST
	@Path("/register")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response createUser(@Valid RegisterDto registerDto)
	{
        Set<ConstraintViolation<RegisterDto>> violations = validator.validate(registerDto);
        if (!violations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
        Locale l = Locale.forLanguageTag(registerDto.getLocale());
        if(l.toLanguageTag().equals("und"))
        	l = Locale.ENGLISH;
		final User registeredUser = us.register(registerDto.getUsername(), registerDto.getPassword(), registerDto.getEmail(), l);
		if(registeredUser == null)
		{
			// Si hubo error al registrar, buscar la causa y notificarlo
			boolean emailInUse = false;
			boolean usernameInUse = false;
			int size = 0;
			int index = 0;
			if(us.findByEmail(registerDto.getEmail()).isPresent())
			{
				emailInUse = true;
				size++;
			}
			if(us.findByUsername(registerDto.getUsername()).isPresent())
			{
				usernameInUse = true;
				size++;
			}
			FormErrorDto[] errors = new FormErrorDto[size];
			if(emailInUse)
				errors[index++] = new FormErrorDto("email", "EmailUnique.registerForm.email");
			if(usernameInUse)
				errors[index++] = new FormErrorDto("username", "UserUnique.registerForm.username");
			return Response.status(Response.Status.CONFLICT).entity(errors.length > 1 ? errors : errors[0]).build();
		}
		final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(registeredUser.getId())).build();
		return Response.created(uri).build();
	}
	
	@GET
	@Path("/{userId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getUserById(@PathParam("userId") long userId)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		return Response.ok(maybeUser.map(u -> UserDto.fromUser(u, uriInfo)).get()).build();
	}
	
    @PUT
    @Path("/{userId}/password")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    public Response updateUserPassword(@PathParam("userId") final long userId, @Valid EditUserPasswordDto editPasswordDto)
    {
        Set<ConstraintViolation<EditUserPasswordDto>> violations = validator.validate(editPasswordDto);
        if(!violations.isEmpty())
        	return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
        if(!us.findById(userId).isPresent())
        	return Response.status(Response.Status.NOT_FOUND).build();
        User loggedUser = us.getLoggedUser();
        if(loggedUser == null || loggedUser.getId() != userId)
        	return Response.status(Response.Status.UNAUTHORIZED).build();
        us.changeUserPassword(loggedUser, editPasswordDto.getPassword());
        return Response.ok(UserDto.fromUser(loggedUser, uriInfo)).build();
    }
    
    @PUT
    @Path("/{userId}/locale")
    @Consumes(value = { MediaType.APPLICATION_JSON, })
    public Response updateUserLocale(@PathParam("userId") final long userId, @Valid EditUserLocaleDto editLocaleDto)
    {   	
        Set<ConstraintViolation<EditUserLocaleDto>> violations = validator.validate(editLocaleDto);
        if(!violations.isEmpty())
        	return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
        if(!us.findById(userId).isPresent())
        	return Response.status(Response.Status.NOT_FOUND).build();
        User loggedUser = us.getLoggedUser();
        if(loggedUser == null || loggedUser.getId() != userId)
        	return Response.status(Response.Status.UNAUTHORIZED).build();
        System.out.println("Updating Locale!");
        us.updateLocale(loggedUser, Locale.forLanguageTag(editLocaleDto.getLocale()));
        return Response.ok(UserDto.fromUser(loggedUser, uriInfo)).build();
    }
	
	@DELETE
	@Path("/{userId}")
	@Produces(value = { MediaType.APPLICATION_JSON, })
	public Response deleteUserById(@PathParam("userId") final long userId) {
        if(!us.findById(userId).isPresent())
        	return Response.status(Response.Status.NOT_FOUND).build();
        User loggedUser = us.getLoggedUser();
        if(loggedUser == null || loggedUser.getId() != userId)
        	return Response.status(Response.Status.UNAUTHORIZED).build();
		us.deleteById(userId);
		return Response.noContent().build(); // Da código 204 en vez de 404
	}
	
    @PUT
    @Path("/{userId}/admin")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response makeAdmin(@PathParam("userId")int userId) {
    	User u = us.findById(userId).orElse(null);
    	if(u != null && !u.getAdminStatus())
    	{
            User loggedUser = us.getLoggedUser();
            if(loggedUser == null || !loggedUser.getAdminStatus())
            	return Response.status(Response.Status.UNAUTHORIZED).build();
    		us.changeAdminStatus(u);
    		return Response.ok(UserDto.fromUser(loggedUser, uriInfo)).build();
    	}
    	else
    		return Response.status(Response.Status.FORBIDDEN).entity(new ValidationErrorDto()).build();
    }

    // TODO: Not tested
    @DELETE
    @Path("/{userId}/admin")
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response removeAdmin(@PathParam("userId")int userId) {
    	User u = us.findById(userId).orElse(null);
    	if(u != null && u.getAdminStatus())
    	{
            User loggedUser = us.getLoggedUser();
            if(loggedUser == null || !loggedUser.getAdminStatus())
            	return Response.status(Response.Status.UNAUTHORIZED).build();
    		us.changeAdminStatus(u);
    		return Response.ok(UserDto.fromUser(loggedUser, uriInfo)).build();
    	}
    	else
    		return Response.status(Response.Status.FORBIDDEN).entity(new ValidationErrorDto()).build();
    }
	
	@GET
	@Path("{userId}/scores")
	public Response listScoresByUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ScoreDto> scores = scors.findAllUserScores(maybeUser.get(), page, SCORES_PAGE_SIZE).stream().map(s -> ScoreDto.fromScore(s, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (scors.countAllUserScores(maybeUser.get()) + SCORES_PAGE_SIZE - 1) / SCORES_PAGE_SIZE;
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
	@Path("{userId}/runs")
	public Response listRunsByUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<RunDto> runs = rs.findRunsByUser(maybeUser.get(), page, RUNS_PAGE_SIZE).stream().map(r -> RunDto.fromRun(r, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (rs.countRunsByUser(maybeUser.get()) + RUNS_PAGE_SIZE - 1) / RUNS_PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<RunDto>>(runs) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("{userId}/reviews")
	public Response listReviewsByUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ReviewDto> reviews = revs.findUserReviews(maybeUser.get(), page, REVIEWS_PAGE_SIZE).stream().map(r -> ReviewDto.fromReview(r, uriInfo)).collect(Collectors.toList());;
		int amount_of_pages = (revs.countReviewsByUser(maybeUser.get()) + REVIEWS_PAGE_SIZE - 1) / REVIEWS_PAGE_SIZE;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<ReviewDto>>(reviews) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	@GET
	@Path("{userId}/backlog")
	public Response listBacklogForUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		List<GameDto> games = gs.getGamesInBacklog(maybeUser.get(), page, BACKLOG_PAGE_SIZE).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (gs.countGamesInBacklog(maybeUser.get()) + BACKLOG_PAGE_SIZE - 1) / BACKLOG_PAGE_SIZE;
		
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(games) {});
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");
		resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", amount_of_pages).build(), "last");
		if(page > 1 && page <= amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
		if(page >= 1 && page < amount_of_pages)
			resp.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
		return resp.build();
	}
	
	///////////////////////////////////////////////////////////////////////
	/*
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
		String referrer = request.getHeader("Referer");
		if(referrer == null)
			request.getSession().setAttribute("url_prior_login", "/");
		else if(!referrer.contains("login") && !referrer.contains("create")) {
			request.getSession().setAttribute("url_prior_login", referrer);
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
		String redirectUrl = "/";
		HttpSession session = request.getSession();
		if (session != null) {
			redirectUrl = (String) session.getAttribute("url_prior_login");
			if (redirectUrl != null) {
				session.removeAttribute("url_prior_login");
				if(redirectUrl.contains("create") || redirectUrl.contains("login")) {
					redirectUrl = "/";
				}
			} else redirectUrl = "/";
		}
		return new ModelAndView("redirect:" + redirectUrl);
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
		String referrer = request.getHeader("Referer");
		if(referrer == null)
			request.getSession().setAttribute("url_prior_login", "/");
		else if(!referrer.contains("login") && !referrer.contains("create")) {
			request.getSession().setAttribute("url_prior_login", referrer);
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
		loadUserScoresRunsAndReviews(mav, visitedUser);
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
		loadUserScoresRunsAndReviews(mav, u);
		return mav;
	}
	
	private void loadUserScoresRunsAndReviews(ModelAndView mav, User u)
	{
		mav.addObject("scoresInPage", scors.findAllUserScores(u, 1, SCORE_TEASER_PAGE_SIZE));
		mav.addObject("scoresCropped", scors.countAllUserScores(u) > SCORE_TEASER_PAGE_SIZE);
		mav.addObject("runsInPage", rs.findRunsByUser(u, 1, RUNS_TEASER_PAGE_SIZE));
		mav.addObject("runsCropped", rs.countRunsByUser(u) > RUNS_TEASER_PAGE_SIZE);
		mav.addObject("reviewsInPage", revs.findUserReviews(u, 1, REVIEWS_TEASER_PAGE_SIZE));
		mav.addObject("reviewsCropped", revs.countReviewsByUser(u) > REVIEWS_TEASER_PAGE_SIZE);
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
	    	LOGGER.debug("Password Reset Token is invalid.");
	    	throw new TokenNotFoundException();
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
		
    @RequestMapping("/users/{id}/scores")
    public ModelAndView viewScoresByUser(@PathVariable("id") long id, HttpServletResponse response, @RequestParam(required = false, defaultValue = "1", value = "page") int page)
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
    public ModelAndView viewRunsByUser(@PathVariable("id") long id, HttpServletResponse response, @RequestParam(required = false, defaultValue = "1", value = "page") int page)
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
    
    @RequestMapping("/users/{id}/reviews")
    public ModelAndView viewReviewsByUser(@PathVariable("id") long id, HttpServletResponse response, @RequestParam(required = false, defaultValue = "1", value = "page") int page)
    {
        final ModelAndView mav = new ModelAndView("user/fullReviewsList");
        User visitedUser = us.findById(id).orElseThrow(UserNotFoundException::new);
        List<Review> reviewsInPage = revs.findUserReviews(visitedUser, page, REVIEWS_PAGE_SIZE);
        mav.addObject("reviewsInPage", reviewsInPage);
        int countResults = revs.countReviewsByUser(visitedUser);
        int totalPages = (countResults + REVIEWS_PAGE_SIZE - 1)/REVIEWS_PAGE_SIZE;
		mav.addObject("pages", totalPages);
		mav.addObject("current", page);
		mav.addObject("user", visitedUser);
        return mav;
    }
    */
    
	private void authWithAuthManager(HttpServletRequest request, String username, String password)
	{
	    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
	    authToken.setDetails(new WebAuthenticationDetails(request));
	    Authentication authentication = authenticationManager.authenticate(authToken);
	    SecurityContextHolder.getContext().setAuthentication(authentication);

        LOGGER.debug("User {} automatically logged in.", username);
	}
}
