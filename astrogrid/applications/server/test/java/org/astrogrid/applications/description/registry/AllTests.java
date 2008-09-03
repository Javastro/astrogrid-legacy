/*
 * $Id: AllTests.java,v 1.4 2008/09/03 14:19:00 pah Exp $
 * 
 * Created on 30-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.description.registry;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 30-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class AllTests {
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.applications.description.registry");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(RegistryEntryBuilderTest.class));
        suite.addTest(new TestSuite(RegistryUploaderTest.class));
        suite.addTest(new TestSuite(IvornUtilTest.class));
        //$JUnit-END$
        return suite;
    }
}
