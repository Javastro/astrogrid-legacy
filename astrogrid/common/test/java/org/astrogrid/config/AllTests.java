/*$Id: AllTests.java,v 1.1 2003/10/07 22:23:11 mch Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.config;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test harness for config package
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.config");
        //$JUnit-BEGIN$
        suite.addTest(FactoryTest.suite());
        suite.addTest(SimpleTest.suite());
        suite.addTest(XmlTest.suite());
        //$JUnit-END$
        return suite;
    }
}


/*
$Log: AllTests.java,v $
Revision 1.1  2003/10/07 22:23:11  mch
Created unit tests for config package

Revision 1.4  2003/09/15 22:09:00  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.3  2003/09/15 17:39:18  mch
Better test coverage

Revision 1.2  2003/09/15 14:35:17  mch
Fixes to reading status id and service id & AllTests now tests package

Revision 1.1  2003/09/11 13:27:17  nw
added tests for xml formatting code

*/
