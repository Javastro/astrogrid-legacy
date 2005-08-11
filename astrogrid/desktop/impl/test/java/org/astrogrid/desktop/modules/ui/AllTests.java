/*$Id: AllTests.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 23-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Mar-2005
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.desktop.modules.pw");
        //$JUnit-BEGIN$
        suite.addTestSuite(ParameterizedWorkflowLauncherTest.class);
        suite.addTestSuite(WorkflowTemplateTest.class); 
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/27 13:42:41  clq2
1082

Revision 1.1.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/23 14:36:18  nw
got pw working
 
*/