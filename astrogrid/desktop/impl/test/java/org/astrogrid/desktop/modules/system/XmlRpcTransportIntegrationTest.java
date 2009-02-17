/*$Id: XmlRpcTransportIntegrationTest.java,v 1.11 2009/02/17 13:46:33 nw Exp $
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



import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.astrogrid.Fixture;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
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
            client = Fixture.createXmlRpcClient(getACR());
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
        final Object[] results = (Object[])client.execute("system.listMethods",new Vector());
        assertNotNull(results);
        assertThat(results,hasItemInArray((Object)"system.configuration.list"));
    }
    
    public void testInstrospection2() throws XmlRpcException, IOException {
        v.add("system.configuration.setKey");
        final Object[] results = (Object[]) client.execute("system.methodSignature",v);
        assertNotNull(results);
        assertEquals(1,results.length);
        final Object[] l = (Object[])results[0];
        assertEquals(3, l.length);
        assertEquals("String",l[1]);
        assertEquals("String",l[2]);
    }
    
    public void testInstrospection3() throws XmlRpcException, IOException {
        v.add("system.configuration.setKey");
        final String results = (String)client.execute("system.methodHelp",v);
        assertNotNull(results);
    }
    
    public void testSetGetRemove() throws XmlRpcException, IOException {
    	v.add("xmltest");
        final String val = Integer.toString((new Random()).nextInt());
        v.add(val);
        Object o = client.execute("system.configuration.setKey",v);
        assertNotNull(o);
        assertThat(o,is(Boolean.class));
        
        v.clear();        
        v.add("xmltest");
        final String results = (String)client.execute("system.configuration.getKey",v);
        assertEquals(val,results);
        
        
        // tests a method returning null
        v.clear();
        v.add("xmltest");
        o = client.execute("system.configuration.removeKey",v);
        assertNotNull(o);
        assertThat(o, is(String.class));
        assertEquals("OK",o);
        
    }
   
    public void testCheckedException() throws IOException {
    	try {
    	final Object o = client.execute("test.transporttest.throwCheckedException",v);
    	fail("expected to puke");
    	} catch (final XmlRpcException e){
    	    assertThat(e.getMessage(),containsString("NotFound"));
    	    assertNull(e.getCause()); // pity - but too much to hope for really.
    	}
    }
    
    public void testUncheckedException() throws IOException {
    	try {
        	final Object o = client.execute("test.transporttest.throwUncheckedException",v);
        	fail("expected to puke");
        	} catch (final XmlRpcException e){
                assertThat(e.getMessage(),containsString("NullPointer"));
        		assertNull(e.getCause()); // pity - but too much to hope for really.
        	}    
        }
    
    public void testUncheckedUnknownException() throws IOException {
    	try {
        	final Object o = client.execute("test.transporttest.throwUncheckedExceptionOfUnknownType",v);
        	fail("expected to puke");
        	} catch (final XmlRpcException e){
                assertThat(e.getMessage(),containsString("AnUnknownRuntimeException"));        		
        		assertNull(e.getCause()); // pity - but too much to hope for really.
        	}    
        }

    public void testByteArrayTransport() throws XmlRpcException, IOException {
    	final byte[] bytes = "fred".getBytes();
    	v.add(bytes);
        final Object o = client.execute("test.transporttest.echoByteArray",v);
        assertNotNull(o);
        assertThat(o,is(byte[].class));
        assertNotSame(bytes,o);
        assertTrue(Arrays.equals(bytes,(byte[])o));
    }

    /** same as previous, but passing a string parameter where a binary parameter is expected */
    public void testByteArrayTransportStringInput() throws XmlRpcException, IOException {       
        v.add("fred");
        final Object o = client.execute("test.transporttest.echoByteArray",v);
        assertNotNull(o);
        assertThat(o,is(byte[].class));
        assertTrue(Arrays.equals("fred".getBytes(),(byte[])o));
    }


    public void testCallUnknownMethod() throws  IOException {
    	try {    		
    		client.execute("unknownMethod",v);
    		fail("expected to fail");
    	} catch (final XmlRpcException e) {
    		// ok. should we test what message is in the exception? naah.
    	}
    }
    

    public void testMethodWithWrongNumberOfParams() throws IOException {
        try {
        	client.execute("system.apihelp.listComponentsOfModule",v);
        	fail("expected to chuck");
        } catch (final XmlRpcException e) {
            assertThat(e.getMessage(),containsString("Incorrect number of parameters"));   
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
Revision 1.11  2009/02/17 13:46:33  nw
Complete - taskfix http input of binary parameters.

Revision 1.10  2008/12/22 18:18:18  nw
improved in-program API help.

Revision 1.9  2008/08/04 16:37:24  nw
Complete - task 441: Get plastic upgraded to latest XMLRPC

Complete - task 430: upgrade to latest xmlrpc lib

Revision 1.8  2007/04/18 15:47:04  nw
tidied up voexplorer, removed front pane.

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