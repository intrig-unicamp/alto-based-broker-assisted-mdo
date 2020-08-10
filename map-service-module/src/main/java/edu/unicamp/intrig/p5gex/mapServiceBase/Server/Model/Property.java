package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;


public class Property {

    public static class PropertyGroup extends Extensible {
        @JsonIgnore
        public static final String UNIFYSLOR_LABEL = "unifyslor";        
        @JsonProperty(UNIFYSLOR_LABEL)
        public String unifyslor = new String();
        
        @JsonIgnore
        public static final String CPU_LABEL = "cpu";        
        @JsonProperty(CPU_LABEL)
        public String cpu = new String();
        
        @JsonIgnore
        public static final String MEM_LABEL = "mem";        
        @JsonProperty(MEM_LABEL)
        public String mem = new String();
        
        @JsonIgnore
        public static final String STORAGE_LABEL = "storage";        
        @JsonProperty(STORAGE_LABEL)
        public String storage = new String();
        @JsonIgnore
        public static final String PORT_LABEL = "port";
        @JsonProperty(PORT_LABEL)
        public List<String> port = new ArrayList<String>();
        
        @JsonIgnore
        public static final String NF_LABEL = "nf";
        @JsonProperty(NF_LABEL)
        public List<String> nf = new ArrayList<String>();                
    }

    public static class PropertyRequest {

        @JsonProperty("properties")
        public List<String> properties = new ArrayList<String>();

        @JsonProperty("endpoints")
        public List<String> endpoints = new ArrayList<String>();
    }

    public static class PropertyResponse {

        public static class Meta extends Extensible {

            @JsonProperty("dependent-vtags")
            public List<VersionTag> netmap_tags = new ArrayList<VersionTag>();

        }

        @JsonProperty("meta")
        public Meta meta = new Meta();

        @JsonProperty("endpoint-properties")
        public Map<String, Map<String, Object>> answer
                            = new LinkedHashMap<String, Map<String, Object>>();
    }

    public static class CostRequest {

        @JsonProperty("cost-type")
        public CostType costType = new CostType();

        @JsonProperty("endpoints")
        public QueryPairs endpoints = new QueryPairs();
    }

    public static class CostResponse {

        public static class Meta extends Extensible {

            @JsonProperty("cost-type")
            public CostType costType = new CostType();

        }

        @JsonProperty("meta")
        public Meta meta = new Meta();

        @JsonProperty("endpoint-cost-map")
        public Map<String, Map<String, Object>> answer
                            = new LinkedHashMap<String, Map<String, Object>>();
    }
}
