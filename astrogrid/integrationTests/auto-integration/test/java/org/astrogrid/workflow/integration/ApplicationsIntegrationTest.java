/*$Id: ApplicationsIntegrationTest.java,v 1.1 2004/03/16 17:48:34 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.delegate.beans.SimpleApplicationDescription;
import org.astrogrid.scripting.Service;

import org.exolab.castor.mapping.loader.DelegateFieldDescriptor;

import java.util.List;

import junit.framework.TestCase;

/** exercise applications service - only so much you can do without submitting a fulblown job.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class ApplicationsIntegrationTest extends AbstractTestForIntegration {
    /**
     * Constructor for ApplicationsIntegrationTest.
     * @param arg0
     */
    public ApplicationsIntegrationTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        List apps = ag.getApplications();
        assertNotNull(apps);
        assertTrue(apps.size() > 0);
        serv = (Service)apps.get(0);
        delegate = (ApplicationController)serv.createDelegate();
    }
    protected Service serv;
    protected ApplicationController delegate;
    
    public void testListApplications() throws Exception {
        String[] results = delegate.listApplications();
        assertNotNull(results);
        assertTrue(results.length > 0);
        System.out.println("Applications for Server " + serv.getEndpoint());
        for (int i = 0; i < results.length; i++) {
            System.out.print(results[i]+ " ");
        } 
    }
    public void testApplicationDescription() throws Exception {
        SimpleApplicationDescription description = delegate.getApplicationDescription(delegate.listApplications()[0]);
        assertNotNull(description);
        System.out.println(description.getXmlDescriptor());
    }
}


/* 
$Log: ApplicationsIntegrationTest.java,v $
Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/