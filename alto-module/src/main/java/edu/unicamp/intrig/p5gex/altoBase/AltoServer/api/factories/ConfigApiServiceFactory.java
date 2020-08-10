package edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.factories;

import edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.ConfigApiService;
import edu.unicamp.intrig.p5gex.altoBase.AltoServer.api.impl.ConfigApiServiceImpl;

public class ConfigApiServiceFactory {
    private final static ConfigApiService service = new ConfigApiServiceImpl();

    public static ConfigApiService getConfigApi() {
        return service;
    }
}
