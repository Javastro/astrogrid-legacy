/*$Id: RegistryIntegrationTest.java,v 1.2 2004/04/08 14:50:54 nw Exp $
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

/** test interactions between workflow and registry
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 * @todo move to real registry
 *
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

    
}


/* 
$Log: RegistryIntegrationTest.java,v $
Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/