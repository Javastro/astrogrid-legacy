/*
 * $Id: TestApplicationControllerRunningHyperZ.java,v 1.3 2004/01/22 12:41:36 pah Exp $
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
public class TestApplicationControllerRunningHyperZ extends BaseApplicationTestCase {

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
   public TestApplicationControllerRunningHyperZ(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(TestApplicationControllerRunningHyperZ.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      controller = new CommandLineApplicationController();
      //completely bogus community snippet....
      monitorURL=null; //application controller will not attempt to call if it is null
      applicationid = "HyperZ";
      parameters = new ParameterValues();
   }

   final public void testExecuteApplication() {
      initializeApplication();
      controller.executeApplication(executionId);
      String runStatus = controller.queryApplicationExecutionStatus(executionId);
      try {
         while (!runStatus.equals(Status.COMPLETED.toString())) {
           Thread.sleep(20000);
           runStatus = controller.queryApplicationExecutionStatus(executionId);
         
         }
         
      }
      catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
           System.out.print("run app ok");
   }

   final public void testGetApplicationDescription() {
      SimpleApplicationDescription desc = controller.getApplicationDescription(applicationid);
      assertNotNull("application description",desc);
   }

   final public void testListApplications() {
      String[] apps = controller.listApplications();
      assertNotNull(apps);
      assertEquals("there are 3 test applications", 3, apps.length);
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

   final public void initializeApplication() {
      
      executionId = initApp();
      
   }

   private String initApp() {
      String exid;
      parameters.setMethodName("simple");
//      parameters.setParameterSpec("<tool><input><parameter name='config_file'>/home/applications/demo/hyperz/zphot.param</parameter><parameter name='input_catalog'>/home/applications/demo/hyperz/bviz-mag-sample.cat</parameter></input><output><parameter name='output_catalog'>out1file</parameter></output></tool>");
    parameters.setParameterSpec("<tool><input><parameter name='config_file'>/home/applications/demo/hyperz/zphot.param</parameter><parameter name='input_catalog'>/home/applications/demo/hyperz/join.xml</parameter></input><output><parameter name='output_catalog'>hyperzout</parameter></output></tool>");
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
   

}
