/*$Id: AllTests.java,v 1.4 2003/11/20 15:45:47 nw Exp $
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
import org.astrogrid.util.*;

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
        suite.addTest(ServerTest.suite());
        suite.addTest(DataQueryServiceTest.suite());
        suite.addTest(ListenerTest.suite());
        suite.addTest(ResponseHelperTest.suite());
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
