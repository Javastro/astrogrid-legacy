/*
 * $Id: CommandLineApplicationControllerTest.java,v 1.13 2004/01/04 14:51:22 pah Exp $
 * 
 * Created on 01-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;


import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.common.config.BaseApplicationTestCase;
import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.applications.description.ApplicationDescriptionConstants;
import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.applications.description.TestAppConst;
import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CommandLineApplicationControllerTest extends BaseApplicationTestCase {

   private CommandLineApplicationController controller;

   private String jobstepid = null;

   private String monitorURL = null;

   private String applicationid = null;


   private ParameterValues parameters = null;

   private String executionId;

   /**
    * Constructor for CommandLineApplicationControllerTest.
    * @param arg0
    */
   public CommandLineApplicationControllerTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CommandLineApplicationControllerTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      controller = new CommandLineApplicationController();
      //completely bogus community snippet....
      monitorURL="JESMonitorDummy";
      applicationid = TestAppConst.TESTAPP_NAME;
      parameters = new ParameterValues();
   }

   final public void testExecuteApplication() {
      testInitializeApplication();
      controller.executeApplication(executionId);
      
   }

   final public void testGetApplicationDescription() {
      SimpleApplicationDescription desc = controller.getApplicationDescription(applicationid);
      assertNotNull("application description",desc);
   }

   final public void testListApplications() {
      String[] apps = controller.listApplications();
      assertNotNull(apps);
      assertEquals("there are 2 test applications", 2, apps.length);
      // perhaps should test names also
   }

   final public void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
      
      // need to start a long running application and then perform this query - would be good to try firing multiple applications at once also...
   }

   final public void testReturnRegistryEntry() {
      String reg  = controller.returnRegistryEntry();
      assertNotNull(reg);
      assertEquals("this is not implemented yet", reg); // need to change when implemented!
   }

   final public void testInitializeApplication() {
      
      executionId = initApp();
      
   }

   private String initApp() {
      String exid;
      parameters.setMethodName(TestAppConst.MAIN_INTERFACE);
      parameters.setParameterSpec(TestAppConst.PARAMETERSPEC1);
      exid = controller.initializeApplication(applicationid, jobstepid, monitorURL, user, parameters);
      CmdLineApplication app = controller.getRunningApplication(exid);
      assertNotNull("applicaton object not returned after initialization", app);
      Parameter[] params = app.getParameters();
      assertNotNull("application did not return the parameters that were set",params);
      //print the paramter values out so that we can see what went in
      System.out.println("Parameter values");
      for (int i = 0; i < params.length; i++) {
         System.out.println(i + " " + params[i]);
      }
      return exid;
   }
   
   final public void testMultiRun()
   {
      String ex1, ex2;
      ex1 = initApp();
      ex2 = initApp();
      String status1 = controller.queryApplicationExecutionStatus(ex1);
      assertEquals("status", Status.INITIALIZED.toString(),status1);
      String status2 = controller.queryApplicationExecutionStatus(ex2);
      assertEquals("status", Status.INITIALIZED.toString(),status2);
      
      //run te applications
      controller.executeApplication(ex1);
      status1 = controller.queryApplicationExecutionStatus(ex1);
      assertEquals("status", Status.RUNNING.toString(),status1);
      
      controller.executeApplication(ex2);
      
      status2 = controller.queryApplicationExecutionStatus(ex2);
      assertEquals("status", Status.RUNNING.toString(),status2);
      
      try {
         Thread.sleep(40000);// wait for the applications to finish - they should run for 30 seconds each
      }
      catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      status1 = controller.queryApplicationExecutionStatus(ex1);
      assertEquals("status", Status.COMPLETED.toString(),status1);
      status2 = controller.queryApplicationExecutionStatus(ex2);
      assertEquals("status", Status.COMPLETED.toString(),status2);
      
   }

}
