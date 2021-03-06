/*$Id: AllTests.java,v 1.6 2005/03/13 07:13:39 clq2 Exp $
 * Created on 25-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.jobscheduler.dispatcher");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(CeaApplicationDispatcherTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.6  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.5.96.1  2005/03/11 14:03:47  nw
tests for different dispatchers.

Revision 1.5  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.4  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.3.50.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code
 
*/