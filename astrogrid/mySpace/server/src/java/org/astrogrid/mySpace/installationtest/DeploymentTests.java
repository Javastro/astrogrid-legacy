/* $Id: DeploymentTests.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 * Created on 29-Dec-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.mySpace.installationtest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testsuite for the webapp installation tests.  Add to this if
 * you add more tests, since this is the suite referenced by JUnitee
 * @author john
 *
 */
public class DeploymentTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(DeploymentTests.class);
    }
    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.mySpace.webapp");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ConfFileLocatedTest.class));
        suite.addTest(new TestSuite(DatabaseLocatedTest.class));
        suite.addTest(new TestSuite(ServicesLocatedTest.class));
        //$JUnit-END$
        return suite;
    }
}
