/*$Id: ApplicationsIntegrationTest.java,v 1.4 2004/04/14 10:16:40 nw Exp $
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
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.scripting.Service;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.List;

/** 
 * Test integration between workflow and applications.
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
    
    /** @todo test returned entry further */
    public void testReturnRegistryEntry() throws Exception {
        String entry = delegate.returnRegistryEntry();
        assertNotNull(entry);
        // I guess its xml or something. need to add further testing here
    }
    /** @todo query registry for a named test application*/
    /*
    public void testExecute() throws Exception {
        // hack, will need to find application description by name later.
        ApplicationDescription descr = reg.getDescriptionFor(reg.listApplications()[0]);
        assertNotNull("could not get application description",descr);
        Tool tool = descr.createToolFromDefaultInterface();
        assertNotNull("tool is null",tool);
        descr.validate(tool); // shouold be ready to go, with no further config.
        
        JobIdentifierType id = new JobIdentifierType(); // not too bothered about this.
        id.setValue(this.getClass().getName());
       String returnEndpoint ="http://www.dont.care.for.now";      
      String execId = delegate.execute(tool,id,returnEndpoint);
    }*/
}


/* 
$Log: ApplicationsIntegrationTest.java,v $
Revision 1.4  2004/04/14 10:16:40  nw
added to the workflow integration tests

Revision 1.3  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.2  2004/04/06 10:55:08  nw
updated to cea connector

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/