/*$Id: AllTests.java,v 1.5 2004/03/03 01:13:42 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("JES Tests");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(InMemorySystemTest.class));
        suite.addTest(new TestSuite(JESTest.class));
        suite.addTest(new TestSuite(LocalSOAPSystemTest.class));
        //$JUnit-END$
        suite.addTest(org.astrogrid.jes.delegate.AllTests.suite());
        suite.addTest(org.astrogrid.jes.jobcontroller.AllTests.suite());
        suite.addTest(org.astrogrid.jes.delegate.impl.AllTests.suite());
        suite.addTest(org.astrogrid.jes.impl.workflow.AllTests.suite());
        suite.addTest(org.astrogrid.jes.jobmonitor.AllTests.suite());
        suite.addTest(org.astrogrid.jes.jobscheduler.AllTests.suite());
        suite.addTest(org.astrogrid.jes.comm.AllTests.suite());
        suite.addTest(org.astrogrid.jes.component.AllTests.suite());
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.5  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.4  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.3.2.3  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.3.2.2  2004/02/17 12:57:11  nw
improved documentation

Revision 1.3.2.1  2004/02/12 01:13:31  nw
removed tests that test noddy classes - like exceptions - only get in the way

Revision 1.3  2004/02/09 11:51:24  nw
fixed glitch

Revision 1.2  2004/02/06 12:39:37  nw
merged in nww-it05-bz#85a branch (trim down unit tests)

Revision 1.1.2.1  2004/02/06 11:11:15  nw
removed dummy tests.
got other tests workinig.
added test suites to tie them all together
 
*/