/*$Id: AllTests.java,v 1.4 2004/11/19 10:27:29 clq2 Exp $
 * Created on 12-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.workflow.integration.itn6.solarevent;
import org.astrogrid.workflow.externaldep.itn6.solarevent.CompositeFitsVotableParsingConcatWorkflowTest;
import org.astrogrid.workflow.externaldep.itn6.solarevent.SimpleSECWorkflowTest;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(
            "Integration Tests for solarevent science case");
        //$JUnit-BEGIN$
        suite.addTestSuite(ExampleVOTableParsingWorkflowTest.class);
        suite.addTestSuite(SimpleFitsWorkflowTest.class);
        suite.addTestSuite(SimpleConcatToolWorkflowTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.4  2004/11/19 10:27:29  clq2
 nww-itn07-659

 Revision 1.3.66.1  2004/11/18 10:52:01  nw
 javadoc, some very minor tweaks.

 Revision 1.3  2004/08/17 15:38:53  nw
 tests for solar event science case.

 Revision 1.2  2004/08/17 13:36:18  nw
 regenerated

 Revision 1.1  2004/08/12 13:33:34  nw
 added framework of classes for testing the solar event science case.
 
 */