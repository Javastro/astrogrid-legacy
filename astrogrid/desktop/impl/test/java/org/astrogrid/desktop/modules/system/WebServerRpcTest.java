/*$Id: WebServerRpcTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class WebServerRpcTest extends TestCase {
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
        return new ACRTestSetup(new TestSuite(WebServerRpcTest.class));
    }
    

    public void testGetKey() throws Exception{
        assertNotNull(getKey());
    }

    /**
     * @return
     */
    private String getKey() throws Exception{
        v.clear();
        return (String)client.execute("system.webserver.getKey",v);
    }


    public void testGetPort() throws Exception{
        assertTrue(getPort() > 0);
        
    }

    /**
     * @return
     */
    private int getPort() throws Exception{
        v.clear();
        return ((Integer)client.execute("system.webserver.getPort",v)).intValue();
    }


    public void testGetUrlRoot() throws Exception {
        assertNotNull(getUrlRoot());
        new URL(getUrlRoot()); // will throw a malformed thingie if not a url.
    }


    /**
     * @return
     */
    private String getUrlRoot()  throws Exception{
        v.clear();
        return (String)client.execute("system.webserver.getUrlRoot",v);
    }
    
    
}


/* 
$Log: WebServerRpcTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/