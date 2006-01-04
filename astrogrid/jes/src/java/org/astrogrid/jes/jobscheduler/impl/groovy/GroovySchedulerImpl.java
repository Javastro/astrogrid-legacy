/*$Id: GroovySchedulerImpl.java,v 1.7 2006/01/04 09:52:32 clq2 Exp $
 * Created on 26-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.jes.util.Cache;
import org.astrogrid.jes.util.TemporaryBuffer;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.Marshaller;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Test;

/** implementation of jobscheduler, based on groovy rules engine and state machine.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
 *
 */
public class GroovySchedulerImpl extends AbstractJobSchedulerImpl 
    implements JobScheduler, ComponentDescriptor{
    /** configuration component - abstracts from messy business of finding stylesheets, etc. */
    public interface Transformers {
        /** transformer of workflows to set of rules. */
        public Transformer getCompiler() throws Exception;
        /** annotator of workflows with id attributes */
        public Transformer getWorkflowAnnotator() throws Exception;
    }
    /** Construct a new GroovySchedulerImpl
     * @param factory produces jobs
     * @param transformers compiles documents into rulebase.
     * @param dispatcher dispatches individual steps to cea
     * @param interpFactory creates interpreters for compiled rulebases.
     */
    public GroovySchedulerImpl(JobFactory factory,Transformers transformers,Dispatcher dispatcher,GroovyInterpreterFactory interpFactory) {
        super(factory);
        this.transformers = transformers;
        this.disp = dispatcher;
        this.interpFactory = interpFactory;
    }
    protected final Transformers transformers;
    protected final Dispatcher disp;
    protected final GroovyInterpreterFactory interpFactory;
    
    /** 
     * annotate the workflow with id numbers. then
     * compile the workflow into xml rules with embedded groovy scripts., deserialize this as interpreter, add as extension to workflow.
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl#initializeJob(org.astrogrid.workflow.beans.v1.Workflow)
     *
     */ 
    protected Workflow initializeJob(Workflow job) throws Exception {
        // serialize input workflow to string
        StringWriter wfWriter = new StringWriter();
        job.marshal(wfWriter);
        wfWriter.close();
        String originalWorkflowDoc  = wfWriter.toString();        

        TemporaryBuffer buff = interpFactory.getBuffer();
        // annotate this workflow
        buff.writeMode();
        Writer annotatedWorkflowDoc = buff.getWriter();
        transformers.getWorkflowAnnotator().transform(new StreamSource(new StringReader(originalWorkflowDoc)),new StreamResult(annotatedWorkflowDoc));
        
        buff.readMode();
        Workflow annotatedJob =  Workflow.unmarshalWorkflow(buff.getReader());
        
        // now compile the workflow into a set of rules.
        buff.writeMode();
        Writer rules = buff.getWriter();
        transformers.getCompiler().transform(new StreamSource(new StringReader(originalWorkflowDoc)),new StreamResult(rules));
               
        //maybe a bit redundant - should be able to add in xml directly - but this is a good sanity check - no point going any further if
        // we can't deserialize the generated rules as an interpreter.
        buff.readMode();
        // Debug: dump text to standard out for the JUnit logs. @todo remove this after debugging GTR 2005-12-09
        System.out.println(buff.getContents());
        GroovyInterpreter interp = interpFactory.newInterpreter(buff.getContents(),new JesInterface(annotatedJob,disp,this));
        interpFactory.pickleTo(interp,annotatedJob);

       return annotatedJob;
    }
    /** unpickle the interpreter from the workflow, then try to run any triggerable rules */
    protected void scheduleSteps(Workflow job) {
    GroovyInterpreter interp = null;
    try {
        interp = interpFactory.unpickleFrom(new JesInterface(job,disp,this));
    } catch (PickleException e) {
        logger.error("Can't create workflow interpreter",e);
        recordFatalError(job,e);
        return;
    }
    try {
        interp.run();
    } catch (ScriptEngineException e) {
        logger.warn("Run threw exception",e);
        recordFatalError(job,e);
        return;
    } 
    try { 
        interpFactory.pickleTo(interp,job);
    } catch (PickleException e) {
        logger.error("Can't pickle interpreter");
        recordFatalError(job,e);
    }    
    } // end of scheduleSteps()

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl#updateJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void updateJobStatus(Workflow job) {
        // nowt - done internally.
    }

    /** 
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl#getStepForFragment(org.astrogrid.workflow.beans.v1.Workflow, java.lang.String)
     */
    protected Step getStepForFragment(Workflow job, String fragment) {        
               return (Step)job.findXPathValue("//*[id='" + fragment + "']");
    }


    /**
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl#updateStepStatus(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Step, org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase)
     *  do what parent, does, and then update corresponding state in interpreter too.
     */
    protected void updateStepStatus(Workflow wf, Step step, ExecutionPhase status) {
        super.updateStepStatus(wf, step, status);
        try {
        GroovyInterpreter interp = interpFactory.unpickleFrom(new JesInterface(wf,disp,this));
        interp.updateStepStatus(step,status);
        interpFactory.pickleTo(interp,wf);
        } catch (PickleException e) {
            logger.error("Failed to update step status",e);
            recordFatalError(wf,e);
        }
    }
    /** store results of a cea computation back into workfllow interpreter.
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl#storeResults(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Step, org.astrogrid.applications.beans.v1.cea.castor.ResultListType)
     */
    protected void storeResults(Workflow wf, Step step, ResultListType results) {
        // first check that they need to be stored
        if (step.getResultVar() == null) { // user hasn't specified a var to store them in, so not needed.
            return;
        }
        try {
            GroovyInterpreter interp = interpFactory.unpickleFrom(new JesInterface(wf,disp,this));
            interp.storeResults(step,results);
            interpFactory.pickleTo(interp,wf);
        } catch (PickleException e) {
            logger.error("Failed to store results",e);
            recordFatalError(wf,e);
        }
    }
//---------------------
  /**
   * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
   */
  public String getName() {
      return "scripted-scheduler";
  }

  /**
   * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
   */
  public String getDescription() {
      return "Scheduler Implementation that uses a scripting engine";
  }

  /**
   * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
   */
  public Test getInstallationTest() {
      return null;
  }




}


/* 
$Log: GroovySchedulerImpl.java,v $
Revision 1.7  2006/01/04 09:52:32  clq2
jes-gtr-1462

Revision 1.6.42.1  2005/12/09 23:11:55  gtr
I refactored the base-directory feature out of its inner class and interface in FileJobFactory and into org.aastrogrid.jes.util. This addresses part, but not all, of BZ1487.

Revision 1.6  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.5.56.1  2005/04/12 17:08:15  nw
caching to improve performance

Revision 1.5  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.4.26.1  2004/11/05 16:15:30  nw
optimized by uising temporary buffers.

Revision 1.4  2004/09/06 16:47:04  nw
javadoc

Revision 1.3  2004/08/04 16:51:46  nw
added parameter propagation out of cea step call.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.4  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.3  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.2  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.
 
*/