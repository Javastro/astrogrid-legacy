/*$Id: ServerInstallationTest.java,v 1.1 2004/07/01 11:43:33 nw Exp $
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


import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;

/** 
 * Test basic installation of a cea server - verify it exists, can serve metadata, and its applications have been placed in the registry.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class ServerInstallationTest extends AbstractTestForCEA {
    
    public ServerInstallationTest(String arg0) {
        this(new JavaProviderServerInfo(),arg0);
    }
    
    /**
     * Constructor for ApplicationsIntegrationTest.
     * @param arg0
     */
    public ServerInstallationTest(ServerInfo info,String arg0) {
        super(info.getServerSearchString(),arg0);
        this.info = info;
    }
    protected final ServerInfo info;
    
    public void testServicePresent() throws Exception {
        URL url = new URL( delegate.getTargetEndPoint());
        // will throw malformed if a problem..
        // now just try a http-get to the endpoint - should return something (i.e. not a 404).
        Reader r = new InputStreamReader( url.openStream());
        StringWriter sw = new StringWriter();
        Piper.pipe(r,sw);        
        assertEquals("endpoint page contains error:" + sw.toString(),-1,sw.toString().toLowerCase().indexOf("error"));                
    } 
    
    public void testApplicationsRegistered() throws Exception {        
        ApplicationRegistry reg = ag.getWorkflowManager().getToolRegistry();
        //assertNotNull(reg.getDescriptionFor(applicationName()));
        String appNames[] = info.getApplicationNames();
        for (int i = 0; i < appNames.length; i++) {
            try {
                ApplicationDescription appDesc = reg.getDescriptionFor(appNames[i]);
                softAssertNotNull("application " + appNames[i] + "not registered",appDesc);
            } catch (WorkflowInterfaceException e) {
                softFail("application " + appNames[i] + "not registered: " + e.getMessage());
            }
        }
    }

    
    
    public void testReturnRegistryEntry() throws Exception {
        String entry = delegate.returnRegistryEntry();
        assertNotNull(entry);
        // I guess its xml or something. need to add further testing here
    }

    
    }


/* 
$Log: ServerInstallationTest.java,v $
Revision 1.1  2004/07/01 11:43:33  nw
cea refactor

Revision 1.4  2004/05/17 22:54:59  pah
look at the actual interfaces that are being run

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