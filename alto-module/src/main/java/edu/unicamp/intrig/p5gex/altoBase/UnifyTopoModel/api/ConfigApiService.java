package edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api;

import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class ConfigApiService {    
    public abstract Response topologyFromALTO(SecurityContext securityContext) throws NotFoundException;    
}
