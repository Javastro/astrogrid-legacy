/*
 * $Id: CommandLineApplicationControllerTest.java,v 1.15 2004/03/23 12:51:25 pah Exp $
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


import java.rmi.RemoteException;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.applications.description.ApplicationDescriptionConstants;
import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.applications.description.TestAppConst;
import org.astrogrid.applications.service.v1.cea.CeaFault;
import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class CommandLineApplicationControllerTest extends BaseApplicationTestCase {

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
      monitorURL=null; //application controller will not attempt to call if it is null
      applicationid = TestAppConst.TESTAPP_NAME;
   }


 
   final public void testListApplications() throws CeaException {
      ApplicationList apps = controller.listApplications();
      assertNotNull(apps);
      assertEquals("there should be 2 test applications", 2, apps.getApplicationDefnCount());
      // perhaps should test names also
   }

   final public void testQueryApplicationExecutionStatus() {
      //TODO Implement queryApplicationExecutionStatus().
      
      // need to start a long running application and then perform this query - would be good to try firing multiple applications at once also...
   }


   final public void testMultiRun() throws CeaException
   {
      String ex1, ex2;
      ex1 = runApplication();
      
      //run te applications
       MessageType status1 = controller.queryExecutionStatus(ex1);
      assertTrue("status", status1.getPhase() == ExecutionPhase.RUNNING);
      
      
      ex2 = runApplication();
      
      MessageType status2 = controller.queryExecutionStatus(ex2);
      assertTrue("status", status2.getPhase() == ExecutionPhase.RUNNING);
      
      try {
         Thread.sleep(40000);// wait for the applications to finish - they should run for 30 seconds each
      }
      catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      status1 = controller.queryExecutionStatus(ex1);
      assertTrue("app1 status completed", status1.getPhase() == ExecutionPhase.COMPLETED);
      status2 = controller.queryExecutionStatus(ex2);
      assertTrue("app2 status completed", status2.getPhase() == ExecutionPhase.COMPLETED);
       
   }

   /** 
    * @see org.astrogrid.applications.manager.BaseApplicationTestCase#setupTool()
    */
   protected void setupTool() {
      thisTool = tool;
    }

}
