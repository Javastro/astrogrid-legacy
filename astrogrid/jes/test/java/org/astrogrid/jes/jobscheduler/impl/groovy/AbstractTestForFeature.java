/*$Id: AbstractTestForFeature.java,v 1.8 2006/01/04 09:52:31 clq2 Exp $
 * Created on 08-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.jes.impl.workflow.CachingFileJobFactory;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.util.TemporaryBaseDirectory;
import org.astrogrid.jes.util.TemporaryBuffer;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import junit.framework.TestCase;

/** abstract class that provides framework for subclasses to test particular features of the scripting workflow.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jul-2004
 * @todo find out why this needs the file job factory.
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
        TemporaryBaseDirectory d = new TemporaryBaseDirectory();
        jobFactory = new CachingFileJobFactory(d);
        trans = new GroovyTransformers();
        disp = new MockDispatcher();
        interpFactory = new GroovyInterpreterFactory(new XStreamPickler(),new TemporaryBuffer());
        sched = new GroovySchedulerImpl(jobFactory,trans,disp,interpFactory); 
    }
    
    protected void tearDown() throws Exception {
        sched = null;
        jobFactory = null;
        trans = null;
        disp = null;
        interpFactory = null;
        System.gc();
        
    }
    
    protected JobScheduler sched;
    protected JobFactory jobFactory;
    protected GroovySchedulerImpl.Transformers trans;
    protected MockDispatcher disp;
    protected GroovyInterpreterFactory interpFactory;
    
    public void testRun() throws Exception {
        Workflow wf = buildWorkflow();
        assertNotNull(wf); 
        wf.validate(); // - must be valid.
        wf = jobFactory.initializeJob(wf);
        JobURN urn = wf.getJobExecutionRecord().getJobId();
        //
        sched.scheduleNewJob(Castor2Axis.convert(urn));
        furtherProcessing(urn);
        Workflow result = jobFactory.findJob(urn);
        verifyWorkflow(result);
    }
    /** construct workflow object */
    protected abstract Workflow buildWorkflow();
    
    /** extend if further scheduling, injuection of results, etc is required */
    protected void furtherProcessing(JobURN urn) throws Exception{
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

    /**
     * @param result
     */
    protected void assertWorkflowCompleted(Workflow result) {
        // workflow should have been executed and completed.
        assertNotNull(result.getJobExecutionRecord());
        JobExecutionRecord jrec = result.getJobExecutionRecord();
        assertNotNull(jrec.getFinishTime());
        assertEquals(ExecutionPhase.COMPLETED,jrec.getStatus());
    }

    protected void assertWorkflowError(Workflow result) {
        // workflow should have been executed and completed.
        assertNotNull(result.getJobExecutionRecord());
        JobExecutionRecord jrec = result.getJobExecutionRecord();
        assertNotNull(jrec.getFinishTime());
        assertEquals(ExecutionPhase.ERROR,jrec.getStatus());
    }    
    
    /**
     * @param sc2
     */
    protected void assertScriptCompletedWithMessage(Script sc2, String msg) {
        assertEquals(1,sc2.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc2.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.COMPLETED, rec.getStatus());
        assertTrue(rec.getMessageCount() > 0);
        //check stdout and stderr messages.
        MessageType stdout = (MessageType)rec.findXPathValue("message[source='stdout']");
        assertNotNull(stdout);
        System.out.println(stdout.getContent());
        assertEquals("Message was " + stdout.getContent(),msg,stdout.getContent().trim());
    }

    /**
     * @param sc1
     */
    protected void assertScriptCompleted(Script sc1) {
        assertEquals(1,sc1.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc1.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.COMPLETED,rec.getStatus());
        assertTrue(rec.getMessageCount() > 0);
    }
    protected void assertScriptError(Script sc1) {
        assertEquals(1,sc1.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc1.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.ERROR,rec.getStatus());
        assertTrue(rec.getMessageCount() > 0);
    }    
    protected void assertScriptNotRun(Script sc1) {
        assertEquals(0,sc1.getStepExecutionRecordCount());

    }    
    /**
     * @param step
     */
    protected void assertStepCompleted(Step step) {
        assertEquals(1,step.getStepExecutionRecordCount());
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.COMPLETED, rec.getStatus());
        assertTrue(rec.getMessageCount() > 0);
    }
    
    protected void assertStepError(Step step) {
        assertEquals(1,step.getStepExecutionRecordCount());
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.ERROR, rec.getStatus());
        assertTrue(rec.getMessageCount() > 0);
    }    

    /**
     * @param step
     */
    protected void assertStepRunning(Step step) {
        assertEquals(1,step.getStepExecutionRecordCount());
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.RUNNING,rec.getStatus());
        assertNotNull(rec.getStartTime());
        assertNull(rec.getFinishTime());
    }

    /**
     * @param script
     * @throws IndexOutOfBoundsException
     */
    protected void assertAllScriptRunsCompleted(Script script) throws IndexOutOfBoundsException {
        for (int i = 0; i < script.getStepExecutionRecordCount(); i++) {
            StepExecutionRecord rec = script.getStepExecutionRecord(i);
            assertEquals(ExecutionPhase.COMPLETED,rec.getStatus());
            assertTrue(rec.getMessageCount() > 0);        
        }
    }
    
}


/* 
$Log: AbstractTestForFeature.java,v $
Revision 1.8  2006/01/04 09:52:31  clq2
jes-gtr-1462

Revision 1.7.18.1  2005/12/09 23:11:55  gtr
I refactored the base-directory feature out of its inner class and interface in FileJobFactory and into org.aastrogrid.jes.util. This addresses part, but not all, of BZ1487.

Revision 1.7  2005/07/27 15:35:08  clq2
jes_nww_review_unit_tests

Revision 1.6.22.1  2005/07/19 15:38:06  nw
fixed unit tests -100% pass rate now.

Revision 1.6  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.5.42.2  2005/04/11 16:31:05  nw
updated version of xstream.
added caching to job factory

Revision 1.5.42.1  2005/04/11 13:57:52  nw
altered to use fileJobFactory instead of InMemoryJobFactory - more realistic

Revision 1.5  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.4.14.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.4  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.3.46.1  2004/11/05 16:07:21  nw
updated test to provide temporary buffer

Revision 1.3  2004/08/18 21:50:59  nw
worked on tests

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/