/*
 * $Id: CommandLineApplicationControllerTest.java,v 1.4 2003/12/07 01:09:48 pah Exp $
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


import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.applications.description.ApplicationDescriptionConstants;
import org.astrogrid.applications.description.TestAppConst;
import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CommandLineApplicationControllerTest extends BaseDBTestCase {

   private CommandLineApplicationController controller;

   private String jobstepid = null;

   private String monitorURL = null;

   private String applicationid = null;

   private User user = null;

   private ParameterValues parameters = null;

   private int executionId;

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
      user = new User("noone","notastrogrid","blah"); 
      monitorURL="JESMonitorDummy";
      applicationid = TestAppConst.TESTAPP_NAME;
      parameters = new ParameterValues();
   }

   final public void testExecuteApplication() {
      testInitializeApplication();
      controller.executeApplication(executionId);
      
   }

//   final public void testGetApplicationDescription() {
//      //TODO Implement getApplicationDescription().
//   }
//
//   final public void testListApplications() {
//      String[] apps = controller.listApplications();
//      assertNotNull(apps);
//      assertEquals("there are 2 test applications", 2, apps.length);
//      // perhaps should test names also
//   }
//
//   final public void testQueryApplicationExecutionStatus() {
//      //TODO Implement queryApplicationExecutionStatus().
//   }
//
//   final public void testReturnRegistryEntry() {
//      String reg  = controller.returnRegistryEntry();
//      assertNotNull(reg);
//      assertEquals("this is not implemented yet", reg); // need to change when implemented!
//   }

   final public void testInitializeApplication() {
      
      parameters.setMethodName(TestAppConst.MAIN_INTERFACE);
      parameters.setParameterSpec("<input><parameter name='P2'>1</parameter><parameter name='P9'>2</parameter></input><output><parameter name='P3'>3</parameter></output>");
      executionId = controller.initializeApplication(applicationid, jobstepid, monitorURL, user, parameters);
   }

}
