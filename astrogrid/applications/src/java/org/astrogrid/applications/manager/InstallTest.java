/*
 * $Id: InstallTest.java,v 1.1 2004/04/15 18:15:59 pah Exp $
 * 
 * Created on 15-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The installation tests.
 * @author Paul Harrison (pah@jb.man.ac.uk) 15-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class InstallTest {

   /**
    * 
    */
   public InstallTest() {
      
   }
   /**
    * This returns the test suite.
    * @return
    */
   public static Test suite() {
      
      CommandLineApplicationController cec = CommandLineApplicationController.getInstance();
      
      TestSuite suite = new TestSuite("Tests for Common Execution Controller");
      
      suite.addTest(cec.getInstallationTests());
      return suite;
   }


 
}
