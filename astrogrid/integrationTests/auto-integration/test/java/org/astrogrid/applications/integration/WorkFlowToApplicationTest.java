/*
 * $Id: WorkFlowToApplicationTest.java,v 1.3 2004/04/21 10:43:03 nw Exp $
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

package org.astrogrid.applications.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import junit.framework.TestCase;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.mySpace.delegate.helper.MySpaceHelper;
import org.astrogrid.portal.workflow.WKF;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
//import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.types.JoinType;
import org.astrogrid.workflow.integration.AbstractTestForIntegration;



/**
 * Exercises the workflow object model, then submits the job created to run a test application.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class WorkFlowToApplicationTest extends AbstractTestForIntegration {
   private Ivorn targetIvorn;
   private static final String TESTCONTAINER = "testdata";
   private boolean rc;

   private String outFilename;

   private String infileName;


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

   }

   public void testRun() throws JesDelegateException, IOException, MarshalException, ValidationException {
      // need to put a file into mySpace
      Vector servers = new Vector();
      servers.add("serv1");

      try {
         //load properties
         VoSpaceClient voSpaceClient = new VoSpaceClient(user); //note that we are assuming that the user is looking at their own files...
         String message = "testdata for integration test file contents";
         byte[] bytes = message.getBytes();
         targetIvorn = createIVORN("testfile.dat");
         voSpaceClient.putBytes(bytes, 0, bytes.length, targetIvorn, false);
         
      
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
      tool.setInterface("i1");
      
      Input inputs = new Input();

      ParameterValue param = new ParameterValue();
      param.setName("P1");
      param.setType(ParameterTypes.INTEGER);
      
      param.setValue("15");
      inputs.addParameter(param);

      
      param = new ParameterValue();
      param.setName("P2");
      param.setType(ParameterTypes.DOUBLE);
      param.setValue("25.4");

      param = new ParameterValue();
      param.setName("P4");
      param.setType(ParameterTypes.STRING);
      param.setValue("Hello World");

      param = new ParameterValue();
      param.setName("p9");
      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(infileName);
      
      
      // output
      Output outputs = new Output();
     
      param = new ParameterValue();
      param.setName("P3");
      param.setType(ParameterTypes.MYSPACE_FILEREFERENCE);
      param.setValue(outFilename);
      
      
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
 
      File f1 = new File("/tmp/workflowtest.xml");
     
      workflow.setCredentials(credentials);
      FileWriter writer = new FileWriter(f1);
      Marshaller.marshal(workflow, writer);
 
      Unmarshaller um = new Unmarshaller(Workflow.class);
      
      FileInputStream istream = new FileInputStream(f1);
      InputSource saxis = new InputSource(istream);
      Workflow inwf =  (Workflow)um.unmarshal( saxis);
      
      //submit the job....
         
      JobController jobcon = JesDelegateFactory.createJobController("http://localhost:8080/astrogrid-jes-SNAPSHOT/services/JobControllerService");
      
      jobcon.submitWorkflow(workflow);
 
   }

 
}

