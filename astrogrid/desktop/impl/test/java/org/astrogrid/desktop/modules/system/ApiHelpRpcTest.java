/*$Id: ApiHelpRpcTest.java,v 1.2 2006/04/18 23:25:47 nw Exp $
 * Created on 03-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.framework.ACRTestSetup;

import org.apache.xmlrpc.XmlRpcClient;

import java.util.List;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** repeat of apihelp tests, via xmlrpc interface - exercise this transport, and make sure configuration is correct
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class ApiHelpRpcTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = ACRTestSetup.pico.getACR();
        WebServer serv = (WebServer)reg.getService(WebServer.class);
        assertNotNull(serv);
        client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
        v = new Vector();
    }
    protected XmlRpcClient client;
    protected Vector v ;

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(ApiHelpRpcTest.class));
    }
    
    public void testListMethods() throws Exception {    
        List l = (List)client.execute("system.apihelp.listMethods",v);
        assertNotNull(l);
        assertTrue(l.contains("system.configuration.getKey"));
    }

    public void testListModules() throws Exception{
        List l = (List)client.execute("system.apihelp.listModules",v);
        assertNotNull(l);
        assertTrue(l.contains("system"));
    }

    public void testListComponentsOfModule() throws Exception {       
        v.add("builtin");
        List l = (List)client.execute("system.apihelp.listComponentsOfModule",v);
        assertTrue(l.contains("builtin.shutdown"));
        v.clear();
        v.add("system");
        l = (List)client.execute("system.apihelp.listComponentsOfModule",v);
        assertTrue(l.contains("system.configuration"));
    }

    public void testListMethodsOfComponent() throws Exception {
        v.add("builtin.shutdown");
        List l = (List)client.execute("system.apihelp.listMethodsOfComponent",v);
        assertTrue(l.contains("builtin.shutdown.halt"));
        v.clear();
        v.add("system.configuration");
        l = (List)client.execute("system.apihelp.listMethodsOfComponent",v);
        assertTrue(l.contains("system.configuration.list"));
    }

    public void testMethodSignature() throws Exception {
        v.add("system.configuration.list");
        List result = (List)client.execute("system.apihelp.methodSignature",v);
        assertEquals(1,result.size());
        List sig1 = (List)result.get(0);
        assertEquals(1,sig1.size());
        assertEquals("key-value map",sig1.get(0)); 
    }

    public void testModuleHelp() throws Exception {
        v.add("builtin");
       assertNotNull(client.execute("system.apihelp.moduleHelp",v));
       v.clear();
       v.add("system");
       assertNotNull(client.execute("system.apihelp.moduleHelp",v));
    }

    public void testComponentHelp() throws Exception {
        v.add("builtin.shutdown");
        assertNotNull(client.execute("system.apihelp.componentHelp",v));
        v.clear();
        v.add("system.configuration");
        assertNotNull(client.execute("system.apihelp.componentHelp",v));        
    }

    public void testMethodHelp() throws Exception {
        v.add("system.configuration.getKey");
        assertNotNull(client.execute("system.apihelp.methodHelp",v));
    }
    
    public void testMethodHelpForIntrospection() throws Exception {
        v.add("system.listMethods");
        assertNotNull(client.execute("system.apihelp.methodHelp",v));        
    }
    
}


/* 
$Log: ApiHelpRpcTest.java,v $
Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/