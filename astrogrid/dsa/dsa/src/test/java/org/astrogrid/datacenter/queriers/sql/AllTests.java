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
package org.astrogrid.datacenter.queriers.sql;

import junit.framework.Test;
import junit.framework.TestSuite;
//import org.astrogrid.datacenter.queriers.sql.postgres.PostgresQueryTranslatorTest;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.queriers.sql");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(SqlPluginTest.class));
        suite.addTest(new TestSuite(SqlQueryTranslatorTest.class));
       // suite.addTest(new TestSuite(PostgresQueryTranslatorTest.class));
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

Revision 1.2  2007/06/08 13:16:09  clq2
KEA-PAL-2169

Revision 1.1.1.1.126.1  2007/04/23 16:45:20  kea
Checkin of work in progress.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.4  2004/09/08 21:25:29  mch
Added Postgres

Revision 1.3  2004/03/12 04:54:06  mch
It05 MCH Refactor

Revision 1.2  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.3  2003/11/06 17:52:14  mch
Added main

Revision 1.2  2003/10/14 12:59:40  nw
moved SqlQueryTranslator here from removed adql package

Revision 1.1  2003/09/05 01:05:32  nw
added testing of SQLQuerier over an in-memory Hsql database,

relies on hsqldb.jar (added to project.xml)
 
*/
