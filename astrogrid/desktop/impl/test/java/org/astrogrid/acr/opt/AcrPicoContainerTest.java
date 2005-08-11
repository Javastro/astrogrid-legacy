/*$Id: AcrPicoContainerTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.opt;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.WebServer;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.prefs.BackingStoreException;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class AcrPicoContainerTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        Finder f = new Finder();
        acr = f.find();
        pico = new DefaultPicoContainer(new AcrPicoContainer(acr));
    }

    protected ACR acr;
    protected MutablePicoContainer pico;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testContainer() throws Exception{
        assertNotNull(acr);
        pico.registerComponentImplementation(TestObject.class);
        pico.start();
        TestObject t = (TestObject)pico.getComponentInstanceOfType(TestObject.class);
        assertNotNull(t);
        t.doTest();
    }
    
    
    /** test object for container - requires two common acr services */
    public static class TestObject {
        
        public TestObject(WebServer ws, Configuration c) {
            this.ws = ws;
            this.c =c;
        }
        protected final WebServer ws;
        protected final Configuration c;
        
        public void doTest() throws  ACRException {
            assertNotNull(ws);
            assertNotNull(c);
            assertTrue(ws.getPort() > 0);
            assertTrue(c.list().size() > 0);
        }
        
        
    }
}


/* 
$Log: AcrPicoContainerTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/