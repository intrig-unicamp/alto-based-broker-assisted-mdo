package edu.unicamp.intrig.p5gex.interDomainBase.UnifyTopoModel.api.factories;

import edu.unicamp.intrig.p5gex.interDomainBase.UnifyTopoModel.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.interDomainBase.UnifyTopoModel.api.impl.ConfigApiServiceImpl;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2016-07-26T10:14:24.517Z")
public class ConfigApiServiceFactory {
    private final static ConfigApiService service = new ConfigApiServiceImpl();

    public static ConfigApiService getConfigApi() {
        return service;
    }
}
