/*$Id: InProcessFinderSeparateTest.java,v 1.6 2009/03/26 18:01:22 nw Exp $
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

import java.util.Map;
import java.util.prefs.BackingStoreException;

import junit.framework.TestCase;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.TestingFinder;

/** test finders ability to start off an acr instance.
 * is a 'separate test' - can't be run in conjunction with other integration tests (although that's what it really is)
 * because this test needs to create and destroy it's own acr instance.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 28-Jul-2005
 *@TEST get this working.
 */
public class InProcessFinderSeparateTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        finder = new TestingFinder();
        acr = finder.find();
    }
    
    protected ACR acr;
    protected Finder finder;
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Shutdown sh =(Shutdown) acr.getService(Shutdown.class);
        sh.halt();
        acr = null;
        finder = null;
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
$Log: InProcessFinderSeparateTest.java,v $
Revision 1.6  2009/03/26 18:01:22  nw
added override annotations

Revision 1.5  2008/04/23 11:32:43  nw
marked as needing tests.

Revision 1.4  2007/01/29 10:38:40  nw
documentation fixes.

Revision 1.3  2007/01/23 20:07:33  nw
fixes to use subclass of finder, and to work in a hub setting.

Revision 1.2  2007/01/23 11:53:37  nw
cleaned up tests, organized imports, commented out or fixed failing tests.

Revision 1.1  2007/01/09 16:12:21  nw
improved tests - still need extending though.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/