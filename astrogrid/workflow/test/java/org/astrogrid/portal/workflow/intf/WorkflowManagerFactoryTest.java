/*$Id: WorkflowManagerFactoryTest.java,v 1.5 2004/06/30 13:37:24 jl99 Exp $
 * Created on 10-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import junit.framework.TestCase;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.jes.delegate.impl.JobControllerDelegateImpl;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 *
 */
public class WorkflowManagerFactoryTest extends TestCase {
    /**
     * Constructor for WorkflowManagerFactoryTest.
     * @param arg0
     */
    public WorkflowManagerFactoryTest(String arg0) {
        super(arg0);
    }
    /** test everyting gets created correctly in a simple setup */
    public void testSimpleSetup() throws Exception {
        Config conf = SimpleConfig.getSingleton();
        conf.setProperty(WorkflowManagerFactory.WORKFLOW_JES_ENDPOINT_KEY,"http://localhost:8080/jes-SNAPSHOT/services/JobController");
        createAndCheckManager(conf);
    }

    
    /** test everything gests create correctly in a dummy-delegate setup */
    public void testDummySetup() throws Exception {
        Config conf = SimpleConfig.getSingleton();
        conf.setProperty(WorkflowManagerFactory.WORKFLOW_JES_ENDPOINT_KEY,JobControllerDelegateImpl.TEST_URI);
        createAndCheckManager(conf);  
    }
    
    protected void createAndCheckManager(Config conf) throws WorkflowInterfaceException {
        WorkflowManagerFactory fac = new WorkflowManagerFactory(conf);
        assertNotNull(fac);
        WorkflowManager man = fac.getManager();
        assertNotNull(man);
        assertNotNull(man.getJobExecutionService());
        assertNotNull(man.getToolRegistry());
        assertNotNull(man.getWorkflowBuilder());
        assertNotNull(man.getWorkflowStore());
    }
}


/* 
$Log: WorkflowManagerFactoryTest.java,v $
Revision 1.5  2004/06/30 13:37:24  jl99
Reorg of import statements to cover removal of old MySpace delegate classes

Revision 1.4  2004/04/14 13:46:06  nw
implemented cut down workflow store interface over Ivo Delegate

Revision 1.3  2004/03/16 00:40:35  nw
added unit test to check can create workflowManager based on dummy delegates

Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:51  nw
tests for impls
 
*/