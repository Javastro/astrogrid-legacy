/*
 * $Id: WorkFlowCastorRoundTripTest.java,v 1.3 2004/08/28 07:29:32 pah Exp $
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
public class WorkFlowCastorRoundTripTest extends TestCase {
   private static final String TESTCONTAINER = "testdata";
   private boolean rc;

   private String outFilename;

   private String infileName;

   private User user= new User();

   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(WorkFlowCastorRoundTripTest.class);
   private final Date runDate = new Date();
   
   /**
    * Constructor for WorkFlowToApplicationTest.
    * @param arg0
    */
   public WorkFlowCastorRoundTripTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(WorkFlowCastorRoundTripTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();

   }

   public void testRun() throws IOException, MarshalException, ValidationException {
      // need to put a file into mySpace
      Vector servers = new Vector();
      servers.add("serv1");


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
      
      param.setValue("15");
      inputs.addParameter(param);

      
      param = new ParameterValue();
      param.setName("P2");
      param.setValue("25.4");

      param = new ParameterValue();
      param.setName("P4");
      param.setValue("Hello World");

      param = new ParameterValue();
      param.setName("p9");
      param.setIndirect(true);
      param.setValue(infileName);
      
      
      // output
      Output outputs = new Output();
     
      param = new ParameterValue();
      param.setName("P3");
      param.setIndirect(true);
      param.setValue(outFilename);
      
      
      tool.setInput(inputs);
      tool.setOutput(outputs);
      tool.setInterface("simple");
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
      File f2 = new File("/tmp/workflowtest2.xml");
     
      workflow.setCredentials(credentials);
      FileWriter writer = new FileWriter(f1);
      Marshaller.marshal(workflow, writer);
 
      Unmarshaller um = new Unmarshaller(Workflow.class);
      
      FileInputStream istream = new FileInputStream(f2);
      InputSource saxis = new InputSource(istream);
      Workflow inwf =  (Workflow)um.unmarshal( saxis);
      
  //TODO need to add some tests of the workflow that is read in...
   }

 

}
