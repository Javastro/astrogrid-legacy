/*
 * $Id: AllTests.java,v 1.1 2004/03/23 19:46:04 pah Exp $
 * 
 * Created on 23-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Runs all the important tests in the applications project. Useful for running from eclipse.
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class AllTests {

   public static void main(String[] args) {
      junit.textui.TestRunner.run(AllTests.class);
   }

   public static Test suite() {
      TestSuite suite = new TestSuite("Tests for org.astrogrid.applications");
      //$JUnit-BEGIN$
      suite.addTest(new TestSuite(ConfigTest.class));
      //$JUnit-END$
      suite.addTest(org.astrogrid.applications.commandline.AllTests.suite());
      suite.addTest(org.astrogrid.applications.common.config.AllTests.suite());
      suite.addTest(org.astrogrid.applications.delegate.AllTests.suite());
      suite.addTest(org.astrogrid.applications.description.AllTests.suite());
      suite.addTest(org.astrogrid.applications.manager.AllTests.suite());
      return suite;
   }
}
