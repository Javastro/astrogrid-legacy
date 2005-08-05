/*$Id: ConfigurationRpcTest.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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
import java.util.Map;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class ConfigurationRpcTest extends TestCase {
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
        return new ACRTestSetup(new TestSuite(ConfigurationRpcTest.class));
    }
    

    public void testGetSetRemove() throws Exception{
        setKey("test","x");
        assertEquals("x",getKey("test"));
        removeKey("test");
        assertEquals("NULL",getKey("test")); // cant return null by xmlrpc. pity.
    }
    
    /**
     * @param string
     */
    private void removeKey(String string) throws Exception{
        v.clear();
        v.add(string);
        client.execute("system.configuration.removeKey",v);
    }


    /**
     * @param string
     * @param string2
     */
    private void setKey(String string, String string2) throws Exception{
        v.clear();
        v.add(string);
        v.add(string2);
        client.execute("system.configuration.setKey",v);
    }


    /**
     * @param string
     * @return
     */
    private String getKey(String string)  throws Exception{
        v.clear();
        v.add(string);
        return (String)client.execute("system.configuration.getKey",v);
    }


    public void testList() throws Exception {
        Map m = list();
        assertNotNull(m);
    }
    
    /**
     * @return
     */
    private Map list() throws Exception{
        v.clear();
        return (Map)client.execute("system.configuration.list",v);
    }


    public void testListKeys() throws Exception {
        List keys = listKeys();
        assertNotNull(keys);
        assertTrue(keys.size() > 0);
    }


    /**
     * @return
     */
    private List listKeys() throws Exception {
        v.clear();
        return (List)client.execute("system.configuration.listKeys",v);
    }
}


/* 
$Log: ConfigurationRpcTest.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/