/*$Id: InterpreterEnvironment.java,v 1.1 2004/07/09 09:30:28 nw Exp $
 * Created on 12-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;


/** provides an exection environment for the scripts in the interprter - ie. hooks back into java from the script */
public class  InterpreterEnvironment {
    /** construct a new interpreter environment
     * @param wf workflow object
     * @param disp ToolDispatcher
     */
    public InterpreterEnvironment(Workflow wf, Dispatcher disp){
        this.wf = wf;
        this.disp = disp;
        this.logger = LogFactory.getLog("script:  " + wf.getName());    }
    
    protected final Workflow wf;
    protected final Dispatcher disp;
    protected final Log logger;

    /** the workflow object the script belongs to */
    public Workflow getWorkflow() {
        return wf;
    }
    /** the tool step dispatcher */
    public Dispatcher getDispatcher() {
        return disp;
    }
    /** a logger for the script to log information messages to */
    public Log getLog() {
        return logger;
    }
    
    // plus some helper methods.
    /** find the step associated with an ID - based on it's id= attribute */
    public Step findStepForId(String id) {
        return (Step)getId(id);
    }
    private Object getId(String id) {
        if (id.equals(getWorkflow().getId())) {
            return getWorkflow();
        } 
        return getWorkflow().findXPathValue("//*[id = '" + id + "']");
    }
    /** @todo evaluate params before calling tool 
     * dispatach a tool step 
     * @param id - the identifier of the step to execute
     * */ 
    public void dispatchStepWithId(String id) throws JesException {
        Step step = findStepForId(id);
          StepExecutionRecord er = AbstractJobSchedulerImpl.newStepExecutionRecord();
          step.addStepExecutionRecord(er);
           try{
               getDispatcher().dispatchStep(getWorkflow(),step);
               er.setStatus(ExecutionPhase.RUNNING);
           } catch (Throwable t) { // absolutely anything
               getLog().info("Step execution failed:",t);                 
                MessageType message = new MessageType();
                StringBuffer buff = new StringBuffer();
               buff.append("Failed: ")
                   .append( t.getClass().getName())
                   .append('\n')
                   .append( t.getMessage())
                   .append('\n');
                StringWriter writer = new StringWriter();                            
                   t.printStackTrace(new PrintWriter(writer));
                 buff.append(writer.toString());
                  
                message.setContent(buff.toString());
                message.setLevel(LogLevel.ERROR);
                message.setSource("Dispatcher");
                message.setPhase(ExecutionPhase.ERROR);
                message.setTimestamp(new Date());
                er.addMessage(message);
                er.setStatus(ExecutionPhase.ERROR);
                er.setFinishTime(new Date());
           }                             
    }
    
    /** @todo - load params back into interp */
    public void completeStepWithId(String id) throws JesException {
    }
    
    public void setStatus(String id,String status) {
        // first see if its a step.
        Object o = getId(id);
            if (o instanceof Workflow) {
                Workflow wf = (Workflow) o;
                ExecutionPhase phase = statusToPhase(status);
                wf.getJobExecutionRecord().setStatus(phase);
                if (phase.equals(ExecutionPhase.COMPLETED)){
                    wf.getJobExecutionRecord().setFinishTime(new Date());
                }
               // this.notifyJobFinished(job);                
            }
       
    }
    
    private ExecutionPhase statusToPhase(String status) {
        if (status.equals("UNSTARTED")) {
            return ExecutionPhase.PENDING;
        }
        if (status.equals("START")) {
            return ExecutionPhase.INITIALIZING;
        }
        if (status.equals("STARTED")) {
            return ExecutionPhase.RUNNING;
        }
        if (status.equals("FINISH")) {
            return ExecutionPhase.RUNNING;
        }
        if (status.equals("FINISHED")) {
            return ExecutionPhase.COMPLETED;
        }
        if (status.equals("ERROR")) {
            return ExecutionPhase.ERROR;
        }
        return ExecutionPhase.UNKNOWN;
    }
}

/* 
$Log: InterpreterEnvironment.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/