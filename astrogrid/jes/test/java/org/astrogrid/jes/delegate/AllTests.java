/*$Id: AllTests.java,v 1.3 2004/02/27 00:46:03 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.delegate");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(JesDelegateExceptionTest.class));
        suite.addTest(new TestSuite(JesDelegateFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.4.1  2004/02/17 12:41:06  nw
added test for JesDelegateFactory

Revision 1.2  2004/02/06 12:39:37  nw
merged in nww-it05-bz#85a branch (trim down unit tests)

Revision 1.1.2.1  2004/02/06 11:11:15  nw
removed dummy tests.
got other tests workinig.
added test suites to tie them all together
 
*/