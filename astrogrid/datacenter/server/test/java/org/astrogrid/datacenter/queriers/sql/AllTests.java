/*$Id: AllTests.java,v 1.2 2003/11/28 16:10:30 nw Exp $
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

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.queriers.sql");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(SqlResultsTest.class));
        suite.addTest(new TestSuite(SqlQuerierTest.class));
        suite.addTest(new TestSuite(AdqlQueryTranslatorTest.class));
        suite.addTest(new TestSuite(SqlQueryTranslatorTest.class));
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
