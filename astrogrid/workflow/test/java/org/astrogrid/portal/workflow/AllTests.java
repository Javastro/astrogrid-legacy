/*$Id: AllTests.java,v 1.2 2004/02/25 10:58:05 nw Exp $
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
        TestSuite suite = new TestSuite("Test for org.astrogrid.portal.workflow");
        //$JUnit-BEGIN$
        //$JUnit-END$
        suite.addTest(CardinalityTestSuite.suite());
        suite.addTest(WorkflowTestSuite.suite());
        suite.addTest(JesTestSuite.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2004/02/25 10:58:05  nw
merged in branch nww-itn05-bz#140 (refactor in preparation for changing object model)

Revision 1.1.2.1  2004/02/24 13:12:56  nw
suite to collect all tests together
 
*/