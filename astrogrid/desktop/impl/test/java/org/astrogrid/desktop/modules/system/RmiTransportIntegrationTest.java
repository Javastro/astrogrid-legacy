/*$Id: RmiTransportIntegrationTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.test.TransportTest;
import org.astrogrid.desktop.ACRTestSetup;
import org.astrogrid.desktop.framework.ACRInternal;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** tests rmi transport - whether certain classes are serializable etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class RmiTransportIntegrationTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
            super.setUp();
            reg = (new Finder()).find();
            WebServer serv = (WebServer)reg.getService(WebServer.class);
            assertNotNull(serv);
            
        }
        protected ACR reg;


    public void testCheckedException() throws InvalidArgumentException, ACRException {
    	TransportTest tt = (TransportTest)this.reg.getService(TransportTest.class);
    	assertNotNull(tt);
    	try {
    		tt.throwCheckedException();
    		fail("expected to chuck");
    	} catch (NotFoundException e) {
    		// ok.
    	}
    }
    
    public void testUncheckedException() throws InvalidArgumentException, NotFoundException, ACRException {
    	TransportTest tt = (TransportTest)this.reg.getService(TransportTest.class);
    	assertNotNull(tt);
    	try {
    		tt.throwUncheckedException();
    		fail("expected to chuck");
    	} catch (NullPointerException e) {
    		// ok.
    	}
    }
    
    public void testUncheckedUnknownException() throws InvalidArgumentException, NotFoundException, ACRException {
    	TransportTest tt = (TransportTest)this.reg.getService(TransportTest.class);
    	assertNotNull(tt);
    	try {
    		tt.throwUncheckedExceptionOfUnknownType();
    		fail("expected to chuck");
    	} catch (RuntimeException e) {
    		assertEquals(RuntimeException.class,e.getClass());
    	}
    }
    
    public void testByteArrayTransport() throws InvalidArgumentException, NotFoundException, ACRException {
    	TransportTest tt = (TransportTest)this.reg.getService(TransportTest.class);
    	assertNotNull(tt);    
    	byte[] bytes = "fred".getBytes();    
    	byte[] other = tt.echoByteArray(bytes);
    	assertNotSame(bytes,other);
    	assertTrue(Arrays.equals(bytes,other));
    }

    public static Test suite() {
        return new ACRTestSetup(new TestSuite(RmiTransportIntegrationTest.class));
    }
}


/* 
$Log: RmiTransportIntegrationTest.java,v $
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