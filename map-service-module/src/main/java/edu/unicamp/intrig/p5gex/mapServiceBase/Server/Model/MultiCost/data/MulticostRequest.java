/*
 * Copyright © 2017 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.MultiCost.data;

import java.util.List;

//import org.opendaylight.alto.core.northbound.api.utils.rfc7285.RFC7285CostMap;
//import org.opendaylight.alto.core.northbound.api.utils.rfc7285.RFC7285CostType;




import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostMap;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostType;

public class MulticostRequest extends CostMap.Filter {

    public static final String FIELD_MULCOST = "multi-cost-types";

    public static final String FIELD_TESTABLE = "testable-cost-types";

    public static final String FIELD_ORCONSTRAINT = "or-constraints";

    @JsonProperty(FIELD_MULCOST)
    public List<CostType> multicostTypes;

    @JsonProperty(FIELD_TESTABLE)
    public List<CostType> testableTypes;

    @JsonProperty(FIELD_ORCONSTRAINT)
    public List<List<String>> orConstraintsRepr;

    @JsonIgnore
    public boolean fallbackMode = false;

    //@JsonIgnore
    //public List<List<Condition>> orConstraints;

}
