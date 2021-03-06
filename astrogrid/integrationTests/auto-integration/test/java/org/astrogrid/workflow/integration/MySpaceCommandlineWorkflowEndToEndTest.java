/*
 * $Id: MySpaceCommandlineWorkflowEndToEndTest.java,v 1.17 2005/03/14 22:03:53 clq2 Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;

/**
 * An end to end test that exercises a commandLine application with myspace interaction.
 * uses TESTAPP
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

    protected FileManagerClient voSpaceClient;
   /*
    * @see SimpleCommandlineWorkflowEndToEndTest#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      targetIvorn = createIVORN("/MySpaceCommandlineWorkflowEndToEndTest-output");        
      inputIvorn =  createIVORN("/MySpaceCommandlineWorkflowEndToEndTest-input"); 
       // write to myspace...
       voSpaceClient = (new FileManagerClientFactory()).login();
       FileManagerNode input;
       if (voSpaceClient.exists(inputIvorn) == null) {
           input = voSpaceClient.createFile(inputIvorn);
       } else {
           input = voSpaceClient.node(inputIvorn);
       }
       OutputStream os = input.writeContent();
       assertNotNull(os);
       PrintWriter pout = new PrintWriter(new OutputStreamWriter(os));
       pout.println(CommandLineProviderServerInfo.TEST_CONTENTS);
       pout.close();

       // remove output file if there already.
       if (voSpaceClient.exists(targetIvorn) != null) {
       try {
        voSpaceClient.node(targetIvorn).delete();
       } catch (Exception e) {
           // don't care. just make sure its gone.
       }    
       }
   }


   /** 
    * @see org.astrogrid.workflow.integration.SimpleCommandlineWorkflowEndToEndTest#configureToolParameters(org.astrogrid.workflow.beans.v1.Tool)
    */
   protected void configureToolParameters(Tool tool) {
       info.populateIndirectTool(tool,inputIvorn.toString(),targetIvorn.toString());
   }
   
   public void checkExecutionResults(Workflow wf) throws Exception{
       // get the result, check its what we expect.
       Step s = (Step)wf.getSequence().getActivity(0);
       assertStepCompleted(s);
       ResultListType res = getResultOfStep(s);
       String value = ((ParameterValue)res.findXPathValue("result[name='P3']")).getValue();
       softAssertEquals("results not at expected location",targetIvorn.toString(),value);  
        try {       
            FileManagerNode node = voSpaceClient.node(targetIvorn);
            InputStream is =  node.readContent();            
        assertNotNull(is);
       
       // now check target ivorn has same contents as the original.
        Reader reader = new InputStreamReader(is);
        StringWriter writer = new StringWriter();    
        Piper.pipe(reader,writer);
        assertTrue("contents of result file do not include contents of input file\n" + writer.toString(),writer.toString().indexOf(CommandLineProviderServerInfo.TEST_CONTENTS) != -1);
        } catch (IOException e) {
            softFail("exception when reading result: " + e.getMessage());
        }
       }
   
}
