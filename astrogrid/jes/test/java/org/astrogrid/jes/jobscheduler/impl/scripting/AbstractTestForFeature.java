/*$Id: AbstractTestForFeature.java,v 1.1 2004/07/09 09:32:12 nw Exp $
 * Created on 08-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import junit.framework.TestCase;

/** abstract class that provides framework for subclasses to test particular features of the scripting workflow.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jul-2004
 *
 */
public abstract class AbstractTestForFeature extends TestCase{
    /** Construct a new AbstractTestForScriptingFeature
     * 
     */
    public AbstractTestForFeature(String name) {
        super(name);
    }
    
    protected void setUp() throws Exception {
        jobFactory = new InMemoryJobFactoryImpl();
        trans = new DefaultTransformers();
        disp = new MockDispatcher();
        paths = new DevelopmentJarPaths();
        interpFactory = new WorkflowInterpreterFactory(paths);
        sched = new ScriptedSchedulerImpl(jobFactory,trans,disp,interpFactory);
    }
    protected JobScheduler sched;
    protected JobFactory jobFactory;
    protected ScriptedSchedulerImpl.Transformers trans;
    protected MockDispatcher disp;
    protected WorkflowInterpreterFactory interpFactory;
    protected WorkflowInterpreterFactory.JarPaths paths;
    
    public void testRun() throws Exception {
        Workflow wf = buildWorkflow();
        assertNotNull(wf); 
        wf.validate(); // - must be valid.
        wf = jobFactory.initializeJob(wf);
        JobURN urn = wf.getJobExecutionRecord().getJobId();
        //
        sched.scheduleNewJob(JesUtil.castor2axis(urn));
        furtherProcessing();
        Workflow result = jobFactory.findJob(urn);
        verifyWorkflow(result);
    }
    /** construct workflow object */
    protected abstract Workflow buildWorkflow();
    
    /** extend if further scheduling, injuection of results, etc is required */
    protected void furtherProcessing() {
    }
    
    protected final Workflow createMinimalWorkflow() {
        Workflow wf = new Workflow();
        Credentials creds = new Credentials();
        Account acc = new Account();
        acc.setCommunity("");
        acc.setName("");
        creds.setAccount(acc);
        Group grp = new Group();
        grp.setCommunity("");
        grp.setName("");
        creds.setGroup(grp);
        creds.setSecurityToken("blerghh");
        wf.setCredentials(creds);
        wf.setName("workflow");
        wf.setDescription("none");
        Sequence seq = new Sequence();
        wf.setSequence(seq);
        return wf;        
    }
    
    /** check expected changes to workflow */
    protected abstract void verifyWorkflow(Workflow result) ;
    
}


/* 
$Log: AbstractTestForFeature.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/