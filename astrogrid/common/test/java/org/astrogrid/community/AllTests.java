/* $Id: AllTests.java,v 1.1 2004/02/17 12:32:24 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.community;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 */

public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.community");
        //$JUnit-BEGIN$
        suite.addTest(AccountTest.suite());
        suite.addTest(UserTest.suite());
        //$JUnit-END$
        return suite;
    }
}
