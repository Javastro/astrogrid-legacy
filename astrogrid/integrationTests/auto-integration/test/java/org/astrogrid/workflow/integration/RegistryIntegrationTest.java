/*$Id: RegistryIntegrationTest.java,v 1.11 2004/09/22 11:23:18 nw Exp $
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

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.integration.*;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.scripting.Service;
import org.astrogrid.store.Ivorn;

import java.io.BufferedReader;
import java.io.IOException;
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
            softAssertNotNull("name is null",name);
            softAssertTrue("empty name",name.trim().length() > 0);    
            try {        
            ApplicationDescription descr = reg.getDescriptionFor(name);
            softAssertNotNull("description is null",descr);
            softAssertEquals("name is not as expected",name,descr.getName());
            } catch(Exception e) {
                System.out.println("Duff registry entry found for " + name);
                softFail("failed for " + name + " " + e.getMessage());
                
            }
        }
    }
        
    /** this is the functionality required by jes - we get list of application names, then call a backdoor into the jes webapp to exercise te resolver code. */
    public void testResolveApplications() throws Exception {
        String[] appNames = reg.listApplications();

        String url = SimpleConfig.getProperty(JesSelfTest.JES_BASE_URL);                   
        for (int i = 0; i < appNames.length; i++) {
 
            URL requestURL = new URL(url + "/backdoor?action=locate&name=" + appNames[i]);
            InputStream is = null;
            try {
                is = requestURL.openStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                System.out.println("Application " + appNames[i] + " resolves to " + line);
                assertNotNull(line);
                URL endpoint = new URL(line); // checks its a valid url.                
            } catch (IOException e) {
                System.err.println("failed to resolve location for " + appNames[i]);
                softFail("failed to resolve location for " + appNames[i] + ": " + e.getMessage());
            }


            
        }
    }
    
    
    

    
}


/* 
$Log: RegistryIntegrationTest.java,v $
Revision 1.11  2004/09/22 11:23:18  nw
made more tolerant of duff registyr entries

Revision 1.10  2004/08/27 13:25:40  nw
removed hardcoded endpoint.

Revision 1.9  2004/08/17 09:22:31  nw
better reporting of failures.

Revision 1.8  2004/07/01 11:47:39  nw
cea refactor

Revision 1.7  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

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