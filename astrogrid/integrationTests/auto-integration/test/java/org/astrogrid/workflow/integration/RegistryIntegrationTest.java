/*$Id: RegistryIntegrationTest.java,v 1.6 2004/04/21 13:43:43 nw Exp $
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

import org.astrogrid.integration.*;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.scripting.Service;
import org.astrogrid.store.Ivorn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/** test functionality that workflow and jes requires of  the registry
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 */
public class RegistryIntegrationTest extends AbstractTestForIntegration {
    /**
     * Constructor for RegistryIntegrationTest.
     * @param arg0
     */
    public RegistryIntegrationTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        super.setUp();

        reg = ag.getWorkflowManager().getToolRegistry();
        
    }
    protected ApplicationRegistry reg;
    
    /** this is the funcitonality required by workflow */
    public void testListApplications() throws Exception {
        String[] appNames = reg.listApplications();
        assertNotNull("application names are null",appNames);
        assertTrue("no application names",appNames.length > 0);
        for (int i = 0; i < appNames.length; i++) {           
            String name = appNames[i];
            assertNotNull("name is null",name);
            assertTrue("empty name",name.trim().length() > 0);
            ApplicationDescription descr = reg.getDescriptionFor(name);
            assertNotNull("description is null",descr);
            assertEquals("name is not as expected",name,descr.getName());
        }
    }
        
    /** this is the functionality required by jes */
    public void testResolveApplications() throws Exception {
        String[] appNames = reg.listApplications();
        for (int i = 0; i < appNames.length; i++) {
            URL requestURL = new URL("http://localhost:8080/astrogrid-jes-SNAPSHOT/backdoor?action=locate&name=" + appNames[i]);
            InputStream is = requestURL.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line = in.readLine();
            System.out.println("Application " + appNames[i] + " resolves to " + line);
            assertNotNull(line);
            URL endpoint = new URL(line); // checks its a valid url.
            
        }
    }
    
    

    
}


/* 
$Log: RegistryIntegrationTest.java,v $
Revision 1.6  2004/04/21 13:43:43  nw
tidied imports

Revision 1.5  2004/04/21 10:43:46  nw
exercises resolver code

Revision 1.4  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.3  2004/04/15 23:11:20  nw
tweaks

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/