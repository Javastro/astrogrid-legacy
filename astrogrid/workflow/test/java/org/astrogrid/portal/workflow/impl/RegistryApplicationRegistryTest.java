/*$Id: RegistryApplicationRegistryTest.java,v 1.4 2004/04/14 15:31:56 nw Exp $
 * Created on 10-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;

import java.net.URL;
import java.util.Arrays;

import junit.framework.TestCase;

/** hard to test this in a unit-test way.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 * @todo this is an integration test- move to integration testing.
 */
public class RegistryApplicationRegistryTest extends TestCase {
    /**
     * Constructor for RegistryApplicationRegistryTest.
     * @param arg0
     */
    public RegistryApplicationRegistryTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        URL url = new URL(ENDPOINT);
        reg = new RegistryApplicationRegistry(url);
    }
    public final static String ENDPOINT = "http://msslxy.mssl.ucl.ac.uk:8080/astrogrid-registry-webapp/services/Registry";
    protected ApplicationRegistry reg;
    
    public void dontTestListApplications() throws Exception {
        String[] apps = reg.listApplications();
        assertNotNull(apps);
        assertTrue(apps.length > 0);
        System.out.println(Arrays.asList(apps));
        
    }
    
    public void dontTestGetDescriptionFor() throws Exception {
        String[] apps = reg.listApplications();
       for (int i = 0; i < apps.length; i++) {
            ApplicationDescription descr = reg.getDescriptionFor(apps[i]);
            assertNotNull(descr);
        }
    }
}


/* 
$Log: RegistryApplicationRegistryTest.java,v $
Revision 1.4  2004/04/14 15:31:56  nw
disabled test - its an integration test really anyhow.

Revision 1.3  2004/04/05 15:15:18  nw
tests for the fresh implementation

Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:52  nw
tests for impls
 
*/