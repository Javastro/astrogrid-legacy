/*$Id: ExerciseAllModulesSystemTest.java,v 1.1 2005/08/05 11:46:56 nw Exp $
 * Created on 27-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import org.astrogrid.acr.RmiLiteACR;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Module;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRTestSetup;
import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.modules.system.RmiLiteRmiServerImpl;

import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2005
 *
 */
public class ExerciseAllModulesSystemTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        serverRegistry= ACRTestSetup.pico.getACR();
        rmiServer = (RmiServer)serverRegistry.getService(RmiServer.class);
        assertNotNull(rmiServer);
        int port = rmiServer.getPort();
        clientRegistry = new RmiLiteACR(port);
    }
    protected ACR serverRegistry;
    protected RmiLiteACR clientRegistry;
    protected RmiServer rmiServer;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
   
    // tests retrieveal of modules, and iteration through contents - so creating a set of stubs for all the components
    // no significant methods on components called however.
    public void testModules() {
        for (Iterator i = clientRegistry.moduleIterator(); i.hasNext(); ) {
            DefaultModule m = (DefaultModule)i.next();
            assertNotNull(m);
            String name = m.getDescriptor().getName();
            assertNotNull(name);
            for (Iterator j = m.componentIterator(); j.hasNext(); ) {
                Object o = j.next();
                assertNotNull(o);
                name=o.toString(); // call a method that all components support.
                assertNotNull(name);
            }
        }
        
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ExerciseAllModulesSystemTest.class),true); // loginto astrogrid.
    }
}


/* 
$Log: ExerciseAllModulesSystemTest.java,v $
Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/