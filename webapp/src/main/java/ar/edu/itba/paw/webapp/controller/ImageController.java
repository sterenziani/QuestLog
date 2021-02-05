package ar.edu.itba.paw.webapp.controller;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @Produces(value = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE })
    @Path("/{category}/{filename}")
    public Response getPicture(@PathParam("filename") String filename, @PathParam("category") String category) throws ImageNotFoundException
    {
        Optional<Image> maybeImage = is.findByImageName(category + "/" + filename);
        if(!maybeImage.isPresent())
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        return Response.ok(maybeImage.get().getImageData()).header("Cache-Control", "max-age=31536000").build();
    }
}
