/*$Id: AllTests.java,v 1.3 2004/03/11 13:53:51 nw Exp $
 * Created on 24-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow;

import org.astrogrid.portal.workflow.design.CardinalityTestSuite;
import org.astrogrid.portal.workflow.design.WorkflowTestSuite;
import org.astrogrid.portal.workflow.jes.JesTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for Workflow System");
        //$JUnit-BEGIN$
        //$JUnit-END$
        //suite.addTest(CardinalityTestSuite.suite());
       // suite.addTest(WorkflowTestSuite.suite());
       // suite.addTest(JesTestSuite.suite());
        suite.addTest(org.astrogrid.portal.workflow.impl.AllTests.suite());
        suite.addTest(org.astrogrid.portal.workflow.intf.AllTests.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.3  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.2.6.1  2004/03/11 13:37:52  nw
tests for impls

Revision 1.2  2004/02/25 10:58:05  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 13:12:56  nw
suite to collect all tests together
 
*/