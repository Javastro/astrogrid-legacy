/*$Id: AllTests.java,v 1.7 2004/07/30 15:42:34 nw Exp $
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
        suite.addTest(org.astrogrid.jes.jobscheduler.impl.groovy.AllTests.suite());
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.7  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.6.20.2  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.6.20.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.6  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.5  2004/07/01 21:15:00  nw
added results-listener interface to jes

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