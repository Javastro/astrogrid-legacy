/*$Id: ScriptedSchedulerImpl.java,v 1.1 2004/07/09 09:30:28 nw Exp $
 * Created on 10-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Test;

/** Implementation of a scheduler that uses a scripting engine to process and interpret the workflow.
 * @author Noel Winstanley nw@jb.man.ac.uk 10-May-2004
 *
 */
public class ScriptedSchedulerImpl extends AbstractJobSchedulerImpl implements JobScheduler, ComponentDescriptor {

    /** configuration component - abstracts from messy business of finding stylesheets, etc. */
    public interface Transformers {
        /** transformer of workflows to intermediate form */
        public Transformer getCompiler() throws Exception;
        /** translator of intermediate form documents to python rules */
        public Transformer getTranslator() throws Exception;
        /** annotator of workflows with id attributes */
        public Transformer getWorkflowAnnotator() throws Exception;
    }
    /** Construct a new ScriptedSchedulerImpl
     * @param facade
     */
    public ScriptedSchedulerImpl(JobFactory factory, Transformers transformers,Dispatcher dispatcher, WorkflowInterpreterFactory interpFactory) {
        super(factory);
        this.transformers = transformers;
        this.disp = dispatcher;
        this.interpFactory = interpFactory;
    }

    protected final Transformers transformers;
    protected final Dispatcher disp;
    protected final WorkflowInterpreterFactory interpFactory;
    

    
    /** 
     * annotate the workflow with id numbers. then
     * compile the workflow into python source, create picked interpreter from this, add as extension to workflow.
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl#initializeJob(org.astrogrid.workflow.beans.v1.Workflow)
     *
     */ 
    protected Workflow initializeJob(Workflow job) throws Exception {
        StringWriter originalWorkflowDoc = new StringWriter();        
        Marshaller.marshal(job,originalWorkflowDoc);
        originalWorkflowDoc.close();
        // document / DOM results don't work - more picky, throws a bunch of errors. which isn't nice.
        StringWriter annotatedWorkflowDoc = new StringWriter();
        //Document annotatedWorkflowDoc = XMLUtils.newDocument();        
        transformers.getWorkflowAnnotator().transform(new StreamSource(new StringReader(originalWorkflowDoc.toString())),new StreamResult(annotatedWorkflowDoc));
        Workflow annotatedJob = (Workflow)Unmarshaller.unmarshal(Workflow.class,new StringReader(annotatedWorkflowDoc.toString()));
        // now compile down the original document
        
        StringWriter intermediateResult = new StringWriter();  // xml states.      
        transformers.getCompiler().transform(new StreamSource(new StringReader(originalWorkflowDoc.toString())),new StreamResult(intermediateResult));
        intermediateResult.close();
        StringWriter pythonSource = new StringWriter();
        transformers.getTranslator().transform(new StreamSource(new StringReader(intermediateResult.toString()))
                ,new StreamResult(pythonSource));
        pythonSource.close();
        WorkflowInterpreter interp = interpFactory.newWorkflowInterpreter(pythonSource.toString(),new InterpreterEnvironment(annotatedJob,disp));
        interp.pickleTo(annotatedJob);
        /*
        // store the sources too, just for reference
        Extension intermediateForm = new Extension();
        intermediateForm.setContent(intermediateResult.toString());
        intermediateForm.setKey("workflow.intermediate.form");
        annotatedJob.getJobExecutionRecord().addExtension(intermediateForm);
        
        Extension scriptSource = new Extension();
       scriptSource.setContent(pythonSource.toString());
       scriptSource.setKey("workflow.script.source");
       annotatedJob.getJobExecutionRecord().addExtension(scriptSource);
       */
       return annotatedJob;
    }

    protected void scheduleSteps(Workflow job) {
    WorkflowInterpreter interp = null;
    try {
        interp = interpFactory.unpickleFrom(new InterpreterEnvironment(job,disp));
    } catch (ScriptEngineException e) {
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
        interp.pickleTo(job);
    } catch (ScriptEngineException e) {
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
     *     quite inefficient that we need to recreate interpreter for this. oh well..
     */
    protected void updateStepStatus(Workflow wf, Step step, ExecutionPhase status) {
        super.updateStepStatus(wf, step, status);
        try {
        WorkflowInterpreter interp = interpFactory.unpickleFrom(new InterpreterEnvironment(wf,disp));
        interp.updateStepStatus(step.getId(),status);
        interp.pickleTo(wf);
        } catch (ScriptEngineException e) {
            logger.error("Failed to update step status",e);
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
$Log: ScriptedSchedulerImpl.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/