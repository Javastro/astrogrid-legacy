/*$Id: ApplicationsIntegrationTest.java,v 1.3 2004/04/08 14:50:54 nw Exp $
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

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.scripting.Service;

import java.util.List;

/** 
 * Test integration between workflow and applications.
 * exercise applications service - only so much you can do without submitting a fulblown job.
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
        assertTrue("no application servers found",apps.size() > 0);
        serv = (Service)apps.get(0);
        delegate = (CommonExecutionConnectorClient)serv.createDelegate();
    }
    protected Service serv;
    protected CommonExecutionConnectorClient delegate;
    
    public void testListApplications() throws Exception {
        ApplicationList results = delegate.listApplications();        
        assertNotNull("list of applications is null",results);
        
        assertTrue("no applications present",results.getApplicationDefnCount() > 0);
        System.out.println("Applications for Server " + serv.getEndpoint());
        for (int i = 0; i < results.getApplicationDefnCount(); i++) {
            System.out.print(results.getApplicationDefn(i)+ " ");
        } 
    }
    public void testApplicationDescription() throws Exception {
        ApplicationList results = delegate.listApplications();
        String name = results.getApplicationDefn(0).getName();
        String description = delegate.getApplicationDescription(name);
        assertNotNull("description of application is null",description);
        System.out.println(description);
    }
    

}


/* 
$Log: ApplicationsIntegrationTest.java,v $
Revision 1.3  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.2  2004/04/06 10:55:08  nw
updated to cea connector

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/