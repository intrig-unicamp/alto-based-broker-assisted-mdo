package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RFC7285IRD {

    public class Meta extends Extensible {

        @JsonProperty("default-alto-network-map")
        public String defaultAltoNetworkMap;

        @JsonProperty("cost-types")
        public Map<String, CostType> costTypes;

        public Meta() {
            defaultAltoNetworkMap = null;
            costTypes = new LinkedHashMap<String, CostType>();
        }

    }

    public class Entry {
        @JsonProperty("uri")
        public String uri;

        @JsonProperty("media-type")
        public String mediaType;

        @JsonProperty("accepts")
        public String accepts;

        @JsonProperty("capabilities")
        public Map<String, Object> capabilities;

        @JsonProperty("uses")
        public List<String> uses;
    }

    @JsonProperty("meta")
    public Meta meta;

    @JsonProperty("resources")
    public Map<String, Entry> resources = new LinkedHashMap<String, Entry>();
}
