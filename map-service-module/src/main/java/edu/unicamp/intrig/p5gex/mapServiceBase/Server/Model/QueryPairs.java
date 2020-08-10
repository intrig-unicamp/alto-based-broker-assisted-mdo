package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model;

import java.util.List;
import java.util.LinkedList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QueryPairs {

    @JsonProperty("srcs")
    public List<String> src = new LinkedList<String>();

    @JsonProperty("dsts")
    public List<String> dst = new LinkedList<String>();

}

