package edu.unicamp.intrig.p5gex.altoBase.AltoServer.api;

import edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class ConfigApiService {            
    public abstract Response retrieveNetworkMap (String id, SecurityContext securityContext) throws NotFoundException;   
}
