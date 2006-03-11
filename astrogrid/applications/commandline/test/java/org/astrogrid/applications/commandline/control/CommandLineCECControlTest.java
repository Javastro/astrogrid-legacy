/*
 * $Id: CommandLineCECControlTest.java,v 1.6 2006/03/11 05:57:54 clq2 Exp $
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
      cs = new CommandLineCECControl(new CommandLineApplicationEnvironment.WorkingDir() {
         
         public File getDir() {
             return workingDir;
         }
       });
      
      logger.info(workingDir.getAbsolutePath());
      testdir = new File(workingDir, "dir");
      assertTrue("test directory could not be created",testdir.mkdir());
      File contentFile = new File(testdir, "afile");
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
 * Revision 1.6  2006/03/11 05:57:54  clq2
 * roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489
 *
 * Revision 1.4  2006/01/10 11:26:52  clq2
 * rolling back to before gtr_1489
 *
 * Revision 1.2  2005/08/10 14:45:37  clq2
 * cea_pah_1317
 *
 * Revision 1.1.2.1  2005/07/21 15:12:06  pah
 * added workfile deletion
 *
 */
