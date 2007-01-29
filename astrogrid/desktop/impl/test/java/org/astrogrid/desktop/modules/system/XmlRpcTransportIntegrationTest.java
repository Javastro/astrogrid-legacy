/*$Id: XmlRpcTransportIntegrationTest.java,v 1.7 2007/01/29 10:42:48 nw Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
import org.astrogrid.io.Piper;

/** tests xmlrpc transport
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class XmlRpcTransportIntegrationTest extends InARTestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
            super.setUp();
            reg = getACR();
            WebServer serv = (WebServer)reg.getService(WebServer.class);
            assertNotNull(serv);
            client = new XmlRpcClient(serv.getUrlRoot() + "xmlrpc");
            v = new Vector();
        }
        protected ACR reg;
        protected XmlRpcClient client;
        protected Vector v ;
        protected void tearDown() throws Exception {
        	super.tearDown();
        	reg = null;
        	client = null;
        	v = null;
        }

    public void testInstrospection1() throws XmlRpcException, IOException {
        List results = (List)client.execute("system.listMethods",new Vector());
        assertNotNull(results);
        assertTrue(results.contains("system.configuration.list"));
    }
    
    public void testInstrospection2() throws XmlRpcException, IOException {
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
        v.add("system.configuration.setKey");
        String results = (String)client.execute("system.methodHelp",v);
        assertNotNull(results);
    }
    
    public void testSetGetRemove() throws XmlRpcException, IOException {
    	v.add("xmltest");
        String val = Integer.toString((new Random()).nextInt());
        v.add(val);
        Object o = client.execute("system.configuration.setKey",v);
        assertNotNull(o);
        assertTrue(o instanceof Boolean);
        
        v.clear();        
        v.add("xmltest");
        String results = (String)client.execute("system.configuration.getKey",v);
        assertEquals(val,results);
        
        
        // tests a method returning null
        v.clear();
        v.add("xmltest");
        o = client.execute("system.configuration.removeKey",v);
        assertNotNull(o);
        assertTrue(o instanceof String);
        assertEquals("OK",o);
        
    }
   
    public void testCheckedException() throws IOException {
    	try {
    	Object o = client.execute("test.transporttest.throwCheckedException",v);
    	fail("expected to puke");
    	} catch (XmlRpcException e){
    		assertTrue(e.getMessage().indexOf("NotFound") != -1);
    		assertNull(e.getCause()); // pity - but too much to hope for really.
    	}
    }
    
    public void testUncheckedException() throws IOException {
    	try {
        	Object o = client.execute("test.transporttest.throwUncheckedException",v);
        	fail("expected to puke");
        	} catch (XmlRpcException e){
        		assertTrue(e.getMessage().indexOf("NullPointer") != -1);
        		assertNull(e.getCause()); // pity - but too much to hope for really.
        	}    
        }
    
    public void testUncheckedUnknownException() throws IOException {
    	try {
        	Object o = client.execute("test.transporttest.throwUncheckedExceptionOfUnknownType",v);
        	fail("expected to puke");
        	} catch (XmlRpcException e){
   
        		System.out.println(e.getMessage());
        		assertTrue(e.getMessage().indexOf("AnUnknownRuntimeException") != -1);
        		assertNull(e.getCause()); // pity - but too much to hope for really.
        	}    
        }

    public void testByteArrayTransport() throws XmlRpcException, IOException {
    	byte[] bytes = "fred".getBytes();
    	v.add(bytes);
        Object o = client.execute("test.transporttest.echoByteArray",v);
        assertNotNull(o);
        assertTrue(o instanceof byte[]);
        assertNotSame(bytes,o);
        assertTrue(Arrays.equals(bytes,(byte[])o));
    }
    


    public void testCallUnknownMethod() throws  IOException {
    	try {    		
    		client.execute("unknownMethod",v);
    		fail("expected to fail");
    	} catch (XmlRpcException e) {
    		// ok. should we test what message is in the exception? naah.
    	}
    }
    

    public void testMethodWithWrongNumberOfParams() throws IOException {
        try {
        	client.execute("system.apihelp.listComponentsOfModule",v);
        	fail("expected to chuck");
        } catch (XmlRpcException e) {
        	assertTrue(e.getMessage().indexOf("Incorrect number of parameters") != -1);
        }

    }
    

    
    /* not really possible - as makes a best effort to convert
    public void testMethodWithWrongTypeOfParams() throws XmlRpcException, IOException {
        v.add(new Integer(42));
        List results = (List)client.execute("system.methodSignature",v);
        assertNotNull(results);
        assertEquals(1,results.size());
        List l = (List)results.get(0);
        assertEquals(3, l.size());
        assertEquals("string",l.get(1));
        assertEquals("string",l.get(2));
    }
    */
    
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(XmlRpcTransportIntegrationTest.class));
    }
}


/* 
$Log: XmlRpcTransportIntegrationTest.java,v $
Revision 1.7  2007/01/29 10:42:48  nw
tidied.

Revision 1.6  2007/01/23 20:07:33  nw
fixes to use subclass of finder, and to work in a hub setting.

Revision 1.5  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.4  2007/01/09 16:12:20  nw
improved tests - still need extending though.

Revision 1.3  2006/08/31 21:07:58  nw
testing of transport of rtesource beans.

Revision 1.2  2006/08/15 10:30:58  nw
tests related to new registry objects.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/