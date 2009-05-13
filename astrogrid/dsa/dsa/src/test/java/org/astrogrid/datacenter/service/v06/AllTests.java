/*$Id: AllTests.java,v 1.1 2009/05/13 13:20:59 gtr Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.datacenter.service.v06");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(DatacenterApplicationTest.class));
        suite.addTest(new TestSuite(DatacenterApplicationDescriptionTest.class));
        suite.addTest(new TestSuite(DatacenterApplicationDescriptionLibraryTest.class));
        //suite.addTest(new TestSuite(DatacenterCEAComponentManagerTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.1  2009/05/13 13:20:59  gtr
*** empty log message ***

Revision 1.2  2007/03/02 13:31:45  kea
Removing test no longer valid after CEA codebase changes.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.1  2004/07/13 17:11:32  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
