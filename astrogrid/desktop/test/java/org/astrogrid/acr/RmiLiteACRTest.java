/*$Id: RmiLiteACRTest.java,v 1.1 2005/08/05 11:46:56 nw Exp $
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

/** Exercise the rmiLite implementation of the finder.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2005
 *
 */
public class RmiLiteACRTest extends TestCase {

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
    
    public void testBasics() {
        assertNotNull(clientRegistry.client);        
        assertNotNull(clientRegistry.remoteRegistry);
        assertNotSame(serverRegistry,clientRegistry.remoteRegistry);
        assertNotNull(clientRegistry.descriptors);       
        assertTrue(clientRegistry.descriptors.containsKey("builtin"));
    }
    
    
    public void testCallConfiguration()  throws Exception{
        Configuration conf = (Configuration)clientRegistry.getService(Configuration.class);
        assertNotNull(conf);         
        assertNotSame(conf,serverRegistry.getService(Configuration.class));             
        Map m = conf.list();
        assertNotNull(m);
    }
    
    public void testGetAndCallComponent() throws Exception {
        WebServer ws = (WebServer)clientRegistry.getService(WebServer.class);
        assertNotNull(ws);
        assertNotSame(ws,serverRegistry.getService(WebServer.class));             
        assertNotNull(ws.getKey());
    }
    
    

    public void testGetModule() {
        DefaultModule m = (DefaultModule)clientRegistry.getModule("builtin");
        assertNotNull(m);
        assertNotNull(m.getDescriptor().getComponent("modules"));
    }
    
    public void testGetModuleIterator() {
        Iterator i = clientRegistry.moduleIterator();
        assertNotNull(i);
        assertTrue(i.hasNext());
    }
    

    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(RmiLiteACRTest.class));
    }
}


/* 
$Log: RmiLiteACRTest.java,v $
Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/