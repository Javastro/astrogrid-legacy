/*$Id: AllTests.java,v 1.1 2009/05/13 13:20:57 gtr Exp $
 * Created on 04-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers;


import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.queriers.sql.SqlPluginTest;
import org.astrogrid.datacenter.queriers.sql.SqlQueryTranslatorTest;
//import org.astrogrid.datacenter.queriers.sql.postgres.PostgresQueryTranslatorTest;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.queriers");
        //$JUnit-BEGIN$
        suite.addTest(SampleStarsPluginTest.suite());
        suite.addTest(QuerierTest.suite());
        suite.addTest(new TestSuite(SqlQueryTranslatorTest.class));
        suite.addTest(SqlPluginTest.suite());
        suite.addTest(new TestSuite(QuerierManagerTest.class));
        //suite.addTest(new TestSuite(PostgresQueryTranslatorTest.class));
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
Revision 1.1  2009/05/13 13:20:57  gtr
*** empty log message ***

Revision 1.2  2007/06/08 13:16:10  clq2
KEA-PAL-2169

Revision 1.1.1.1.126.1  2007/04/23 16:45:19  kea
Checkin of work in progress.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.8  2004/09/01 13:19:54  mch
Added sample stars metadata

Revision 1.7  2004/07/07 19:33:59  mch
Fixes to get Dummy db working and xslt sheets working both for unit tests and deployed

Revision 1.6  2004/04/01 17:21:04  mch
Added Postrgres tests

Revision 1.5  2004/03/12 04:54:06  mch
It05 MCH Refactor

Revision 1.4  2004/02/16 23:07:04  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.3  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.2  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.1  2003/11/14 00:38:30  mch
Code restructure

Revision 1.2  2003/11/06 16:45:10  mch
Added main method

Revision 1.1  2003/09/05 01:04:26  nw
bind things together
 
*/
