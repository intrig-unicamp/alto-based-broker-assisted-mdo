package edu.unicamp.intrig.p5gex.altoBase.AltoServer.api;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

import edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.factories.ConfigApiServiceFactory;
import org.opendaylight.alto.commons.types.rfc7285.MediaType;

@Path("/controller/nb/v2/alto")

public class ConfigApi {
    private final ConfigApiService delegate = ConfigApiServiceFactory.getConfigApi();
        
    @GET
    @Path("/networkmap/{id}")
    @Produces({ MediaType.ALTO_NETWORKMAP, MediaType.ALTO_ERROR })    
    public Response topologyFromALTO(@ApiParam(value = "ID of id",required=true) @PathParam("id") String id, @Context SecurityContext securityContext)
    throws NotFoundException {               
        return delegate.retrieveNetworkMap(id, securityContext);
    }
}
