/*$Id: AllTests.java,v 1.3 2004/04/08 14:50:54 nw Exp $
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
        TestSuite suite = new TestSuite("Test for org.astrogrid.workflow.integration");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(AbstractTestForIntegration.class));
        suite.addTest(new TestSuite(ApplicationsIntegrationTest.class));
        suite.addTest(new TestSuite(DataCenterIntegrationTest.class));
        suite.addTest(new TestSuite(JesIntegrationTest.class));
        suite.addTest(new TestSuite(MySpaceIntegrationTest.class));
        suite.addTest(new TestSuite(RegistryIntegrationTest.class));
        suite.addTest(new TestSuite(WorkflowEndToEndTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.3  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.2  2004/04/06 12:08:30  nw
fixes

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/