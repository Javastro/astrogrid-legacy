/*
 * $Id: WorkFlowToApplication.java,v 1.1 2004/01/09 00:28:33 pah Exp $
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

import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.applications.common.config.ConfigLoader;
import org.astrogrid.community.User;
import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.mySpace.delegate.helper.MySpaceHelper;
import org.astrogrid.portal.workflow.WKF;
import org.astrogrid.portal.workflow.design.Cardinality;
import org.astrogrid.portal.workflow.design.JoinCondition;
import org.astrogrid.portal.workflow.design.Parameter;
import org.astrogrid.portal.workflow.design.Sequence;
import org.astrogrid.portal.workflow.design.Step;
import org.astrogrid.portal.workflow.design.Tool;
import org.astrogrid.portal.workflow.design.Workflow;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class WorkFlowToApplication extends TestCase {
   private boolean rc;

   private String outFilename;

   private String infileName;

   private User user= new User();

   private MySpaceClient mySpaceManager;

   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(WorkFlowToApplication.class);
   private final Date runDate = new Date();

   /**
    * Constructor for WorkFlowToApplication.
    * @param arg0
    */
   public WorkFlowToApplication(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(WorkFlowToApplication.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      WKF.getInstance().checkPropertiesLoaded();
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG); // set up the test config as early as possible

   }

   public void testRun() {
      Cardinality cardinality = null;
      // need to put a file into mySpace
      Vector servers = new Vector();
      servers.add("serv1");

      try {
         

         //TODO get a better way of getting the mySpaceManager than this....
         mySpaceManager = MySpaceDelegateFactory.createDelegate("http://localhost:8080/astrogrid-mySpace/services/MySpaceManager");
         user = new User();
         String userId = user.getUserId();
         String communityId = user.getCommunity();
         String credential = user.getToken();
         mySpaceManager.createUser(userId, communityId, credential, servers);
         mySpaceManager.createContainer(userId, communityId, credential, "testdata");
         infileName =
            MySpaceHelper.formatMyspaceReference(user, "serv1",  "testdata", "testInfile");
         outFilename =
         MySpaceHelper.formatMyspaceReference(user, "serv1",  "testdata", "testOutfile");
         mySpaceManager.saveDataHolding(
            userId,
            communityId,
            credential,
            infileName,
            "This is some test contents for myspace",
            "data",
            mySpaceManager.OVERWRITE);
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

      workflow = Workflow.createWorkflow(communitySnippet(), workflowName, description);

      sequence = new Sequence(workflow);
      workflow.setChild(sequence);
      step = sequence.createStep(0);
      step.setName("One step sequence");
      step.setDescription("One step sequence containing test tool");
      step.setJoinCondition(JoinCondition.ANY);

      // Setting sequence and step numbers shows a weakness in the current approach.
      // These should be auto-generated in some fashion...
      step.setSequenceNumber(1);
      step.setStepNumber(1);

      // set up the tool - totally manually

      Tool tool = new Tool("testapp");
      cardinality = new Cardinality(1, 1);

      Parameter param = tool.newInputParameter("P1");
      param.setCardinality(cardinality);
      param.setType("xs:integer");
      param.setValue("15");

      param = tool.newInputParameter("P2");
      param.setCardinality(cardinality);
      param.setType("xs:double");
      param.setValue("25.4");

      param = tool.newInputParameter("P4");
      param.setCardinality(cardinality);
      param.setType("xs:string");
      param.setValue("Hello World");

      param = tool.newInputParameter("P9");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(infileName);
      
      
      // output
      
      param = tool.newOutputParameter("P3");
      param.setCardinality(cardinality);
      param.setType("agpd:MySpace_FileReference");
      param.setValue(outFilename);
      
      //put the tool in the step...
      step.setTool(tool);

      rc = Workflow.submitWorkflow( communitySnippet(), workflow ) ;
      logger.info( "JobController says: " + rc ) ;

   }

   /**
    * @return
    */
   private String communitySnippet() {
      return user.toSnippet();
   }

}
