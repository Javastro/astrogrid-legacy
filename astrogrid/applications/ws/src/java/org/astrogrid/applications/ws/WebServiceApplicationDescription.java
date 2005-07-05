/*
 * $Id: WebServiceApplicationDescription.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on Mar 1, 2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.ws;

import java.net.URL;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * The CEA description of a web service. 
 * @TODO at the moment there is only
 * @author Paul Harrison (pharriso@eso.org) Mar 1, 2005
 * @version $Name:  $
 * @since iteration9
 */
public class WebServiceApplicationDescription extends
        AbstractApplicationDescription {

    /** the location of the WSDL description of the service */
    URL WSDLlocation;
    /** a string that can be used to search the registy for service instances*/
    String registrySearch;
    /**
     * @param env
     */
    public WebServiceApplicationDescription(
            ApplicationDescriptionEnvironment env) {
        super(env);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String callerAssignedID,
            User user, Tool tool) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "WebServiceApplicationDescription.initializeApplication() not implemented");
    }

}
