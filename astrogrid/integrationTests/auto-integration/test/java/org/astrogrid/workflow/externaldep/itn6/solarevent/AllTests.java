/*$Id: AllTests.java,v 1.1 2004/08/17 15:38:23 nw Exp $
 * Created on 17-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.workflow.externaldep.itn6.solarevent;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(
            "Test for org.astrogrid.workflow.externaldep.itn6.solarevent");
        //$JUnit-BEGIN$
        suite.addTestSuite(CompositeFitsVotableParsingConcatWorkflowTest.class);
        suite.addTestSuite(SimpleMovieMakerWorkflowTest.class);
        suite.addTestSuite(SimpleSECWorkflowTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.1  2004/08/17 15:38:23  nw
 set of integration tests that require external resources.
 
 */