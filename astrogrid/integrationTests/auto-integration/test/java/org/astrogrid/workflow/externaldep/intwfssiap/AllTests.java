/*$Id: AllTests.java,v 1.2 2004/11/30 15:39:32 clq2 Exp $
 * Created on 25-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.workflow.externaldep.intwfssiap;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Nov-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for INTWFS SIAP");
        //$JUnit-BEGIN$
        suite.addTestSuite(SimpleINTWFSSiapWorkflowTest.class);
        suite.addTestSuite(RealSiapImageFetchUsingScriptTest.class);
        suite.addTestSuite(RealSiapImageFetchUsingTool.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.2  2004/11/30 15:39:32  clq2
 nww-itn07-684b

 Revision 1.1.2.2  2004/11/26 15:46:01  nw
 tests for the siap, siap-image-fetch tool, and equivalent script

 Revision 1.1.2.1  2004/11/25 00:36:36  nw
 tests for external siap tools.
 
 */