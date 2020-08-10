/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;


public class Endpoint {

    public static class AddressGroup extends Extensible {

        @JsonIgnore
        public static final String IPV4_LABEL = "ipv4";

        @JsonIgnore
        public static final String IPV6_LABEL = "ipv6";

        @JsonProperty(IPV4_LABEL)
        public List<String> ipv4 = new ArrayList<String>();

        @JsonProperty(IPV6_LABEL)
        public List<String> ipv6 = new ArrayList<String>();

    }

    public static class PropertyRequest {

        @JsonProperty(value="properties")
        public List<String> properties;

        @JsonProperty(value="endpoints")
        public List<String> endpoints;
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
        public CostType costType;

        @JsonProperty("constraints")
        public List<String> constraints = new ArrayList<String>();

        @JsonProperty("endpoints")
        public QueryPairs endpoints;
    }

    public static class CostResponse {

        public static class Meta extends Extensible {

            @JsonProperty("cost-type")
            public CostType costType = new CostType();

        }

        @JsonProperty("meta")
        public Meta meta = new Meta();

        @JsonProperty("endpoint-cost-map")
        public Map<String, Map<String, Object>> answer = null;
    }
}
