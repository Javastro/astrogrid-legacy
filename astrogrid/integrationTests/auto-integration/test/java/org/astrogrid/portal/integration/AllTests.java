/*$Id: AllTests.java,v 1.2 2004/06/07 14:39:16 jdt Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 *
 */
public class AllTests {
    /**
     * Hide
     * Constructor
     *
     */
    private AllTests() {
    }
    /**
     * Fire me up
     * @param args ignored
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    /**
     * Get the test suite of all portal tests
     * @return test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Portal Tests");
        //$JUnit-BEGIN$
        suite.addTestSuite(RegisterPageTest.class); 
        suite.addTestSuite(PortalLoginPageTest.class);
        suite.addTestSuite(PortalLoginPageHttpUnitTest.class);
        suite.addTestSuite(ReminderPageTest.class);
        suite.addTestSuite(CommunityPageNewUser.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.2  2004/06/07 14:39:16  jdt
added new tests

Revision 1.1  2004/04/15 14:03:51  nw
draws things together
 
*/