/*$Id: AllTests.java,v 1.2 2005/03/11 13:36:22 clq2 Exp $
 * Created on 10-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.filemanager.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2005
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.filemanager.integration");
        //$JUnit-BEGIN$
        suite.addTestSuite(NodeDelegateTest.class);
        suite.addTestSuite(FileManagerClientTest.class);
        suite.addTestSuite(NodeTest.class);
        suite.addTestSuite(FileManagerTreeClientTest.class);
        suite.addTestSuite(FIleManagerAladinAdapterTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.2  2005/03/11 13:36:22  clq2
 with merges from filemanager

 Revision 1.1.2.1  2005/03/10 19:32:49  nw
 bunch of tests for filemanager
 
 */