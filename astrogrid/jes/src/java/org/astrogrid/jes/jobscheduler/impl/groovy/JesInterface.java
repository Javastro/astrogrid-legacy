/*$Id: JesInterface.java,v 1.13 2004/11/29 20:00:24 clq2 Exp $
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
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.jes.util.TemporaryBuffer;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.commons.collections.CollectionUtils;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/** provides an interface for executing groovy scripts in rulebase to call back into jes server 
 * also provides some 'helper' methods that can be called from scripts to create usefule objects, etc. Maybe these should be split into a libraru class, and a JesServerINterface class.
 * @modified - added temporary buffers (under soft reference) for capturing system err and out. 
 */
public class  JesInterface extends WorkflowLogger {
    private static final String ENCODING = "UTF-16";
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
    private SoftReference softErrBuff = new SoftReference(new TemporaryBuffer());
    private SoftReference softOutBuff = new SoftReference(new TemporaryBuffer());
    
    
    
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
    public boolean dispatchStep(String id, JesShell shell, ActivityStatusStore states, Map rules)  {
        Step step = (Step)getId(id);
          StepExecutionRecord er = AbstractJobSchedulerImpl.newStepExecutionRecord();
          step.addStepExecutionRecord(er);
           try{
               Tool tool = shell.evaluateTool(step.getTool(),id,states,rules);
               getDispatcher().dispatchStep(getWorkflow(),tool,id); 
               er.setStatus(ExecutionPhase.RUNNING);
               return true;
           } catch (Throwable t) { // absolutely anything
               error("Failed to dispatch step",t,er);
               er.setStatus(ExecutionPhase.ERROR);                      
               debug("Failed to dispatch step",t,er);                 
                return false;
           }                             
    }
    
    /** execute a set activity */
    public boolean executeSet(String id,JesShell shell,ActivityStatusStore map,Map rules)  {
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
    public boolean dispatchScript(String id,JesShell shell,ActivityStatusStore map,Map rules) {
        Script script = (Script)getId(id);        
        StepExecutionRecord er = AbstractJobSchedulerImpl.newStepExecutionRecord();
        script.addStepExecutionRecord(er);
        er.setStatus(ExecutionPhase.RUNNING);

        TemporaryBuffer errB = (TemporaryBuffer)softErrBuff.get();
        if (errB == null) {
            errB = new TemporaryBuffer();
            softErrBuff = new SoftReference(errB);
        }
        
        TemporaryBuffer outB = (TemporaryBuffer)softOutBuff.get();
        if (outB == null) {
            outB = new TemporaryBuffer();
            softOutBuff = new SoftReference(outB);
        }
        
        errB.writeMode();
        outB.writeMode();
        PrintStream errStream = null;
        PrintStream outStream = null;
        try {
            errStream = new PrintStream(errB.getOutputStream(),false,ENCODING);
            outStream = new PrintStream(outB.getOutputStream(),false,ENCODING);            
        } catch (UnsupportedEncodingException e) {
            logger.fatal("JVM doesn't support UTF-16 - which is required by specs",e);
            throw new RuntimeException("JVM doesn't support UTF-16 - which is required by specs",e);
        }

        try { 
            shell.executeScript(script.getBody(),id,map,rules,errStream,outStream);
            er.setStatus(ExecutionPhase.COMPLETED);
            return true;
        } catch (Throwable t) {
            error("Failed to execute script",t,er);
            er.setStatus(ExecutionPhase.ERROR);                   
            debug("Failed to executte script",t,er);
            return false;
        } finally { // want to record results, no matter what happened.
            errStream.close();
            outStream.close();            
            errB.readMode();
            outB.readMode();
           MessageType message =buildMessage(errB.getContents(ENCODING));
           message.setLevel(LogLevel.INFO);
           message.setSource("stderr");
           er.addMessage(message);
           
           message = buildMessage(outB.getContents(ENCODING));
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
Revision 1.13  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.12.12.1  2004/11/25 23:34:34  nw
improved error messages reported from jes

Revision 1.12  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.11.18.1  2004/11/05 16:15:04  nw
uses temporary buffers,
updated to new rulestore type.

Revision 1.11  2004/09/16 21:47:29  nw
made sure all streams are closed

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