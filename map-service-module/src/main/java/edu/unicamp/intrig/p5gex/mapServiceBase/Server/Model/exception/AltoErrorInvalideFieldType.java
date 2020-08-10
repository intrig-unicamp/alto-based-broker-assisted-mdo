/*
 * Copyright (c) 2015 Yale University and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package edu.unicamp.intrig.p5gex.mapServiceBase.Server.Model.exception;

import javax.ws.rs.core.Response;

public class AltoErrorInvalideFieldType extends AltoErrorTestException {
    public AltoErrorInvalideFieldType() {
        super(Response.Status.BAD_REQUEST, ERROR_CODES.E_MISSING_FIELD.name());
    }

    public AltoErrorInvalideFieldType(String field) {
        super(Response.Status.BAD_REQUEST, ERROR_CODES.E_MISSING_FIELD.name(), field);
    }
}
