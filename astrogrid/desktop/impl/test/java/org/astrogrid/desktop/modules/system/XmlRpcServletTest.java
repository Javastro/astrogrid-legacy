/*$Id: XmlRpcServletTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 25-Jul-2005
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
import org.astrogrid.desktop.framework.MutableACR;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class XmlRpcServletTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
            super.setUp();
            reg = ACRTestSetup.pico.getACR();
            WebServer serv = (WebServer)reg.getService(WebServer.class);
            assertNotNull(serv);
            client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
            
        }
        protected ACR reg;
        protected XmlRpcClient client;


    public void testInstrospection1() throws XmlRpcException, IOException {
        List results = (List)client.execute("system.listMethods",new Vector());
        assertNotNull(results);
        assertTrue(results.contains("system.configuration.list"));
    }
    
    public void testInstrospection2() throws XmlRpcException, IOException {
        Vector v = new Vector();
        v.add("system.configuration.setKey");
        List results = (List)client.execute("system.methodSignature",v);
        assertNotNull(results);
        assertEquals(1,results.size());
        List l = (List)results.get(0);
        assertEquals(3, l.size());
        assertEquals("string",l.get(1));
        assertEquals("string",l.get(2));
    }
    
    public void testInstrospection3() throws XmlRpcException, IOException {
        Vector v = new Vector();
        v.add("system.configuration.setKey");
        String results = (String)client.execute("system.methodHelp",v);
        assertNotNull(results);
    }
    
    public void testSetGetRemove() throws XmlRpcException, IOException {
        Vector v = new Vector();
        v.add("xmltest");
        String val = Integer.toString((new Random()).nextInt());
        v.add(val);
        client.execute("system.configuration.setKey",v);
        v.clear();
        v.add("xmltest");
        String results = (String)client.execute("system.configuration.getKey",v);
        assertEquals(val,results);
        v.clear();
        
        
    }
    
    /** doesn't really belong here - part of the rmi stuff really
     * but can't go in existing rmi tests, as is a precursor to them.
     * @todo move to where it belongs.
     * @throws Exception
     */
    public void testSerializationOfModuleDescriptors() throws Exception {
        Map m = ((MutableACR)this.reg).getDescriptors();
        assertNotNull(m);
        for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry)i.next();
            System.out.println("Testing " + e.getKey());
            roundTrip(e.getKey());
            roundTrip(e.getValue());
        }
        roundTrip(m);        
        
    }
    
    /**
     * @param m
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void roundTrip(Object m) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(m);
        os.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        Object o = ois.readObject();
        assertNotNull(o);
        assertNotSame(o,m);
        assertEquals(o,m);
    }

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(XmlRpcServletTest.class));
    }
}


/* 
$Log: XmlRpcServletTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/