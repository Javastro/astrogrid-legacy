/*$Id: ClientTests.java,v 1.8 2004/07/03 11:52:50 mch Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.axisdataserver.TransportTest;
import org.astrogrid.datacenter.delegate.nvocone.NvoConeTest;
import org.astrogrid.io.xml.XmlPrinterTest;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Aug-2003
 *
 */
public class ClientTests {
   
   /**
    * Runs the full set.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
   
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for datacenter client");
      //$JUnit-BEGIN$
      //suite.addTest(QueryTestSuite.suite());
      //$JUnit-END$
      // and add other suites too.
      suite.addTest(org.astrogrid.datacenter.adql.AllTests.suite());
      suite.addTest(new TestSuite(TransportTest.class));
      suite.addTest(NvoConeTest.suite());
      suite.addTest(XmlPrinterTest.suite());
      return suite;
   }
}


/*
 $Log: ClientTests.java,v $
 Revision 1.8  2004/07/03 11:52:50  mch
 Removed delegate type test and added xml printer test

 Revision 1.7  2004/03/13 01:07:11  mch
 It05 Refactor (Client)

 Revision 1.6  2004/03/12 20:04:39  mch
 It05 Refactor (Client)

 Revision 1.5  2003/11/26 16:31:46  nw
 altered transport to accept any query format.
 moved back to axis from castor

 Revision 1.4  2003/11/20 15:43:31  nw
 linked in missing tests

 Revision 1.3  2003/11/19 18:48:47  nw
 fixed transport bug

 Revision 1.2  2003/11/17 15:40:51  mch
 Package movements

 Revision 1.1  2003/11/14 00:36:40  mch
 Code restructure

 Revision 1.14  2003/11/13 12:49:17  mch
 Removed config test (now in common)

 Revision 1.13  2003/11/05 18:54:43  mch
 Build fixes for change to SOAPy Beans and new delegates

 Revision 1.12  2003/09/19 12:02:37  nw
 Added top level test - runs integration tests against an inprocess db and inprocess axis.

 Revision 1.11  2003/09/11 13:28:24  nw
 added xml creation tests

 Revision 1.10  2003/09/05 12:05:42  mch
 Removed tests on removed classes

 Revision 1.9  2003/09/05 01:06:09  nw
 linked in new tests

 Revision 1.8  2003/09/02 14:41:39  nw
 added test

 Revision 1.7  2003/08/29 15:26:55  mch
 Renamed TestXxxx to XxxxxTest so Maven runs them

 Revision 1.6  2003/08/28 15:26:44  nw
 unit tests for adql

 Revision 1.4  2003/08/27 23:57:55  mch
 added workspace and delegate tests

 Revision 1.3  2003/08/26 23:37:11  mch
 Added tests

 Revision 1.2  2003/08/22 10:37:48  nw
 added test hierarchy for Job / JobStep

 Revision 1.1  2003/08/21 12:29:18  nw
 added unit testing for factory manager hierarchy.
 added 'AllTests' suite classes to draw unit tests together - single
 place to run all.

 */
