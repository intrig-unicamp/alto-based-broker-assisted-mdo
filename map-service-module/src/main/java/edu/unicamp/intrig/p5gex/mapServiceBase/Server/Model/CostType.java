package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public class CostType {

    @JsonProperty("cost-mode")
    public String mode = null;

    @JsonProperty("cost-metric")
    public String metric = null;

    @JsonProperty("description")
    public String description = null;

    public CostType() {
    }

    public CostType(String mode, String metric) {
        this.mode = mode;
        this.metric = metric;
    }

    public CostType(String mode, String metric, String description) {
        this.mode = mode;
        this.metric = metric;
        this.description = description;
    }

    @Override
    public int hashCode() {
        String[] members = { metric, mode };
        return Arrays.hashCode(members);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj != null ? (obj instanceof CostType) : false))
            return false;

        CostType other = (CostType)obj;
        String[] lhs = { metric, mode };
        String[] rhs = { other.metric, other.mode };
        return Arrays.equals(lhs, rhs);
    }
}
