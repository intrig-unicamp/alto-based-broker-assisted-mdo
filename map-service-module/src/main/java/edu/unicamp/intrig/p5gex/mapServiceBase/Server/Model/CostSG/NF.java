package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostSG;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NF {
    @JsonProperty("id")
    public String id = new String();

    @JsonProperty("name")
    public String name = new String(); 
    
    @JsonProperty("functional_type")
    public String functional_type = new String();
    
}
