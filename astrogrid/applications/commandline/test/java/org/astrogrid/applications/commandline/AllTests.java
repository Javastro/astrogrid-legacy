/*
 * $Id: AllTests.java,v 1.3 2005/07/05 08:26:56 clq2 Exp $
 * 
 * Created on 11-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.commandline;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class AllTests {
   public static void main(String[] args) {
      junit.textui.TestRunner.run(AllTests.class);
   }
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for Commandline variant of CEA");
      suite.addTest(org.astrogrid.applications.commandline.digester.AllTests
         .suite());
      //$JUnit-BEGIN$
      suite.addTestSuite(CommandLineCEAComponentManagerTest.class);
      suite.addTestSuite(CommandLineApplicationDescriptionTest.class);
      suite.addTestSuite(CommandLineApplicationTest.class);
      suite.addTestSuite(CommandLineApplicationEnvironmentTest.class);
      suite.addTestSuite(DftCommandLineFormationTest.class);
      suite.addTestSuite(SExtractorCommandLineFormationTest.class);
      suite.addTestSuite(WFOApplicationConfigLoadTest.class);
       //$JUnit-END$
      return suite;
   }
}