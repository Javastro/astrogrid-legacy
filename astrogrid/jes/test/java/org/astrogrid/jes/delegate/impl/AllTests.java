/*$Id: AllTests.java,v 1.3 2004/03/05 16:16:55 nw Exp $
 * Created on 09-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate.impl;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.delegate.impl");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(AbstractDelegateTest.class));
        suite.addTest(new TestSuite(SOAPJobMonitorTest.class));
        suite.addTest(new TestSuite(SOAPJobControllerTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.3  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/09 11:19:28  nw
end of this branch - have a set of oo-based delegates.
not ready for use until innards are updated too.
 
*/