package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostType;

public class EdgeReq {
    @JsonProperty("id")
    public String id = new String();

    @JsonProperty("src_node")
    public String src_node = new String();
    
    @JsonProperty("dst_node")
    public String dst_node = new String(); 
    
    @JsonProperty("sg_path")
    public List<String> sg_path;    
}
