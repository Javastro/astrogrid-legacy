/*
 * $Id: CommandLineCECControlTest.java,v 1.7 2006/03/17 17:50:58 clq2 Exp $
 * 
 * Created on 20-Jul-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.astrogrid.applications.commandline.AbstractCommandLineEnvironmentTestCase;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.manager.ControlService;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pharriso@eso.org) 20-Jul-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class CommandLineCECControlTest extends AbstractCommandLineEnvironmentTestCase {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(CommandLineCECControlTest.class);

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CommandLineCECControlTest.class);
   }

   private File testdir;
   private ControlService cs;

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      
      cs = new CommandLineCECControl(this.configuration);
      
      // Create a file in the CEC's temporary-files directory.
      // This simulates what the CEC would do for itself when
      // running an application. The point is to test that the
      // SUT can delete its working files.
      File tfd = this.configuration.getTemporaryFilesDirectory();
      logger.info(tfd.getAbsolutePath());
      File contentFile = new File(tfd, "afile");
      PrintWriter pw = new PrintWriter(new FileOutputStream(contentFile));
      pw.println("some test content");
      pw.close();
   }

   /*
    * Test method for 'org.astrogrid.applications.commandline.control.CommandLineCECControl.deleteOldRuntimeWorkFiles(int)'
    */
   public void testDeleteOldRuntimeWorkFiles() {
      String result = cs.deleteOldRuntimeWorkFiles(0);
      System.out.println(result);
      assertTrue("the deletion failed in some fashion", result.indexOf("Failed") == -1);
   }

}


/*
 * $Log: CommandLineCECControlTest.java,v $
 * Revision 1.7  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.5  2006/03/07 21:45:27  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.20.2  2006/01/31 21:39:07  gtr
 * Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.
 *
 * Revision 1.2.20.1  2005/12/19 18:12:30  gtr
 * Refactored: changes in support of the fix for 1492.
 *
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:12:06  pah
 * added workfile deletion
 *
 */
