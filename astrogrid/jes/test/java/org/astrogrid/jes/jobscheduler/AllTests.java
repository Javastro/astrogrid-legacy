/*$Id: AllTests.java,v 1.4 2004/03/15 00:32:01 nw Exp $
 * Created on 19-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;
import org.astrogrid.jes.jobscheduler.impl.*;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.jobscheduler");
        //$JUnit-BEGIN$
        //$JUnit-END$
        suite.addTest(org.astrogrid.jes.jobscheduler.locator.AllTests.suite());
        suite.addTest(org.astrogrid.jes.jobscheduler.dispatcher.AllTests.suite());
        suite.addTest(org.astrogrid.jes.jobscheduler.impl.AllTests.suite());
        suite.addTest(org.astrogrid.jes.jobscheduler.policy.AllTests.suite());
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.4  2004/03/15 00:32:01  nw
merged contents of comm package into jobscheduler package.

Revision 1.3  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/