/*$Id: JesInterface.java,v 1.10 2004/09/06 16:47:04 nw Exp $
 * Created on 12-May-2004
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
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/** provides an interface for executing groovy scripts in rulebase to call back into jes server 
 * also provides some 'helper' methods that can be called from scripts to create usefule objects, etc. Maybe these should be split into a libraru class, and a JesServerINterface class.
 */
public class  JesInterface extends WorkflowLogger {
    /** construct a new interpreter environment
     * @param wf workflow object
     * @param disp ToolDispatcher
     */
    public JesInterface(Workflow wf, Dispatcher disp,GroovySchedulerImpl sched){
        super(wf);
        this.disp = disp;
        this.sched = sched;    }
    
    protected final GroovySchedulerImpl sched;
    protected final Dispatcher disp;

    
    
    
    /** the workflow object the script belongs to */
    public Workflow getWorkflow() {
        return wf;
    }
    
    /** return list of steps in the workflow document */
    public List getSteps() {
            Iterator i = JesUtil.getJobSteps(wf);
            List l = new ArrayList();
            CollectionUtils.addAll(l,i);
             return l;
        }
    
    /** create a new initialized parameter object */
    public ParameterValue newParameter() {
        ParameterValue pval = new  ParameterValue();
        pval.setIndirect(false);
        pval.setEncoding("");
        return pval;
    }
    
    /** the tool step dispatcher */
    public Dispatcher getDispatcher() {
        return disp;
    }
    // plus some helper methods.

    /** access object in workflow tree by id */
    public Object getId(String id) {        
        if (id.equals(getWorkflow().getId())) {
            return getWorkflow();
        } 
        Object result =  getWorkflow().findXPathValue("//*[id = '" + id + "']");
        if (result != null) {
            return result;
        } else {
            int lastIndex = id.lastIndexOf('-');
            if (lastIndex != -1) {
            // assume its a mangled id. try again, removing last portion.
                return getId(id.substring(0,lastIndex));
            } else {
                return null;
            }
        }
    }
    /** 
     * dispatach a tool step to a cea server for execution. 
     * @param id - the identifier of the step to execute
     * */ 
    public boolean dispatchStep(String id, JesShell shell, ActivityStatusStore states, List rules)  {
        Step step = (Step)getId(id);
          StepExecutionRecord er = AbstractJobSchedulerImpl.newStepExecutionRecord();
          step.addStepExecutionRecord(er);
           try{
               Tool tool = shell.evaluateTool(step.getTool(),id,states,rules);
               getDispatcher().dispatchStep(getWorkflow(),tool,id); 
               er.setStatus(ExecutionPhase.RUNNING);
               return true;
           } catch (Throwable t) { // absolutely anything
               error("Step execution failed:",t);                               
                MessageType message = buildMessage("Failed to dispatch step",t);
                message.setLevel(LogLevel.ERROR);
                er.addMessage(message);                
                er.setStatus(ExecutionPhase.ERROR);
                er.setFinishTime(new Date());
                return false;
           }                             
    }
    
    /** execute a set activity */
    public boolean executeSet(String id,JesShell shell,ActivityStatusStore map,List rules)  {
        Set set = (Set)getId(id);
        try {
            shell.executeSet(set,id,map,rules);
            return true;
        } catch (Throwable t) {
            error("set " + set.getVar() + ":= " + set.getValue() + " failed",t);
            return false;
        }
    }    
    /** dispatch / execute a script activity */
    public boolean dispatchScript(String id,JesShell shell,ActivityStatusStore map,List rules) {
        Script script = (Script)getId(id);        
        StepExecutionRecord er = AbstractJobSchedulerImpl.newStepExecutionRecord();
        script.addStepExecutionRecord(er);
        er.setStatus(ExecutionPhase.RUNNING);

        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();        
        try {
            PrintStream errStream = new PrintStream(err);
            PrintStream outStream = new PrintStream(out);
            shell.executeScript(script.getBody(),id,map,rules,errStream,outStream);
            errStream.close();
            outStream.close();
            er.setStatus(ExecutionPhase.COMPLETED);
            return true;
        } catch (Throwable t) {
            error("Script execution failed:",t);                 
            MessageType message = buildMessage("Failed to execute script",t);
            message.setLevel(LogLevel.ERROR);
            er.addMessage(message);
            er.setStatus(ExecutionPhase.ERROR);       
            return false;
        } finally { // want to record results, no matter what happened.
           MessageType message =buildMessage(err.toString());
           message.setLevel(LogLevel.INFO);
           message.setSource("stderr");
           er.addMessage(message);
           
           message = buildMessage(out.toString());
           message.setLevel(LogLevel.INFO);
           message.setSource("stdout");
           er.addMessage(message);
          er.setFinishTime(new Date());
        }
    }

    
    /** records most important events back into main workflow document. */
    public void setWorkflowStatus(Status status) {
                ExecutionPhase phase = Status.toPhase(status);
                JobExecutionRecord er =  wf.getJobExecutionRecord();
                er.setStatus(phase);
                if (phase.equals(ExecutionPhase.COMPLETED) || phase.equals(ExecutionPhase.ERROR)){
                    wf.getJobExecutionRecord().setFinishTime(new Date());
                    logger.info("Job " + er.getJobId().getContent() + " finished with status " + er.getStatus());
                    sched.notifyJobFinished(wf);
                }           
    }
    

}

/* 
$Log: JesInterface.java,v $
Revision 1.10  2004/09/06 16:47:04  nw
javadoc

Revision 1.9  2004/08/18 21:50:15  nw
improved error propagation and reporting.
messages are now logged to workflow document

Revision 1.8  2004/08/13 09:10:30  nw
tidied imports

Revision 1.7  2004/08/09 17:34:10  nw
implemented parfor.
removed references to rulestore

Revision 1.6  2004/08/06 11:59:12  nw
added helper methods for scripts manipulating the workflow document itself.

Revision 1.5  2004/08/04 16:51:46  nw
added parameter propagation out of cea step call.

Revision 1.4  2004/08/03 16:32:26  nw
remove unnecessary envId attrib from rules
implemented variable propagation into parameter values.

Revision 1.3  2004/08/03 14:27:38  nw
added set/unset/scope features.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.4  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.3  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.2  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1.2.1  2004/07/27 23:37:59  nw
refactoed framework.
experimented with betwixt - can't get it to work.
got XStream working in 5 mins.
about to remove betwixt code.

Revision 1.1.2.1  2004/07/26 15:51:19  nw
first stab at a groovy scheduler.
transcribed all the classes in the python prototype, and took copies of the
classes in the 'scripting' package.

Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/