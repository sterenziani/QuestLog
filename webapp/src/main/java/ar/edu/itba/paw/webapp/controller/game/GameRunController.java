package ar.edu.itba.paw.webapp.controller.game;
import java.net.URI;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.RunService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.Playstyle;
import ar.edu.itba.paw.model.entity.Run;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.model.exception.BadFormatException;
import ar.edu.itba.paw.webapp.dto.RegisterRunDto;
import ar.edu.itba.paw.webapp.dto.RunDto;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;

@Path("games")
@Component
public class GameRunController
{
	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private Validator validator;
    
    @Autowired
    private UserService         us;

    @Autowired
    private GameService         gs;

    @Autowired
    private RunService          runs;

    @Autowired
    private PlatformService     ps;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameRunController.class);

	@POST
	@Path("{gameId}/new_run")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response addRun(@Valid RegisterRunDto registerRunDto, @PathParam("gameId") long gameId) throws BadFormatException
	{
		User loggedUser = us.getLoggedUser();
		if(loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		Set<ConstraintViolation<RegisterRunDto>> violations = validator.validate(registerRunDto);
		if (!violations.isEmpty())
			return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
		
		final Optional<Game> game = gs.findById(gameId);
		if(!game.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		if(game.get().getPlatforms().size() == 0 || !game.get().hasReleased())
        	return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
		final Optional<Platform> platform = ps.findById(registerRunDto.getPlatform());
		if(!platform.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		if(!game.get().getPlatforms().contains(platform.get()))
			return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
		final Optional<Playstyle> playstyle = runs.findPlaystyleById(registerRunDto.getPlaystyle());
		if(!playstyle.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		
		Run run = runs.register(loggedUser, game.get(), platform.get(), playstyle.get(), registerRunDto.getTime());
		final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(run.getId())).build();
		return Response.created(uri).build();
	}
	
	@GET
	@Path("/runs/{runId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getRunById(@PathParam("runId") long runId)
	{
		Optional<Run> run = runs.findRunById(runId);
		if(!run.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();	
		return Response.ok(RunDto.fromRun(run.get(), uriInfo)).build();
	}
}
