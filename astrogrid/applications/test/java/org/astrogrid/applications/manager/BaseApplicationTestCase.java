/*
 * $Id: BaseApplicationTestCase.java,v 1.3 2004/04/01 13:54:54 pah Exp $
 * 
 * Created on 30-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.util.Vector;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.common.config.BaseDBTestCase;
import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.applications.description.TestAppConst;
import org.astrogrid.applications.manager.externalservices.MySpaceFromConfig;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public abstract class BaseApplicationTestCase extends WorkFlowUsingTestCase {

   protected MySpaceClient mySpaceManager;
   protected User user;
   protected CommandLineApplicationController controller; //REFACTORME this does not need to be a command line application
   protected String jobstepid = null;
   protected String applicationid = null;
   protected String executionId;
   protected Tool thisTool;

   /**
    * 
    */
   public BaseApplicationTestCase() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param name
    */
   public BaseApplicationTestCase(String name) {
      super(name);
      // TODO Auto-generated constructor stub
   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      Vector servers = new Vector();
      servers.add("serv1");
      super.setUp();
      mySpaceManager = new MySpaceFromConfig(config).getClient();
      assertNotNull(mySpaceManager);
      user = new User();
      String userId = user.getAccount();
      String communityId = user.getGroup();
      String credential = user.getToken();
      mySpaceManager.createUser(userId, communityId, credential, servers);
      mySpaceManager.saveDataHolding(userId, communityId, credential, "testInFile", "This is some test contents for myspace", "data", mySpaceManager.OVERWRITE);
      
      
   }

   public final void testExecuteApplication() throws CeaException {
      setupTool();
      runApplication();
      
      MessageType runStatus = controller.queryExecutionStatus(executionId);
      try {
         while (runStatus.getPhase() != ExecutionPhase.COMPLETED) {
           Thread.sleep(20000);
           runStatus = controller.queryExecutionStatus(executionId);
         
         }
         
      }
      catch (InterruptedException e) {
         e.printStackTrace();
      }
           System.out.print("run app ok");
   }


   public final void testqueryExecutionStatus() {
      //TODO Implement queryExecutionStatus().
      
      // need to start a long running application and then perform this query - would be good to try firing multiple applications at once also...
   }

   public final void testReturnRegistryEntry() {
      String reg  = controller.returnRegistryEntry();
      assertNotNull(reg);
      assertEquals("this is not implemented yet", reg); // need to change when implemented!
   }

   
   
   public void testGetApplicationDescription() throws CeaException {
      ApplicationBase desc = controller.getApplicationDescription(applicationid);
      assertNotNull("application description",desc);
   }

   protected String runApplication() throws CeaException {
      String exid;
      exid = controller.execute(thisTool, jobstepid, monitorURL);
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
   
   protected abstract void setupTool();

   protected String monitorURL = null;
   

}
