/*
 * $Id: WebServiceApplicationDescriptionLibrary.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on Mar 1, 2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.ws.registry;

import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.component.descriptor.ComponentDescriptor;

/**
 * Responsible for creating the {@link org.astrogrid.applications.ws.WebServiceApplicationDescription}s that this proxy knows about.
 * These are read from the registry.
 * @author Paul Harrison (pharriso@eso.org) Mar 1, 2005
 * @version $Name:  $
 * @since iteration9
 */
public class WebServiceApplicationDescriptionLibrary extends BaseApplicationDescriptionLibrary implements  ComponentDescriptor {

    /**
     * @param env2
     */
    public WebServiceApplicationDescriptionLibrary(CeaRegisteredQuerier querier, ApplicationDescriptionEnvironment env2) {
        super(env2);
       
    }

}
