/*$Id: AllTests.java,v 1.2 2005/03/11 13:37:06 clq2 Exp $
 * Created on 10-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.client.delegate;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Feb-2005
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for org.astrogrid.filemanager.client.delegate");

        suite.addTestSuite(DirectNodeDelegateTestCase.class);
        suite.addTest(SoapNodeDelegateTestCase.suite());        
        suite.addTestSuite(DirectNodeTestCase.class);
        suite.addTest(SoapNodeTestCase.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:38  nw
split code inito client and server projoects again.

Revision 1.1.2.3  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)

Revision 1.1.2.2  2005/02/11 14:30:23  nw
changes to follow source refactoring - tests themselves may
need to be refactoed later

Revision 1.1.2.1  2005/02/10 16:24:17  nw
formatted code, added AllTests that draw all tests together for easy execution from IDE
 
*/