/*$Id: InProcessFinderIntegrationTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.system.Configuration;

import java.util.Map;
import java.util.prefs.BackingStoreException;

import junit.framework.TestCase;

/** test finders ability to start off an acr instance.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class InProcessFinderIntegrationTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        finder = new Finder();
        acr = finder.find();
    }
    
    protected ACR acr;
    protected Finder finder;
    
    protected void tearDown() throws Exception {
        super.tearDown();
        Shutdown sh =(Shutdown) acr.getService(Shutdown.class);
        sh.halt();
    }

    /** necessary to test all in one go - as otherwise we have startup / shutdown problems -
     * as next method connects via rmi to already running acr.
     * @throws BackingStoreException 
     * @throws ACRException 
     *
     */
    public void testAll() throws ACRException, BackingStoreException {
    	this.doTestInProcessFinder();
    	this.doTestSingletonACR();
    	this.doTestDuffService();
    	this.doTestUnknownService();
    	this.doTestNamedService();
    	this.doTestMisnamedService();
    }
    
    /** test we can find acr, get a sercice from it, and call that service */
    public void doTestInProcessFinder() throws ACRException, BackingStoreException {
        assertNotNull(acr);
        Configuration conf = (Configuration)acr.getService(Configuration.class);
        assertNotNull(conf);
        Map m = conf.list();
        assertNotNull(m);
        assertTrue(m.size() > 0);
    }
    
    public void doTestSingletonACR() throws ACRException {
        ACR acr1 = finder.find();
        assertSame(acr,acr1);
    }
    
    public void doTestDuffService() throws  ACRException {
    	try {
    		acr.getService(Object.class);
    		fail("Expected to fail");
    	} catch(NotFoundException e) {
    		// ok
    	}
    }
    
    public void doTestUnknownService() throws ACRException {
    	try {
    		acr.getService(Runnable.class);
    		fail("Expected to fail");
    	} catch(NotFoundException e) {
    		// ok
    	}
    }
    
    public void doTestNamedService() throws InvalidArgumentException, NotFoundException, ACRException {
    	Object o = acr.getService("system.configuration");
    	assertNotNull(o);
    	assertTrue(o instanceof Configuration);
    }
    
    public void doTestMisnamedService() throws ACRException {
    	try {
    		acr.getService("unknown.service");
    		fail("Expected to fail");
    	} catch(NotFoundException e) {
    		// ok
    	}
    }
 

}


/* 
$Log: InProcessFinderIntegrationTest.java,v $
Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/