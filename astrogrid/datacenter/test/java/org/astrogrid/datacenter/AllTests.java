/*$Id: AllTests.java,v 1.7 2003/08/29 15:26:55 mch Exp $
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

    /**
     * Runs the full set.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
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
        suite.addTest(org.astrogrid.datacenter.query.QueryParsingTest.suite());
        suite.addTest(org.astrogrid.datacenter.queriers.QuerierTest.suite());
        suite.addTest(org.astrogrid.datacenter.service.WorkspaceTest.suite());
        suite.addTest(org.astrogrid.datacenter.service.ServerTest.suite());
        suite.addTest(org.astrogrid.datacenter.delegate.DelegateTest.suite());
        suite.addTest(org.astrogrid.datacenter.adql.AllTests.suite());
        return suite;
    }
}


/*
$Log: AllTests.java,v $
Revision 1.7  2003/08/29 15:26:55  mch
Renamed TestXxxx to XxxxxTest so Maven runs them

Revision 1.6  2003/08/28 15:26:44  nw
unit tests for adql

Revision 1.4  2003/08/27 23:57:55  mch
added workspace and delegate tests

Revision 1.3  2003/08/26 23:37:11  mch
Added tests

Revision 1.2  2003/08/22 10:37:48  nw
added test hierarchy for Job / JobStep

Revision 1.1  2003/08/21 12:29:18  nw
added unit testing for factory manager hierarchy.
added 'AllTests' suite classes to draw unit tests together - single
place to run all.

*/
