package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.service.RegionService;
import ar.edu.itba.paw.webapp.dto.RegionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/regions")
@Component
public class RegionController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private RegionService rs;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response listRegions()
    {
        final List<RegionDto> regions = rs.getAllRegions().stream().map(r -> RegionDto.fromRegion(r, uriInfo)).collect(Collectors.toList());
        Response.ResponseBuilder resp = Response.ok(new GenericEntity<List<RegionDto>>(regions) {});
        return resp.build();
    }
}
