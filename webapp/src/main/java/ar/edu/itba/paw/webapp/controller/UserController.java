package ar.edu.itba.paw.webapp.controller;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
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

import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.webapp.dto.*;
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
import ar.edu.itba.paw.model.entity.User;

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
    private Validator validator;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Context
	private UriInfo uriInfo;
	
	private static final String PAGINATION_CURR_PAGE_HEADER = "Current-Page";
	private static final String PAGINATION_PAGE_COUNT_HEADER = "Page-Count";
	
	@GET
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response listUsers(@Context HttpServletRequest request, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("searchTerm") @DefaultValue("") String searchTerm, @QueryParam("page_size") @DefaultValue("20") int page_size)
	{
		final List<UserDto> allUsers = us.searchByUsernamePaged(searchTerm, page, page_size).stream().map(u -> UserDto.fromUser(u, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (us.countUserSearchResults(searchTerm) + page_size - 1) / page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<UserDto>>(allUsers) {});
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
				errors[index++] = new FormErrorDto("email", "signup.email.errors.duplicated");
			if(usernameInUse)
				errors[index++] = new FormErrorDto("username", "signup.username.errors.duplicated");
			return Response.status(Response.Status.CONFLICT).entity(errors).build();
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
	
	@GET
	@Path("/profile")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getLoggedUserProfile()
	{
		User u = us.getLoggedUser();
		if(u == null)
			return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
		return Response.ok(UserDto.fromUser(u, uriInfo)).build();
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
    		return Response.ok(UserDto.fromUser(u, uriInfo)).build();
    	}
    	else
    		return Response.status(Response.Status.FORBIDDEN).entity(new ValidationErrorDto()).build();
    }

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
    		return Response.ok(UserDto.fromUser(u, uriInfo)).build();
    	}
    	else
    		return Response.status(Response.Status.FORBIDDEN).entity(new ValidationErrorDto()).build();
    }
	
	@GET
	@Path("{userId}/scores")
	public Response listScoresByUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("25") int page_size)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ScoreDto> scores = scors.findAllUserScores(maybeUser.get(), page, page_size).stream().map(s -> ScoreDto.fromScore(s, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (scors.countAllUserScores(maybeUser.get()) + page_size - 1) / page_size;
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
	@Path("{userId}/scores/{gameId}")
	public Response getUserGameScore(@PathParam("userId") long userId, @PathParam("gameId") long gameId)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeGame.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		Optional<Score> maybeScore = scors.findScore(maybeUser.get(), maybeGame.get());
		if(!maybeScore.isPresent()) {
			Score score = new Score(maybeUser.get(), maybeGame.get(), -1);
			return Response.ok(false).build();
		}
		return Response.ok(maybeScore.map(u -> ScoreDto.fromScore(u, uriInfo)).get()).build();
	}
	
	@GET
	@Path("{userId}/runs")
	public Response listRunsByUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("25") int page_size)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<RunDto> runs = rs.findRunsByUser(maybeUser.get(), page, page_size).stream().map(r -> RunDto.fromRun(r, uriInfo)).collect(Collectors.toList());
		int amount_of_pages = (rs.countRunsByUser(maybeUser.get()) + page_size - 1) / page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		ResponseBuilder resp = Response.ok(new GenericEntity<List<RunDto>>(runs) {});
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
	@Path("{userId}/runs/{gameId}")
	public Response listGameRunsByUser(@PathParam("userId") long userId, @PathParam("gameId") long gameId)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<RunDto> runs = rs.findGameRuns(maybeGame.get(), maybeUser.get()).stream().map(r -> RunDto.fromRun(r, uriInfo)).collect(Collectors.toList());
		ResponseBuilder resp = Response.ok(new GenericEntity<List<RunDto>>(runs) {});
		return resp.build();
	}
	
	@GET
	@Path("{userId}/reviews")
	public Response listReviewsByUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("10") int page_size)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ReviewDto> reviews = revs.findUserReviews(maybeUser.get(), page, page_size).stream().map(r -> ReviewDto.fromReview(r, uriInfo)).collect(Collectors.toList());;
		int amount_of_pages = (revs.countReviewsByUser(maybeUser.get()) + page_size - 1) / page_size;
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
	
	@GET
	@Path("{userId}/reviews/{gameId}")
	public Response listGameReviewsByUser(@PathParam("userId") long userId, @PathParam("gameId") long gameId)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final Optional<Game> maybeGame = gs.findById(gameId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		final List<ReviewDto> reviews = revs.findUserAndGameReviews(maybeUser.get(), maybeGame.get()).stream().map(r -> ReviewDto.fromReview(r, uriInfo)).collect(Collectors.toList());
		ResponseBuilder resp = Response.ok(new GenericEntity<List<ReviewDto>>(reviews) {});
		return resp.build();
	}
	
	@GET
	@Path("{userId}/backlog")
	public Response listBacklogForUser(@PathParam("userId") long userId, @QueryParam("page") @DefaultValue("1") int page, @QueryParam("page_size") @DefaultValue("15") int page_size, @QueryParam("backlog") @DefaultValue("") String backlog)
	{
		final Optional<User> maybeUser = us.findById(userId);
		if(!maybeUser.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		List<GameDto> games = gs.getGamesInBacklog(maybeUser.get(), page, page_size).stream().map(g -> GameDto.fromGame(g, uriInfo)).collect(Collectors.toList());
        if(us.getLoggedUser() == null)
        	AnonBacklogHelper.updateList(games, backlog);
		int amount_of_pages = (gs.countGamesInBacklog(maybeUser.get()) + page_size - 1) / page_size;
		if(amount_of_pages == 0)
			amount_of_pages = 1;
		
		ResponseBuilder resp = Response.ok(new GenericEntity<List<GameDto>>(games) {});
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
	@Path("/login")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getLoggedUserPrivileges()
	{
		User u = us.getLoggedUser();
		if(u == null)
			return Response.status(Response.Status.UNAUTHORIZED.getStatusCode()).build();
		return Response.ok(UserPrivilegesDto.fromUser(u)).build();
	}
}
