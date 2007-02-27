/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.component;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Jun-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.applications.component");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(BaseCEAComponentManagerTest.class));
        suite.addTest(new TestSuite(CEAComponentManagerFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}