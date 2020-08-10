package edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api.factories.ConfigApiServiceFactory;
import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.model.VirtualizerSchema;

@Path("/restconf/data")

public class ConfigApi {
    private final ConfigApiService delegate = ConfigApiServiceFactory.getConfigApi();
    
    @GET
    @Path("/virtualizer/")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Retrieve virtualizer", notes = "Retrieve operation of resource: virtualizer", response = VirtualizerSchema.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = VirtualizerSchema.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Internal Error", response = VirtualizerSchema.class) })
    public Response retrieveVirtualizer(@Context SecurityContext securityContext)
    throws NotFoundException {    
        System.out.println("INTRIG: Topology exporter from Virtualizer");
        return delegate.topologyFromALTO(securityContext);
    }    
}
