/*$Id: AllTests.java,v 1.1 2009/05/13 13:20:57 gtr Exp $
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
        TestSuite suite = new TestSuite("Test for Datacenter Server");

        suite.addTest(org.astrogrid.datacenter.queriers.AllTests.suite());
        suite.addTest(org.astrogrid.datacenter.service.AllTests.suite());
        suite.addTest(org.astrogrid.datacenter.queriers.sql.AllTests.suite());
     //    suite.addTest(org.astrogrid.datacenter.queriers.fits.FitsQuerierTest.suite());
        return suite;
    }
}


/*
$Log: AllTests.java,v $
Revision 1.1  2009/05/13 13:20:57  gtr
*** empty log message ***

Revision 1.3  2005/03/10 13:59:00  KevinBenson
corrections to fits and testing to fits

Revision 1.2  2005/02/28 18:47:05  mch
More compile fixes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.12  2004/03/12 04:54:06  mch
It05 MCH Refactor

Revision 1.11  2004/03/03 23:25:51  mch
Removed DirectDelegate

Revision 1.10  2004/01/15 18:06:05  nw
added other tests lurking around

Revision 1.9  2004/01/15 17:39:26  nw
removed dodgy cruft.

Revision 1.8  2004/01/15 16:41:44  nw
removed duplicate test

Revision 1.7  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.6  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.5  2003/11/24 21:04:54  nw
fixed test linking
tidied up database connection JNDI lookup

Revision 1.4  2003/11/21 18:18:30  mch
Added client tests to server to get complete tests for server

Revision 1.3  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.14  2003/11/13 12:49:17  mch
Removed config test (now in common)

Revision 1.13  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.12  2003/09/19 12:02:37  nw
Added top level test - runs integration tests against an inprocess db and inprocess axis.

Revision 1.11  2003/09/11 13:28:24  nw
added xml creation tests

Revision 1.10  2003/09/05 12:05:42  mch
Removed tests on removed classes

Revision 1.9  2003/09/05 01:06:09  nw
linked in new tests

Revision 1.8  2003/09/02 14:41:39  nw
added test

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
