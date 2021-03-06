/*$Id: AllTests.java,v 1.2 2004/11/05 16:52:42 jdt Exp $
 * Created on 04-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.jes.util;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Mar-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.util");
        //$JUnit-BEGIN$
        suite.addTestSuite(TemporaryBufferTest.class);
        suite.addTestSuite(JesUtilsTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.2  2004/11/05 16:52:42  jdt
 Merges from branch nww-itn07-scratchspace

 Revision 1.1.118.1  2004/11/05 15:47:34  nw
 tests for static buffer, and resources

 Revision 1.1  2004/03/05 16:16:55  nw
 worked now object model through jes.
 implemented basic scheduling policy
 removed internal facade
 
 */