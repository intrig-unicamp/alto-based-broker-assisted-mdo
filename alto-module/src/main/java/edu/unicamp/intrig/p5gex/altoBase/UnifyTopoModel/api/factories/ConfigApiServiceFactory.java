package edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api.factories;

import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.altoBase.UnifyTopoModel.api.impl.ConfigApiServiceImpl;

public class ConfigApiServiceFactory {
    private final static ConfigApiService service = new ConfigApiServiceImpl();

    public static ConfigApiService getConfigApi() {
        return service;
    }
}
