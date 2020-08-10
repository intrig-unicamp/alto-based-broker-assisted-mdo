/*
 * Copyright © 2017 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.MultiCost.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostMap;
import edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.CostType;

public class MulticostResponse extends CostMap {

    public static class Meta extends CostMap.Meta {

        public Meta(CostMap.Meta base) {
            this.netmap_tags = base.netmap_tags;
            this.costType = base.costType;
        }

        @JsonProperty(MulticostRequest.FIELD_MULCOST)
        public List<CostType> multicostTypes;
    }

}
