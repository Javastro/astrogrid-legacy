/*$Id: AllTests.java,v 1.2 2003/11/21 17:37:56 nw Exp $
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

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.queriers");
        //$JUnit-BEGIN$
        suite.addTest(DummyQuerierTest.suite());
        suite.addTest(QueryTranslatorTest.suite());
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
