/*$Id: AllTests.java,v 1.2 2004/04/08 14:47:12 nw Exp $
 * Created on 15-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.jobscheduler.impl");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ResumeJobSuccessTest.class));
        suite.addTest(new TestSuite(SchedulerTaskQueueDecoratorTest.class));
        suite.addTest(new TestSuite(SubmitNewJobNotifierFailsTest.class));
        suite.addTest(new TestSuite(SubmitNewJobSuccessTest.class));
        suite.addTest(new TestSuite(DeleteJobTest.class));
        suite.addTest(new TestSuite(AbortJobTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.2  2004/04/08 14:47:12  nw
added delete and abort job functionality

Revision 1.1  2004/03/15 00:32:01  nw
merged contents of comm package into jobscheduler package.
 
*/