/*$Id: AllTests.java,v 1.2 2003/08/22 10:37:48 nw Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class AllTests {

    public static void main(String[] args) {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.datacenter");
        //$JUnit-BEGIN$
        //suite.addTest(QueryTestSuite.suite());
        //$JUnit-END$
        // and add other suites too.
        suite.addTest(org.astrogrid.datacenter.config.AllTests.suite());
        suite.addTest(org.astrogrid.datacenter.datasetagent.AllTests.suite());
        suite.addTest(org.astrogrid.datacenter.impl.abstr.AllTests.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.2  2003/08/22 10:37:48  nw
added test hierarchy for Job / JobStep

Revision 1.1  2003/08/21 12:29:18  nw
added unit testing for factory manager hierarchy.
added 'AllTests' suite classes to draw unit tests together - single
place to run all.
 
*/