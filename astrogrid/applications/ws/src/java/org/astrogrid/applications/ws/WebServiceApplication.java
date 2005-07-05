/*
 * $Id: WebServiceApplication.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 * 
 * Created on Feb 28, 2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.ws;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.AbstractApplication.IDs;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pharriso@eso.org) Feb 28, 2005
 * @version $Name:  $
 * @since iteration9
 */
public class WebServiceApplication extends AbstractApplication implements
        Runnable {

    /**
     * @param ids
     * @param tool
     * @param applicationInterface
     * @param lib
     */
    public WebServiceApplication(IDs ids, Tool tool,
            ApplicationInterface applicationInterface, ProtocolLibrary lib) {
        super(ids, tool, applicationInterface, lib);
        // TODO Auto-generated constructor stub
    }
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "WebServiceApplication.run() not implemented");
    }

}
