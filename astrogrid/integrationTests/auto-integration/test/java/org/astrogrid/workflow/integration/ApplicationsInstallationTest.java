/*$Id: ApplicationsInstallationTest.java,v 1.4 2004/04/21 10:44:05 nw Exp $
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
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.scripting.Service;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/** 
 * Test integration between workflow and applications.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class ApplicationsInstallationTest extends AbstractTestForIntegration {
    /**
     * Constructor for ApplicationsIntegrationTest.
     * @param arg0
     */
    public ApplicationsInstallationTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        List apps = ag.getApplications();
        assertNotNull(apps);
        assertTrue("no application servers found",apps.size() > 0);
        serv = findRequiredService(apps.iterator());        
        delegate = (CommonExecutionConnectorClient)serv.createDelegate();
    } 
    
    protected String applicationName() {
        return TESTAPP;
    }
    
    protected Service findRequiredService(Iterator apps) {
        while( apps.hasNext() ) { // find the correct one
            Service s = (Service)apps.next();
            if (s.getDescription().indexOf("command-line") != -1) {
                return s;
            }           
        }
        fail("failed to find command-line service");
        // never reached.
        return null;
    }
    
    protected Service serv;
    protected CommonExecutionConnectorClient delegate;
    
    public void testApplicationsRegistered() throws Exception {        
        ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
        assertNotNull(reg.getDescriptionFor(applicationName()));
    }

    public void testApplicationResolvable() throws Exception {
        URL requestURL = new URL("http://localhost:8080/astrogrid-jes-SNAPSHOT/backdoor?action=locate&name=" + applicationName());
        InputStream is = requestURL.openStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line = in.readLine();
        assertNotNull(line);
        URL endpoint = new URL(line); // checks its a valid url.
        
    }    
    
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
    
    
    public void testReturnRegistryEntry() throws Exception {
        String entry = delegate.returnRegistryEntry();
        assertNotNull(entry);
        // I guess its xml or something. need to add further testing here
    }
    
    public void testExecute() throws Exception {
        ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
        ApplicationDescription descr = reg.getDescriptionFor(applicationName());
        assertNotNull("could not get application description",descr);
        Tool tool = descr.createToolFromDefaultInterface();
        assertNotNull("tool is null",tool);
        populateTool(tool);
        descr.validate(tool);
        
        JobIdentifierType id = new JobIdentifierType(); // not too bothered about this.
        id.setValue(this.getClass().getName());
       String returnEndpoint ="http://localhost:8080/astrogrid-jes-SNAPSHOT/services/JobMonitorService";      
      String execId = delegate.execute(tool,id,returnEndpoint);
      assertNotNull(execId);
      
    }

    /**
     * @param tool
     */
    protected void populateTool(Tool tool) throws Exception{
        // shouold be ready to go, with no further config.
    }
}


/* 
$Log: ApplicationsInstallationTest.java,v $
Revision 1.4  2004/04/21 10:44:05  nw
tidied to check applicatrions are resolvable.

Revision 1.3  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.2  2004/04/15 23:11:20  nw
tweaks

Revision 1.1  2004/04/15 12:18:25  nw
updating tests

Revision 1.6  2004/04/15 10:28:40  nw
improving testing

Revision 1.5  2004/04/14 16:42:37  nw
fixed tests to break more sensibly

Revision 1.4  2004/04/14 10:16:40  nw
added to the workflow integration tests

Revision 1.3  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.2  2004/04/06 10:55:08  nw
updated to cea connector

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/