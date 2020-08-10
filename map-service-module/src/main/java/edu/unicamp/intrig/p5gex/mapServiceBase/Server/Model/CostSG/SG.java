package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.Property;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostMap.Meta;

public class SG {
    @JsonProperty("id")
    public String id = new String();

    @JsonProperty("name")
    public String name = new String();
    
    @JsonProperty("nfs")
    public List<NF> nfs;
    
    @JsonProperty("saps")
    public List<SAP> saps;    
    
    @JsonProperty("nexthops")
    public Map<String, NextHop> nexthops
                    = new LinkedHashMap<String, NextHop>();
    
    @JsonProperty("reqs")
    public List<EdgeReq> reqs;
    
}
