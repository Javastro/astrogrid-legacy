/*$Id: AllTests.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.impl.workflow");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(AbstractJobFactoryImplTest.class));
        suite.addTest(DBJobFactoryImplTest.suite());
        suite.addTest(FileJobFactoryImplTest.suite());
        suite.addTest(InMemoryJobFactoryImplTest.suite());
        suite.addTest(new TestSuite(JobImplTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/17 12:57:11  nw
improved documentation
 
*/