/*$Id: JesDelegateFactoryTest.java,v 1.3 2004/03/05 16:16:55 nw Exp $
 * Created on 17-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate;

import org.astrogrid.jes.delegate.impl.JobControllerDelegateImpl;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegateImpl;
import org.astrogrid.jes.delegate.impl.TestJobControllerDelegateImpl;
import org.astrogrid.jes.delegate.impl.TestJobMonitorDelegateImpl;

import junit.framework.TestCase;

/** Exercise creation methods of jesDelegateFactory 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public class JesDelegateFactoryTest extends TestCase {
    /**
     * Constructor for JesDelegateFactoryTest.
     * @param arg0
     */
    public JesDelegateFactoryTest(String arg0) {
        super(arg0);
    }
    private static String DUMMY_ENDPOINT = "http://www.nowhere.org/services/Dummy";
    /*
     * Test for JobController createJobController(String)
     */
    public void testCreateJobControllerTest() {
        Delegate del = JesDelegateFactory.createJobController(Delegate.TEST_URI,0);
        assertNotNull(del);
        assertEquals(Delegate.TEST_URI,del.getTargetEndPoint());
        assertTrue(del instanceof TestJobControllerDelegateImpl);
    }
    /*
     * Test for JobMonitor createJobMonitor(String)
     */
    public void testCreateJobMonitorTest() {
        Delegate del = JesDelegateFactory.createJobMonitor(Delegate.TEST_URI,0);
        assertNotNull(del);
        assertEquals(Delegate.TEST_URI,del.getTargetEndPoint());
        assertTrue(del instanceof TestJobMonitorDelegateImpl);        
    }


    /*
     * Test for JobController createJobController(String)
     */
    public void testCreateJobController() {
        Delegate del = JesDelegateFactory.createJobController(DUMMY_ENDPOINT);
        assertNotNull(del);
        assertEquals(DUMMY_ENDPOINT,del.getTargetEndPoint());
        assertTrue(del instanceof JobControllerDelegateImpl);
    }
    /*
     * Test for JobMonitor createJobMonitor(String)
     */
    public void testCreateJobMonitor() {
        Delegate del = JesDelegateFactory.createJobMonitor(DUMMY_ENDPOINT);
        assertNotNull(del);
        assertEquals(DUMMY_ENDPOINT,del.getTargetEndPoint());
        assertTrue(del instanceof JobMonitorDelegateImpl);        
    }
   
}


/* 
$Log: JesDelegateFactoryTest.java,v $
Revision 1.3  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.2  2004/02/17 12:57:11  nw
improved documentation

Revision 1.1.2.1  2004/02/17 12:41:06  nw
added test for JesDelegateFactory
 
*/