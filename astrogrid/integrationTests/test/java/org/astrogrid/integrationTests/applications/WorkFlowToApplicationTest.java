/*
 * $Id: WorkFlowToApplicationTest.java,v 1.4 2004/03/04 12:28:01 pah Exp $
 * 
 * Created on 07-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.integrationTests.applications;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import junit.framework.TestCase;

import org.astrogrid.applications.beans.v1.Parameter;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.common.config.ConfigLoader;
import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.integrationTests.common.ConfManager;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.mySpace.delegate.helper.MySpaceHelper;
import org.astrogrid.portal.workflow.WKF;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.types.JoinType;



/**
 * Exercises the workflow object model, then submits the job created to run a test application.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class WorkFlowToApplicationTest extends TestCase {
   private static final String TESTCONTAINER = "testdata";
   private boolean rc;

   private String outFilename;

   private String infileName;

   private User user= new User();

   private MySpaceClient mySpaceManager;

   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(WorkFlowToApplicationTest.class);
   private final Date runDate = new Date();
   
   /**
    * Constructor for WorkFlowToApplicationTest.
    * @param arg0
    */
   public WorkFlowToApplicationTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(WorkFlowToApplicationTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      WKF.getInstance().checkPropertiesLoaded();
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG); // set up the test config as early as possible

   }

   public void testRun() throws JesDelegateException, IOException {
      // need to put a file into mySpace
      Vector servers = new Vector();
      servers.add("serv1");

      try {
         //load properties
         mySpaceManager = MySpaceDelegateFactory.createDelegate(ConfManager.getInstance().getMySpaceEndPoint());
         user = new User();
         String userId = user.getUserId();
         String communityId = user.getCommunity();
         String credential = user.getToken();
         mySpaceManager.createUser(userId, communityId, credential, servers);
         String containerref = MySpaceHelper.formatMyspaceContainerReference(user, "serv1", TESTCONTAINER);
         mySpaceManager.createContainer(userId, communityId, credential, containerref);
         infileName =
            MySpaceHelper.formatMyspaceReference(user, "serv1",  TESTCONTAINER, "testInfile");
         outFilename =
         MySpaceHelper.formatMyspaceReference(user, "serv1",  TESTCONTAINER, "testOutfile");
         mySpaceManager.saveDataHolding(
            userId,
            communityId,
            credential,
            infileName,
            "This is some test contents for myspace",
            TESTCONTAINER, // this should not be the container, but a "type" referece
            MySpaceClient.OVERWRITE);
            mySpaceManager.deleteDataHolding(userId, communityId, credential, outFilename);
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      // now create the workflow

      final String workflowName =
         "WorkflowToApplicationControllerTest" + runDate.getTime(),
         description =
            "A one sequence, one step workflow designed without use of a template";

      Workflow workflow = null;
      Sequence sequence = null;
      Step step = null;
      String wfXML = null;

      workflow = new Workflow();
      workflow.setName(workflowName);
      workflow.setDescription(description);

      sequence = new Sequence();
     
      workflow.setSequence(sequence);
     
      step = new Step();
      step.setName("One step sequence");
      step.setDescription("One step sequence containing test tool");
      step.setJoinCondition(JoinType.ANY);
      sequence.addActivity(step);
      
      // Setting sequence and step numbers shows a weakness in the current approach.
      // These should be auto-generated in some fashion...
      step.setSequenceNumber(1);
      step.setStepNumber(1);

      // set up the tool - totally manually

      Tool tool = new Tool();
      tool.setName("testapp");
      
      Input inputs = new Input();

      ParameterValue param = new ParameterValue();
      param.setName("P1");
      param.setType(ParameterTypes.INTEGER);
      
      param.setContent("15");
      inputs.addParameter(param);

      
      param = new ParameterValue();
      param.setName("P2");
      param.setType(ParameterTypes.DOUBLE);
      param.setContent("25.4");

      param = new ParameterValue();
      param.setName("P4");
      param.setType(ParameterTypes.STRING);
      param.setContent("Hello World");

      param = new ParameterValue();
      param.setName("p9");
      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setContent(infileName);
      
      
      // output
      Output outputs = new Output();
     
      param = new ParameterValue();
      param.setName("P3");
      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setContent(outFilename);
      
      
      tool.setInput(inputs);
      tool.setOutput(outputs);
      //put the tool in the step...
      step.setTool(tool);

      // set up the credentials TODO - should have a common util to do this for the intgration tests...
      
      Credentials credentials = new Credentials();
      Account account = new Account();
      account.setName("anonymous");
      account.setCommunity("test.astrogrid.org");
      credentials.setAccount(account);
      Group group = new Group();
      group.setName("test");
      group.setCommunity("test.astrogrid.org");
      credentials.setGroup(group);
      credentials.setSecurityToken("dummy");
      
      workflow.setCredentials(credentials);
      
      
      //submit the job....
         
      JobController jobcon = JesDelegateFactory.createJobController(ConfManager.getInstance().getJobControllerEndPoint());  
      
      jobcon.submitJob(workflow);      
 
   }

   /**
    * @return
    */
   private String communitySnippet() {
      return user.toSnippet();
   }

}
