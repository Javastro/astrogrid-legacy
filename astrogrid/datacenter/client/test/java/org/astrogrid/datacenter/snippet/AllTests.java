/*$Id: AllTests.java,v 1.1 2003/11/17 15:40:51 mch Exp $
 * Created on 11-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.snippet;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Sep-2003
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for org.astrogrid.datacenter.common");
        //$JUnit-BEGIN$
        suite.addTest(DocHelperTest.suite());
        suite.addTest(QueryIdHelperTest.suite());
        suite.addTest(StatusHelperTest.suite());
        //suite.addTest(ResponseHelperTest.suite());
        //$JUnit-END$
        return suite;
    }
}


/*
$Log: AllTests.java,v $
Revision 1.1  2003/11/17 15:40:51  mch
Package movements

Revision 1.2  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.4  2003/09/15 22:09:00  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.3  2003/09/15 17:39:18  mch
Better test coverage

Revision 1.2  2003/09/15 14:35:17  mch
Fixes to reading status id and service id & AllTests now tests package

Revision 1.1  2003/09/11 13:27:17  nw
added tests for xml formatting code

*/
