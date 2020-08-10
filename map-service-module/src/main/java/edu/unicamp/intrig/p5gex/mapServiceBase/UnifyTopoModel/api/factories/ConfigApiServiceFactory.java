package edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.api.factories;

import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.mapServiceBase.UnifyTopoModel.api.impl.ConfigApiServiceImpl;

public class ConfigApiServiceFactory {
    private final static ConfigApiService service = new ConfigApiServiceImpl();

    public static ConfigApiService getConfigApi() {
        return service;
    }
}
