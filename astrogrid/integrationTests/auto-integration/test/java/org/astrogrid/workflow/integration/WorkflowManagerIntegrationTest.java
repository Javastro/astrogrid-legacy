/*$Id: WorkflowManagerIntegrationTest.java,v 1.3 2004/04/06 12:08:30 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.Arrays;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class WorkflowManagerIntegrationTest extends AbstractTestForIntegration {
    /**
     * Constructor for WorkflowManagerIntegrationTest.
     * @param arg0
     */
    public WorkflowManagerIntegrationTest(String arg0) {
        super(arg0);
    }
    /*
     * @see AbstractTestForIntegration#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        manager = ag.getWorkflowManager();
        acc = ag.getObjectHelper().createAccount("noel","jodrell");
        assertNotNull(acc);
        group = ag.getObjectHelper().createGroup("devel","jodell");
        assertNotNull(group);
        creds = ag.getObjectHelper().createCredendtials(acc,group);
        assertNotNull(creds);
        wf = manager.getWorkflowBuilder().createWorkflow(creds,"test workflow","a description");        
    }
    
    protected Account acc;
    protected Group group;
    protected Credentials creds;
    protected Workflow wf;
    protected WorkflowManager manager;
    
    public void testBuilder() throws Exception {
        assertNotNull(wf);
        assertTrue(wf.isValid());
    }
    
    public void testStore() throws Exception{
        WorkflowStore store = manager.getWorkflowStore();
        assertNotNull(store);
        store.saveWorkflow(acc,wf);
        
        // get it back again.
        
        Workflow wf1 = store.readWorkflow(acc,wf.getName());
        assertNotNull(wf1);
        assertEquals(wf1.getName(),wf.getName());
        
        // list it
        String[] arr = store.listWorkflows(acc);
        assertNotNull(arr);
        assertTrue(arr.length > 0);
        assertTrue(Arrays.asList(arr).contains(wf.getName()));
        // delete it.
        store.deleteWorkflow(acc,wf.getName());
        // check its not there.
        assertFalse(Arrays.asList(store.listWorkflows(acc)).contains(wf.getName()));
    }
    
    public void testRegistry() throws Exception {
        ApplicationRegistry reg = manager.getToolRegistry();
        String[] names = reg.listApplications();
        assertNotNull(names);
        assertTrue(names.length > 0);
        
        for (int i = 0; i < names.length; i++) {
            ApplicationDescription descr = reg.getDescriptionFor(names[i]);
            assertNotNull(descr);
            assertEquals(descr.getName(),names[i]);
        }
    }
    
    public void testSimpleWorkflow() throws Exception {
        ApplicationRegistry reg = manager.getToolRegistry();
        // create a tool
       ApplicationDescription descr = reg.getDescriptionFor(reg.listApplications()[0]);
       assertNotNull(descr);
       Tool tool = descr.createToolFromDefaultInterface();
       assertNotNull(tool);
       descr.validate(tool); // shouold be ready to go, with no further config.
       // add a step to the workflow.
       Step step = new Step();
       step.setDescription("single step");
       step.setName("test step");
       step.setTool(tool);
       wf.getSequence().addActivity(step);
       assertTrue(wf.isValid());
       // save it in the store,
       manager.getWorkflowStore().saveWorkflow(acc,wf); 
       // now submit it.

       JobExecutionService jes = manager.getJobExecutionService();
       JobURN urn = jes.submitWorkflow(wf);
       assertNotNull(urn);
       //check its in the list.
       JobSummary summaries[] = jes.readJobList(acc);
       assertNotNull(summaries);
       assertTrue(summaries.length > 0);
       boolean found = false;
       for (int i = 0; i < summaries.length; i++) {
           if (summaries[i].getJobURN().getContent().equals(urn.getContent())) {
               found=true;
           }
       }
       assertTrue(found);
           Thread.sleep(10000); // 10 seconds should be enough
        // try retreiving the workflow.
           Workflow w1 = jes.readJob(urn);
           assertNotNull(w1);
            assertEquals(w1.getName(),wf.getName());
       
       assertEquals(ExecutionPhase.COMPLETED,w1.getJobExecutionRecord().getStatus()); // i.e. its not in error
       // dump it to myspace store - then we can look at it later.
       w1.setName("execution results:" + urn.getContent());
       manager.getWorkflowStore().saveWorkflow(acc,w1);
       
       
    }
    
}


/* 
$Log: WorkflowManagerIntegrationTest.java,v $
Revision 1.3  2004/04/06 12:08:30  nw
fixes

Revision 1.2  2004/03/17 01:14:37  nw
removed possible infinite loop

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/