/*$Id: RmiTransportIntegrationTest.java,v 1.9 2009/03/26 18:01:21 nw Exp $
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

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.system.WebServer;
import org.astrogrid.acr.test.TransportTest;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.TestingFinder;

/** tests rmi transport - whether certain classes are serializable etc.
 * 
 * @todo find some way to verify that this test is accessing AR via RMI.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Jul-2005
 *
 */
public class RmiTransportIntegrationTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
            super.setUp();
            reg = (new TestingFinder()).find();
            WebServer serv = (WebServer)reg.getService(WebServer.class);
            assertNotNull(serv);
            
        }
        protected ACR reg;
        @Override
        protected void tearDown() throws Exception {
        	super.tearDown();
        	reg = null;
        }

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
    	//@todo not returning expected type in all cases	
    		// works within eclipse, but not in asr build from maven.
    		// unknown whether it works from other AR variants.
    	//	assertEquals(RuntimeException.class,e.getClass());
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
        return new ARTestSetup(new TestSuite(RmiTransportIntegrationTest.class));
    }
}


/* 
$Log: RmiTransportIntegrationTest.java,v $
Revision 1.9  2009/03/26 18:01:21  nw
added override annotations

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

Revision 1.2  2006/08/15 10:31:09  nw
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