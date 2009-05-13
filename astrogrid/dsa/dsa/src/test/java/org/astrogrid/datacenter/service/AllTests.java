/*$Id: AllTests.java,v 1.1 2009/05/13 13:20:58 gtr Exp $
 * Created on 04-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.service");
        //$JUnit-BEGIN$
        suite.addTest(DataServiceTest.suite());
        suite.addTest(new TestSuite(MulticoneTest.class));
//        suite.addTest(org.astrogrid.datacenter.service.v041.AllTests.suite());
//        suite.addTest(org.astrogrid.datacenter.service.v05.AllTests.suite());
        //$JUnit-END$
        return suite;
    }
    
    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
    
}


/*
$Log: AllTests.java,v $
Revision 1.1  2009/05/13 13:20:58  gtr
*** empty log message ***

Revision 1.3  2007/12/04 17:31:39  clq2
PAL_KEA_2378

Revision 1.1.1.1.138.1  2007/11/15 18:19:16  kea
Multicone fixes, various bugzilla ticket fixes, tweaks after profiling.

Revision 1.2  2007/11/01 11:25:46  kea
Merging MBT's branch pal-mbt-multicone1.

Revision 1.1.1.1.140.1  2007/10/26 16:45:05  mbt
Add MulticoneTest

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.7  2004/03/13 01:10:10  mch
It05 Refactor (Client)

Revision 1.6  2004/03/12 04:54:07  mch
It05 MCH Refactor

Revision 1.5  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.4  2003/11/20 15:45:47  nw
started looking at tese tests

Revision 1.3  2003/11/18 14:37:35  nw
removed references to WorkspaceTest - has now been moved to astrogrid-common

Revision 1.2  2003/11/18 11:08:55  mch
Removed client dependencies on server

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.4  2003/11/06 16:46:28  mch
Added main method

Revision 1.3  2003/09/15 22:56:32  mch
Test fixes

Revision 1.2  2003/09/05 13:25:29  nw
added end-to-end test of DataQueryService

Revision 1.1  2003/09/05 01:03:27  nw
bind things together

*/
