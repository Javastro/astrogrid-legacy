/*$Id: Jes2QueryTest.java,v 1.4 2004/05/13 12:25:04 mch Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.community.User;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowManagerFactory;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * Tests running a query on a datacenter using JES - thus also testing the
 * datacenter's CEA interface
 * @author MCH
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class Jes2QueryTest extends TestCase {

   protected User user;
    protected Account acc;
    protected Group group;
    protected Credentials creds;
    protected Workflow wf;
    protected WorkflowManager manager;
   
   /*
     * @see AbstractTestForIntegration#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
      
      User user = new User("avodemo@test.astrogrid.org", "unknown group", "loony");
      
      acc = new Account();
        acc.setName("avodemo");
        acc.setCommunity("test.astrogrid.org");
        assertNotNull(acc);
    
        group = new Group();
        group.setName("inttest");
        group.setCommunity("test.astrogrid.org");
        assertNotNull(group);
  
    creds = new Credentials();
        creds.setAccount(acc);
        creds.setGroup(group);
        creds.setSecurityToken("token");
        assertNotNull(creds);

      manager = new WorkflowManagerFactory().getManager();
        wf = manager.getWorkflowBuilder().createWorkflow(creds,"test workflow","a description");
    }
    
    public void testBuilder() throws Exception {
        assertNotNull(wf);
        assertTrue(wf.isValid());
    }

    /**
    public void testStore() throws Exception{
        WorkflowStore store = manager.getWorkflowStore();
        assertNotNull(store);
        store.saveWorkflow(acc, wf);
        
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
    
    public void testDatacenter() throws Exception {
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
     **/
    
    public void testSubmit() throws Exception {

       Ivorn workflowIvorn = new Ivorn("ivo://org.astrogrid.localhost/myspace#Jes2QueryTest.workflow");
 
       
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
       manager.getWorkflowStore().saveWorkflow(user, workflowIvorn, wf);
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
       
       assertEquals(ExecutionPhase.COMPLETED_TYPE,w1.getJobExecutionRecord().getStatus().getType()); // i.e. its not in error
       // dump it to myspace store - then we can look at it later.
       w1.setName("execution results:" + urn.getContent());
       manager.getWorkflowStore().saveWorkflow(user, workflowIvorn, w1);
       
       
    }
    
        /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(Jes2QueryTest.class);
    }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
    
}


/*
$Log: Jes2QueryTest.java,v $
Revision 1.4  2004/05/13 12:25:04  mch
Fixes to create user, and switched to mostly testing it05 interface

Revision 1.3  2004/05/12 09:17:51  mch
Various fixes - forgotten whatfors...

Revision 1.2  2004/04/16 15:55:08  mch
added alltests

Revision 1.1  2004/04/16 15:41:03  mch
Added autotests

Revision 1.1  2004/04/15 17:27:57  mch
JES single step test

 
*/
