/* $Id: AllTests.java,v 1.7 2004/07/20 02:00:34 nw Exp $
 * Created on Apr 15, 2004 by jdt@roe.ac.uk
 * The auto-integration project
 * Copyright (c) Astrigrid 2004.  All rights reserved.
 *
 */
package org.astrogrid.datacenter.integration;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 *
 * @author jdt
 */
public class AllTests  {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Datacenter");
        //$JUnit-BEGIN$
        suite.addTest(ConeTest.suite());
        suite.addTest(IvoQuery2MySpaceTest.suite());
        suite.addTest(Jes2QueryTest.suite());
        suite.addTest(Query2MySpaceTest.suite());
        suite.addTest(AdqlTest.suite());
        //$JUnit-END$
        return suite;
    }
}
/*
 *  $Log: AllTests.java,v $
 *  Revision 1.7  2004/07/20 02:00:34  nw
 *  removed CeaTest - redundant - covered by more thorough tests in org.astrogrid.applications.integration.datacenter
 *
 *  Revision 1.6  2004/07/01 11:44:13  nw
 *  added CeaTest - as was missing before
 *
 *  Revision 1.5  2004/05/12 09:17:51  mch
 *  Various fixes - forgotten whatfors...
 *
 *  Revision 1.4  2004/04/26 12:16:42  nw
 *  *** empty log message ***
 *
 *  Revision 1.3  2004/04/26 09:05:10  mch
 *  Added adql test
 *
 *  Revision 1.2  2004/04/21 13:41:59  nw
 *  minor tweaks
 *
 *  Revision 1.1  2004/04/16 16:53:28  mch
 *  AllTests.java
 *
 */
