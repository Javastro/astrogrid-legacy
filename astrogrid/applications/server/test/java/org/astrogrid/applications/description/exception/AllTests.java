/*
 * $Id: AllTests.java,v 1.3 2008/09/03 14:19:03 pah Exp $
 * 
 * Created on 11-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.description.exception;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.applications.description.exception");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ApplicationDescriptionNotFoundExceptionTest.class));
        suite.addTest(new TestSuite(InterfaceDescriptionNotFoundExceptionTest.class));
        suite.addTest(new TestSuite(ParameterNotInInterfaceExceptionTest.class));
        suite.addTest(new TestSuite(ParameterTypeNotDefinedExceptionTest.class));
        //$JUnit-END$
        return suite;
    }
}
