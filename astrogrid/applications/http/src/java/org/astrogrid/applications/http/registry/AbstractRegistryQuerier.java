/* $Id: AbstractRegistryQuerier.java,v 1.4 2004/09/01 15:42:26 jdt Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 * Created on Jul 30, 2004
 */
package org.astrogrid.applications.http.registry;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains stuff likely to be common to all RegistryQueriers...maintains a List
 * of WebHttpApplications etc
 * 
 * @author jdt
 */
public abstract class AbstractRegistryQuerier implements RegistryQuerier {

    protected Map applications = new HashMap();

    /**
     * Returns a list of  HttpApplication beans. 
     */
    public Collection getHttpApplications() throws IOException {
        return applications.values();
    }

}