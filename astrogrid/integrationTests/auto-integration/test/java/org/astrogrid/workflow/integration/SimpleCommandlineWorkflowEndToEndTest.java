/*$Id: SimpleCommandlineWorkflowEndToEndTest.java,v 1.5 2004/04/25 21:10:11 pah Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import junit.framework.Test;
import junit.framework.TestSuite;

/** end-to-end test of workfow - usecase of creating and submitting a workflow.
 * <p>
 * involves jes, cea, registry, all orchestrated through the workflow library.
 * <p>
 * Test for a workflow that has a single step that calls the testapp
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 * @author Paul Harrison pah@jb.man.ac.uk 23-Apr-2004
 *
 */
public class SimpleCommandlineWorkflowEndToEndTest extends AbstractTestForIntegration {
    private static final String INFILENAME = "/tmp/in";
   private static final String OUTFILENAME = "/tmp/out";
   private File infile;
    private File outfile;
    protected final String TESTCONTENTS = "workflow test contents";
  /**
     * Constructor for WorkflowManagerIntegrationTest.
     * @param arg0
     */
    public SimpleCommandlineWorkflowEndToEndTest(String arg0) {
        super(arg0);
    }
    /*
     * @see AbstractTestForIntegration#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        WorkflowManager manager = ag.getWorkflowManager();
        jes = manager.getJobExecutionService();
        reg = manager.getToolRegistry();
        targetApplication = TESTAPP2;
        outfile = new File(OUTFILENAME);
        assertNotNull(outfile);
        infile = new File(INFILENAME);
        assertNotNull(infile);
        PrintWriter pw = new PrintWriter(new FileOutputStream(infile));
        assertNotNull(pw);
        pw.println(TESTCONTENTS);
        pw.close();
        
        
        
    }
    
    protected JobExecutionService jes;    
    protected ApplicationRegistry reg;  
    protected String targetApplication;
    protected static JobURN urn;

    public void verifyRequiredRegistryEntries() throws Exception {
        ApplicationDescription description = reg.getDescriptionFor(targetApplication);
        assertNotNull(description);
        assertEquals(targetApplication,description.getName());
    }


    public void testSubmitWorkflow() throws Exception {
        buildWorkflow();
        assertTrue("workflow is not valid",wf.isValid());
        urn = jes.submitWorkflow(wf);
         assertNotNull("submitted workflow produced null urn",urn);
        System.out.println("SimpleCommandlineWorkflowEndToEndTest: assigned URN is " + urn.getContent());             
         //check its in the list.
         JobSummary summaries[] = jes.readJobList(acc);       
         assertNotNull("null job list returned",summaries);
         assertTrue("empty job list returned",summaries.length > 0);
         boolean found = false;
         for (int i = 0; i < summaries.length; i++) {
             if (summaries[i].getJobURN().getContent().equals(urn.getContent())) {
                 found=true;
             }
         }
         assertTrue("job not found in list",found);    
    }
    
    
    /** build a simple one-step workflow */
    protected void buildWorkflow() throws Exception  {
        wf.setName("Simple CommandLine Workflow Test");
            // create a tool
           ApplicationDescription descr = reg.getDescriptionFor(targetApplication);
           assertNotNull("could not get application description",descr);
           Tool tool = descr.createToolFromDefaultInterface();
           assertNotNull("tool is null",tool);
           configureToolParameters(tool);
           descr.validate(tool); // shouold be ready to go, with no further config.
           // add a step to the workflow.
           Step step = new Step();
           step.setDescription("single step");
           step.setName("test step");
           step.setTool(tool);
           wf.getSequence().addActivity(step);
    }
    
    /**
    * Fine tune the parameter values... 
    */
   protected void configureToolParameters(Tool tool) {
      ParameterValue pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P1']");
      pval.setValue("1"); // wait one second...
      pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P2']");
      pval.setValue("30.5");
      pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P4']");
      pval.setValue("test string");
      pval = (ParameterValue)tool.findXPathValue("input/parameter[name='P9']");
      pval.setValue(INFILENAME);
      pval = (ParameterValue)tool.findXPathValue("output/parameter[name='P3']");
      pval.setValue(OUTFILENAME);


     

   }
   
   public static final long WAIT_TIME = 15 * 1000; 
    public void testExecutionProgress() throws Exception {
        // loop, polling progress, until seen completed.
        long startTime= System.currentTimeMillis();
        while(System.currentTimeMillis() < startTime + WAIT_TIME) {
            Workflow w1 = jes.readJob(urn);
            if (w1.getJobExecutionRecord() != null && w1.getJobExecutionRecord().getStatus().getType() >= ExecutionPhase.COMPLETED_TYPE) {
                return;
            }
        }
        fail("Job failed to complete in expected time");
    }

    
    public void testCheckExecutionResults() throws Exception {
        wf = jes.readJob(urn);
        assertNotNull("null workflow returned",wf);  
       assertEquals("Workflow not completed",ExecutionPhase.COMPLETED,wf.getJobExecutionRecord().getStatus()); // i.e. its not in error              
    }
    
    public void tidyUp() throws Exception {
        // if workflow completed without error, delete it.
        Workflow w1 = jes.readJob(urn);
        if (w1.getJobExecutionRecord().getStatus().equals(ExecutionPhase.COMPLETED)) {
            jes.deleteJob(urn);
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SimpleCommandlineWorkflowEndToEndTest.class.getName());        

        suite.addTest(new SimpleCommandlineWorkflowEndToEndTest("verifyRequiredRegistryEntries"));
        suite.addTest(new SimpleCommandlineWorkflowEndToEndTest("testSubmitWorkflow"));
        suite.addTest(new SimpleCommandlineWorkflowEndToEndTest("testExecutionProgress"));
        suite.addTest(new SimpleCommandlineWorkflowEndToEndTest("testCheckExecutionResults"));
        suite.addTest(new SimpleCommandlineWorkflowEndToEndTest("tidyUp"));        
        return suite;
    }
 

    
}


/* 
$Log: SimpleCommandlineWorkflowEndToEndTest.java,v $
Revision 1.5  2004/04/25 21:10:11  pah
change timings slightly to give the app time to finish

Revision 1.4  2004/04/25 20:54:22  pah
make use the local file store rather than myspace

Revision 1.3  2004/04/23 22:40:54  pah
more tweaks

Revision 1.2  2004/04/23 16:12:49  pah
added the myspace testapp test

Revision 1.1  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

Revision 1.9  2004/04/22 08:58:38  nw
improved

Revision 1.8  2004/04/21 13:43:43  nw
tidied imports

Revision 1.7  2004/04/20 14:48:21  nw
simplified myspace storepoint.

Revision 1.6  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.5  2004/04/15 23:11:20  nw
tweaks

Revision 1.4  2004/04/14 16:42:37  nw
fixed tests to break more sensibly

Revision 1.3  2004/04/14 15:28:47  nw
updated tests to fit with new WorkspaceStore interface

Revision 1.2  2004/04/14 10:16:40  nw
added to the workflow integration tests

Revision 1.1  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.4  2004/04/06 15:35:28  nw
altered order of things happining

Revision 1.3  2004/04/06 12:08:30  nw
fixes

Revision 1.2  2004/03/17 01:14:37  nw
removed possible infinite loop

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/