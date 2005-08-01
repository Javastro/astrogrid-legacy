/* $Id: AllTests.java,v 1.2 2005/08/01 08:15:52 clq2 Exp $
 * Created on Apr 15, 2004 by jdt@roe.ac.uk
 * The auto-integration project
 * Copyright (c) Astrigrid 2004.  All rights reserved.
 *
 */
package org.astrogrid.datacenter.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.integration.clientside.FitsTest;
import org.astrogrid.datacenter.integration.clientside.MetadataTest;
import org.astrogrid.datacenter.integration.clientside.Query2MySpaceTest;
import org.astrogrid.datacenter.integration.clientside.RdbmsTest;
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
        TestSuite suite = new TestSuite("Datacenter");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(RdbmsTest.class));
        suite.addTest(new TestSuite(FitsTest.class));
        suite.addTest(new TestSuite(MetadataTest.class));
        suite.addTest(new TestSuite(Query2MySpaceTest.class));
        //$JUnit-END$
        return suite;
    }
}
/*
 *  $Log: AllTests.java,v $
 *  Revision 1.2  2005/08/01 08:15:52  clq2
 *  Kmb 1293/1279/intTest1 FS/FM/Jes/Portal/IntTests
 *
 *  Revision 1.1.2.1  2005/07/12 11:21:06  KevinBenson
 *  old datacenter moved out of the test area
 *
 *  Revision 1.20  2004/11/23 15:45:31  jdt
 *  Merge from INT_JDT_757 (restoring mch's tests)
 *
 *  Revision 1.19.2.1  2004/11/23 15:12:52  jdt
 *  Restored the broken tests that I removed in a hissy fit a week ago.
 *
 *  Revision 1.18  2004/11/03 00:34:40  mch
 *  PAL_MCH Candidate 2 merge
 *
 *  Revision 1.17  2004/11/03 00:31:03  mch
 *  PAL_MCH Candidate 2 merge
 *
 *  Revision 1.16  2004/10/12 23:05:16  mch
 *  Seperated tests properly
 *
 *  Revision 1.15  2004/10/08 15:52:18  mch
 *  More tests for Registry push etc
 *
 *  Revision 1.14  2004/09/09 11:18:45  mch
 *  Moved DeployedServicesTest to separate package
 *
 *  Revision 1.13  2004/09/08 20:35:10  mch
 *  Added tests against deployed services
 *
 *  Revision 1.12  2004/09/08 20:06:11  mch
 *  Added metadat push test
 *
 *  Revision 1.11  2004/09/08 13:58:48  mch
 *  Separated out tests by datacenter and added some
 *
 *  Revision 1.10  2004/09/02 12:33:49  mch
 *  Added better tests and reporting
 *
 *  Revision 1.9  2004/08/31 21:52:28  jdt
 *  fixed compile error
 *
 *  Revision 1.8  2004/07/21 10:37:52  nw
 *  regenerated
 *
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
