/* $Id: AllTests.java,v 1.4 2004/04/20 14:45:37 nw Exp $
 * Created on Apr 15, 2004 by jdt@roe.ac.uk
 * The auto-integration project
 * Copyright (c) Astrigrid 2004.  All rights reserved.
 *
 */
package org.astrogrid.store.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 *
 * @author jdt
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Store");
        //$JUnit-BEGIN$
        suite.addTest(MySpaceTest.suite());
        suite.addTest(VoSpaceTest.suite());
        suite.addTest(new TestSuite(DeployedManagerTest.class));
        //$JUnit-END$
        return suite;
    }
}
/*
 *  $Log: AllTests.java,v $
 *  Revision 1.4  2004/04/20 14:45:37  nw
 *  added in new test
 *
 *  Revision 1.3  2004/04/16 15:17:41  mch
 *  Removed it04Delegate test
 *
 *  Revision 1.2  2004/04/15 23:11:20  nw
 *  tweaks
 *
 *  Revision 1.1  2004/04/15 14:53:09  jdt
 *  *** empty log message ***
 *
 */
