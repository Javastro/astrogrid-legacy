/*$Id: RegistryIntegrationTest.java,v 1.16 2005/11/04 17:31:05 clq2 Exp $
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
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    /** should return the same list as listApplications, but fuller details */
    public void testListUIApplications() throws Exception {
        String[] appNames = reg.listApplications();
        ApplicationDescriptionSummary[] summaries = reg.listUIApplications();
        assertNotNull("application names are null",appNames);
        assertNotNull("summaries are null",summaries);
        assertEquals("expected same number of apps",appNames.length, summaries.length);
        for (int i = 0; i < appNames.length; i++) {
            assertEquals(appNames[i],summaries[i].getName());
            assertNotNull(summaries[i].getUIName());
            String[] interfaceNames = summaries[i].getInterfaceNames();
            assertNotNull(interfaceNames);
            assertTrue(interfaceNames.length > 0);
            for (int j = 0; j < interfaceNames.length; j++) {
                assertNotNull(interfaceNames[j]);
                assertTrue(interfaceNames[j].trim().length() > 0);
            }
        }
    }

    /**
     * Tests the resolution of application IVOIDs to service endpoints.
     * Gets a list of all CEA applications and resolves each one in turn, using
     * a servlet built into JES for testing. Any failures are treated as 'soft'
     * failures: the test continues to resolve the other applications but is deemed
     * to have failed if any application cannot be resolved. One application will
     * always fail: it seems to be included for that purpose in other registry tests.
     * In this case we do not let it make the test fail.
     */
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
                if (appNames[i].equals("ivo://org.astrogrid.localhost/InvalidHttpApp")) {
                  System.err.println("This failure was expected.");
                }
                else {
                  softFail("failed to resolve location for " + appNames[i] + ": " + e.getMessage());
                }
            }



        }
    }





}


/*
$Log: RegistryIntegrationTest.java,v $
Revision 1.16  2005/11/04 17:31:05  clq2
axis_gtr_1046

Revision 1.15.86.1  2005/10/25 17:43:31  gtr
I set it not to fail when 'InvalidHttpApp' is not resolved, since that app should not be resolveable.

Revision 1.15  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.14.34.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.14  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.11.24.1  2004/11/10 13:32:27  nw
added test for new method in ApplicationDescriptionRegistry

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