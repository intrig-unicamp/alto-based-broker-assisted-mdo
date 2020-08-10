package edu.unicamp.intrig.p5gex.mapServiceBase.Server.api;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;

import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.MediaType;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.api.factories.ConfigApiServiceFactory;

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
    
    @GET
    @Path("/propertymap/{id}")
    @Produces({ MediaType.ALTO_PROPERTYMAP, MediaType.ALTO_ERROR })    
    public Response propertyMap(@ApiParam(value = "ID of id",required=true) @PathParam("id") String id, @Context SecurityContext securityContext)
    throws NotFoundException {               
        return delegate.retrievePropertyMap(id, securityContext);
    }
    
    @POST
    @Path("/filtered/propertymap/{id}")
    @Consumes({ MediaType.ALTO_PROPERTYMAP_FILTER    })
    @Produces({ MediaType.ALTO_PROPERTYMAP, MediaType.ALTO_ERROR })    
    public Response FilteredPropertyMap(@ApiParam(value = "ID of id",required=true) @PathParam("id") String id, String filterJSON, @Context SecurityContext securityContext)
    throws NotFoundException {               
        return delegate.retrieveFilteredPropertyMap(id, filterJSON, securityContext);
    }
    
    @POST
    @Path("/filtered/costmap/{id}")
    @Consumes({ MediaType.ALTO_COSTMAP_FILTER })
    @Produces({ MediaType.ALTO_COSTMAP, MediaType.ALTO_ERROR })    
    public Response CostMap(@ApiParam(value = "ID of id",required=true) @PathParam("id") String id, String filterJSON, @Context SecurityContext securityContext)
    throws NotFoundException {               
        return delegate.retrieveCostMap(id, filterJSON, securityContext);
    }
    
    @POST
    @Path("/multi/costmap/filtered/{id}")
    @Consumes({ MediaType.ALTO_COSTMAP_FILTER })
    @Produces({ MediaType.ALTO_COSTMAP, MediaType.ALTO_ERROR })    
    public Response MultiCostMapFilter(@ApiParam(value = "ID of id",required=true) @PathParam("id") String id, String filterJSON, @Context SecurityContext securityContext)
    throws NotFoundException {               
        return delegate.retrieveMultiCostMapFilter(id, filterJSON, securityContext);
    }
    
    @POST
    @Path("/costmap/sg/{id}")
    @Consumes({ MediaType.ALTO_COSTMAP_FILTER })
    @Produces({ MediaType.ALTO_COSTMAP, MediaType.ALTO_ERROR })    
    public Response CostMapSG(@ApiParam(value = "ID of id",required=true) @PathParam("id") String id, String filterJSON, @Context SecurityContext securityContext)
    throws NotFoundException {            
        return delegate.retrieveCostMapSG(id, filterJSON, securityContext);
    }
}
