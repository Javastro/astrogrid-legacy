/*$Id: AllTests.java,v 1.8 2004/04/15 23:11:20 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Workflow");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ApplicationsInstallationTest.class));
        suite.addTest(new TestSuite(DataCenterIntegrationTest.class));
        suite.addTest(new TestSuite(JesInstallationTest.class));
        suite.addTest(new TestSuite(MySpaceIntegrationTest.class));
        suite.addTest(new TestSuite(RegistryIntegrationTest.class));
        suite.addTest(new TestSuite(WorkflowEndToEndTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.8  2004/04/15 23:11:20  nw
tweaks

Revision 1.7  2004/04/15 13:02:38  jdt
whoops.  I must have caught this one mid cvs commit while
it was out of synch with the other tests.

Revision 1.6  2004/04/15 12:38:24  jdt
Think I've fixed a typo?

Revision 1.5  2004/04/15 12:12:28  nw
testing

Revision 1.4  2004/04/14 10:16:40  nw
added to the workflow integration tests

Revision 1.3  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.2  2004/04/06 12:08:30  nw
fixes

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/