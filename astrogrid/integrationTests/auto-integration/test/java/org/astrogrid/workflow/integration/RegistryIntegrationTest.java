/*$Id: RegistryIntegrationTest.java,v 1.3 2004/04/15 23:11:20 nw Exp $
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

import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.scripting.Service;
import org.astrogrid.store.Ivorn;

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
            System.out.println("***" + name);
            assertNotNull("name is null",name);
            assertTrue("empty name",name.trim().length() > 0);
            ApplicationDescription descr = reg.getDescriptionFor(name);
            assertNotNull("description is null",descr);
            assertEquals("name is not as expected",name,descr.getName());
            // now see if we can resolve each name to an endpoint.           
        }
    }
    
    /** this is the functionality required by jes */
    public void testResolveApplications() throws Exception {
        String[] appNames = reg.listApplications();
        assertTrue("no application names",appNames.length > 0);
        RegistryService regService = (RegistryService)((Service)ag.getRegistries().get(0)).createDelegate();
        for (int i = 0; i < appNames.length ; i++) {
            String endpoint = regService.getEndPointByIdentifier(new Ivorn(appNames[i]));
            assertNotNull("Registry failed to resolve "+ appNames[i] + " to an endpoint - it returned null",endpoint);
            try {
                URL endpointURL = new URL(endpoint);
            } catch (MalformedURLException e) {
                fail("registry resolved " + appNames[i] + " to endpoint " + endpoint + " which isn't a valid url");
            }
            System.out.println(endpoint);
        }
    }
    
    

    
}


/* 
$Log: RegistryIntegrationTest.java,v $
Revision 1.3  2004/04/15 23:11:20  nw
tweaks

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/