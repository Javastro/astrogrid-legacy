/*$Id: GroovySchedulerImpl.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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

/**
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
     * @param factory
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
        StringWriter originalWorkflowDoc = new StringWriter();        
        Marshaller.marshal(job,originalWorkflowDoc);
        originalWorkflowDoc.close();
        // document / DOM results don't work - more picky, throws a bunch of errors. which isn't nice.
        StringWriter annotatedWorkflowDoc = new StringWriter();
        //Document annotatedWorkflowDoc = XMLUtils.newDocument();        
        transformers.getWorkflowAnnotator().transform(new StreamSource(new StringReader(originalWorkflowDoc.toString())),new StreamResult(annotatedWorkflowDoc));
        Workflow annotatedJob = (Workflow)Unmarshaller.unmarshal(Workflow.class,new StringReader(annotatedWorkflowDoc.toString()));
        // now compile down the original document
        
        StringWriter rules = new StringWriter();  // xml states.      
        transformers.getCompiler().transform(new StreamSource(new StringReader(originalWorkflowDoc.toString())),new StreamResult(rules));
        rules.close();
        //@todo a bit redundant - should be able to add in xml directly - but this is a good sanity check - no point going any further if
        // we can't deserialize the generated rules as an interpreter.
        GroovyInterpreter interp = interpFactory.newInterpreter(rules.toString(),new JesInterface(annotatedJob,disp,this));
        interpFactory.pickleTo(interp,annotatedJob);

       return annotatedJob;
    }

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
     *     quite inefficient that we need to recreate interpreter for this. oh well, at least no script is called.
     */
    protected void updateStepStatus(Workflow wf, Step step, ExecutionPhase status) {
        super.updateStepStatus(wf, step, status);
        try {
        GroovyInterpreter interp = interpFactory.unpickleFrom(new JesInterface(wf,disp,this));
        interp.updateStepStatus(step.getId(),status);
        interpFactory.pickleTo(interp,wf);
        } catch (PickleException e) {
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
$Log: GroovySchedulerImpl.java,v $
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