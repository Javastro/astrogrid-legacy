/*$Id: AllTests.java,v 1.3 2004/02/27 00:46:03 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobcontroller;
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
        TestSuite suite = new TestSuite("Tests for jobcontroller service");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(SubmitSuccessTest.class));
        suite.addTest(new TestSuite(SubmitNotifierFailureTest.class));
        suite.addTest(new TestSuite(SubmitStoreFailureTest.class));
        suite.addTest(new TestSuite(ListJobsSuccessTest.class));
        suite.addTest(new TestSuite(ListJobsStoreFailureTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.4.2  2004/02/17 16:51:02  nw
thorough unit testing for job controller

Revision 1.2.4.1  2004/02/12 01:13:31  nw
removed tests that test noddy classes - like exceptions - only get in the way

Revision 1.2  2004/02/06 12:39:37  nw
merged in nww-it05-bz#85a branch (trim down unit tests)

Revision 1.1.2.1  2004/02/06 11:11:15  nw
removed dummy tests.
got other tests workinig.
added test suites to tie them all together
 
*/