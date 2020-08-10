package edu.unicamp.intrig.p5gex.mapServiceBase.Server.api;

import edu.unicamp.intrig.p5gex.mapServiceBase.Server.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class ConfigApiService {
    public abstract Response retrieveNetworkMap(String id,
            SecurityContext securityContext) throws NotFoundException;

    public abstract Response retrievePropertyMap(String id,
            SecurityContext securityContext) throws NotFoundException;

    public abstract Response retrieveFilteredPropertyMap(String id, String filterJSON,
            SecurityContext securityContext) throws NotFoundException;
    
    public abstract Response retrieveCostMap(String id, String filterJSON,
            SecurityContext securityContext) throws NotFoundException;

    public abstract Response retrieveMultiCostMapFilter(String id,
            String filterJSON, SecurityContext securityContext)
            throws NotFoundException;

    public abstract Response retrieveCostMapSG(String id, String filterJSON,
            SecurityContext securityContext) throws NotFoundException;
}
