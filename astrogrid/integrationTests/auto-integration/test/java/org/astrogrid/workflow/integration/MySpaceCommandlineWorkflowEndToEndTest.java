/*
 * $Id: MySpaceCommandlineWorkflowEndToEndTest.java,v 1.1 2004/04/23 16:12:49 pah Exp $
 * 
 * Created on 23-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.workflow.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * An end to end test that exercises a commandLine application with myspace interaction.
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class MySpaceCommandlineWorkflowEndToEndTest
   extends SimpleCommandlineWorkflowEndToEndTest {

   private Ivorn inputIvorn;

   protected Ivorn targetIvorn;
   protected final String TESTCONTENTS = "workflow test contents";

   /**
    * Constructor for MySpaceCommandlineWorkflowEndToEndTest.
    * @param arg0
    */
   public MySpaceCommandlineWorkflowEndToEndTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(MySpaceCommandlineWorkflowEndToEndTest.class);
   }

   /*
    * @see SimpleCommandlineWorkflowEndToEndTest#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      targetApplication = TESTAPP;
      targetIvorn = new Ivorn(MYSPACE,user.getUserId() + "/MySpaceCommandlineWorkflowEndToEndTest-output");        
      inputIvorn = new Ivorn(MYSPACE,user.getUserId() + "/MySpaceCommandlineWorkflowEndToEndTest-input"); 
      
    // write to myspace...
    VoSpaceClient voSpaceClient = new VoSpaceClient(user);
    byte[] bytes = TESTCONTENTS.getBytes();
    voSpaceClient.putBytes(bytes, 0, bytes.length, inputIvorn, false);       
    
   }

   /*
    * @see TestCase#tearDown()
    */
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   /** 
    * @see org.astrogrid.workflow.integration.SimpleCommandlineWorkflowEndToEndTest#configureToolParameters(org.astrogrid.workflow.beans.v1.Tool)
    */
   protected void configureToolParameters(Tool tool) {
      ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
      pval.setValue(inputIvorn.toString());
      pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
      pval.setValue(targetIvorn.toString());

     

   }
   public static Test suite() {
        TestSuite suite = new TestSuite(MySpaceCommandlineWorkflowEndToEndTest.class.getName());        

        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("verifyRequiredRegistryEntries"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("testSubmitWorkflow"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("testExecutionProgress"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("testCheckExecutionResults"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("tidyUp"));        
        return suite;
    }

}
