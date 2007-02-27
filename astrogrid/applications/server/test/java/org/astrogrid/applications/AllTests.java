/*
 * $Id: AllTests.java,v 1.6 2007/02/27 16:07:41 gtr Exp $
 * 
 * Created on 23-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * Runs all the important tests in the applications project. Useful for running from eclipse.
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests forCEA System");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(StatusTest.class));
        suite.addTest(new TestSuite(AbstractApplicationTest.class));
        suite.addTest(new TestSuite(DefaultIDsTest.class));
        //$JUnit-END$
        suite.addTest(org.astrogrid.applications.description.exception.AllTests.suite());
        suite.addTest(org.astrogrid.applications.description.registry.AllTests.suite());
        suite.addTest(org.astrogrid.applications.component.AllTests.suite());
        suite.addTest(org.astrogrid.applications.manager.AllTests.suite());
        suite.addTest(org.astrogrid.applications.manager.idgen.AllTests.suite());
        suite.addTest(org.astrogrid.applications.manager.persist.AllTests.suite());
        suite.addTest(org.astrogrid.applications.parameter.protocol.AllTests.suite());
        return suite;
    }
}
