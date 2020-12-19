package ar.edu.itba.paw.webapp.controller;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.model.entity.Image;
import ar.edu.itba.paw.webapp.exception.ImageNotFoundException;

@Path("images")
@Component
public class ImageController {

    @Autowired
    private ImageService is;
    
	@Context
	private UriInfo uriInfo;
    
    @GET
    @Produces(value = {"image/base64"})
    @Path("/{filename}")
    public Response getPicture(@Context HttpServletRequest request, @PathParam("filename") String filename) throws ImageNotFoundException
    {
        Optional<Image> maybeImage = is.findByImageName(filename);
        if(!maybeImage.isPresent())
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        return Response.ok(maybeImage.get().getImageData()).build();
    }
}
