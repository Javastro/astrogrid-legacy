/*$Id: ApplicationsInstallationTest.java,v 1.3 2004/05/17 12:37:31 pah Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.integration.*;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.scripting.Service;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

/** 
 * Test CEA interface of command-line application controller.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class ApplicationsInstallationTest extends AbstractTestForApplications {
    /**
     * Constructor for ApplicationsIntegrationTest.
     * @param arg0
     */
    public ApplicationsInstallationTest(String arg0) {
        super(arg0);
    }
    
    public void testApplicationsRegistered() throws Exception {        
        ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
        assertNotNull(reg.getDescriptionFor(applicationName()));
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
    
    }


/* 
$Log: ApplicationsInstallationTest.java,v $
Revision 1.3  2004/05/17 12:37:31  pah
Improve CEA tests that call application controller directly

Revision 1.2  2004/04/26 12:16:07  nw
got applications int test working.
dsa works, but suspect its failing under the hood.

Revision 1.1  2004/04/21 13:41:34  nw
set up applications integration tests

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