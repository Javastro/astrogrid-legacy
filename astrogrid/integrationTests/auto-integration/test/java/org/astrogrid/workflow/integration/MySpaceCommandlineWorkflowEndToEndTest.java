/*
 * $Id: MySpaceCommandlineWorkflowEndToEndTest.java,v 1.8 2004/05/27 13:58:58 nw Exp $
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

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;

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
      targetIvorn = createIVORN("/MySpaceCommandlineWorkflowEndToEndTest-output");        
      inputIvorn =  createIVORN("/MySpaceCommandlineWorkflowEndToEndTest-input");        
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
      super.configureToolParameters(tool);
      ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
      pval.setValue(inputIvorn.toString());
      pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
      pval.setValue(targetIvorn.toString());    
   }
   
   public void setupAndVerifyVOSpaceFiles()  throws Exception {
      System.out.println("Input IVORN: " + inputIvorn.toString());
      System.out.println("Output IVORN: " + targetIvorn.toString());
       // write to myspace...
       VoSpaceClient voSpaceClient = new VoSpaceClient(user);
       OutputStream os = voSpaceClient.putStream(inputIvorn);
       assertNotNull(os);
       PrintWriter pout = new PrintWriter(new OutputStreamWriter(os));
       pout.println(TESTCONTENTS);
       pout.close();
       
       InputStream is = voSpaceClient.getStream(inputIvorn);
       assertNotNull(is);
       BufferedReader reader = new BufferedReader(new InputStreamReader(is));
       String content = reader.readLine();
       assertNotNull(content);
       reader.close();
       assertEquals("contents not written to input file correctly",TESTCONTENTS,content);
       // remove output file if there already.
       try {
        voSpaceClient.delete(targetIvorn);
       } catch (Exception e) {
           // don't care. just make sure its gone.
       }
      
   }
   
   public void testResultFileInMyspace() throws Exception {
       VoSpaceClient client = new VoSpaceClient(user);
       InputStream is = client.getStream(targetIvorn);
       assertNotNull(is);
       
       // now check target ivorn has same contents as the original.
       Reader reader = new InputStreamReader(is);
       StringWriter writer = new StringWriter();
       Piper.pipe(reader,writer);
       reader.close();
       writer.close();
       System.out.println("Result file contents:");
       System.out.println(writer.toString());      
       assertTrue("contents of result file do not include contents of input file",writer.toString().indexOf(TESTCONTENTS) != -1);
       }
   
   public static Test suite() {
        TestSuite suite = new TestSuite(MySpaceCommandlineWorkflowEndToEndTest.class.getName());        

        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("verifyRequiredRegistryEntries"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("setupAndVerifyVOSpaceFiles"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("testSubmitWorkflow"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("testExecutionProgress"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("testCheckExecutionResults"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("testResultFileInMyspace"));
        suite.addTest(new MySpaceCommandlineWorkflowEndToEndTest("tidyUp"));        
        return suite;
    }

   /* (non-Javadoc)
    * @see org.astrogrid.workflow.integration.SimpleCommandlineWorkflowEndToEndTest#buildWorkflow()
    */
   protected void buildWorkflow() throws Exception {
      super.buildWorkflow();
      wf.setName("MySpaceUsingWorkflow");
   }

}
