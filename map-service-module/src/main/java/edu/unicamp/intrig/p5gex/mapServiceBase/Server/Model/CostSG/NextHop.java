package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NextHop {            
    @JsonProperty("src_node")
    public String src_node = new String();
    
    @JsonProperty("src_type")
    public String src_type = new String();
    
    @JsonProperty("src_node_nf")
    public String src_node_nf = new String();

    @JsonProperty("dst_node")
    public String dst_node = new String();
    
    @JsonProperty("dst_type")
    public String dst_type = new String();
    
    @JsonProperty("dst_node_nf")
    public String dst_node_nf = new String();

}
