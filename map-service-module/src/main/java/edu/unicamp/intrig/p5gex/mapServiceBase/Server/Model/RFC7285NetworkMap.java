package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Network Map: defined in RFC 7285 section 11.2.1
 * */
public class RFC7285NetworkMap {

    public static class Meta extends Extensible {

        @JsonProperty("vtag")
        public VersionTag vtag = new VersionTag();

    }

    /**
     * used for filtered-network-map, RFC7285 secion 11.3.1
     * */
    public static class Filter {

        @JsonProperty("pids")
        public List<String> pids = new ArrayList<String>();

    }

    @JsonProperty("meta")
    public Meta meta = new Meta();

    @JsonProperty("network-map")
    public Map<String, Property.PropertyGroup> map
                    = new LinkedHashMap<String, Property.PropertyGroup>();
}
