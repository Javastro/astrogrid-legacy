/* $Id: AllTests.java,v 1.1 2004/04/16 16:53:28 mch Exp $
 * Created on Apr 15, 2004 by jdt@roe.ac.uk
 * The auto-integration project
 * Copyright (c) Astrigrid 2004.  All rights reserved.
 *
 */
package org.astrogrid.datacenter.integration;

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
        suite.addTest(ConeTest.suite());
        suite.addTest(CeaTest.suite());
        suite.addTest(Query2MySpaceTest.suite());
        suite.addTest(IvoQuery2MySpaceTest.suite());
        suite.addTest(Jes2QueryTest.suite());
        //$JUnit-END$
        return suite;
    }
}
/*
 *  $Log: AllTests.java,v $
 *  Revision 1.1  2004/04/16 16:53:28  mch
 *  AllTests.java
 *
 */
