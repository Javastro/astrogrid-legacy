/*$Id: WorkflowInterpreter.java,v 1.1 2004/07/09 09:30:28 nw Exp $
 * Created on 11-May-2004
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
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.Extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.python.core.PyDictionary;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import java.io.StringWriter;

/** Class that wraps the gory details of interacting with the python interpreter. 
 * may form an interface eventually,a facade that different languages can hide behind.  
 * @author Noel Winstanley nw@jb.man.ac.uk 11-May-2004
 */
public class WorkflowInterpreter {
    private  static final Log logger = LogFactory.getLog(WorkflowInterpreter.class);
    
    public static final String EXTENSION_KEY = "pickled.python.interpreter";
    static final String EXTENSION_XPATH = "jobExecutionRecord/extension[key='" +  EXTENSION_KEY+ "']";
    
    WorkflowInterpreter(PySystemState state) {
        this.pyInterpreter= new PythonInterpreter(new PyDictionary(),state);
    }   
    
    protected final PythonInterpreter pyInterpreter;
  //  protected final InterpreterEnvironment env;
    
    /** could internalize this too ... */
    public void pickleTo(Workflow wf) throws ScriptEngineException{
        // see if its already there first..
        Extension pickleJar = (Extension)wf.findXPathValue(EXTENSION_XPATH);
        if (pickleJar == null) {
            pickleJar = new Extension();
            pickleJar.setKey("pickled.python.interpreter");
            wf.getJobExecutionRecord().addExtension(pickleJar);
        }
        pickleJar.setContent(this.pickle());
    }
    
    protected String pickle() throws ScriptEngineException {
        StringWriter writer = new StringWriter();
        pyInterpreter.set("_stream",writer);     
        try {
            pyInterpreter.exec("interp.pickle(_stream)");   
        return writer.toString();
        } catch (Exception e) {
            throw new ScriptEngineException("Failed to pickle interpreter",e);        
    } 
        
    }
    
    public void updateStepStatus(String id, ExecutionPhase phase) throws ScriptEngineException {
        String state = "";
        if (phase.equals(ExecutionPhase.COMPLETED)){
            state = "FINISH"; // need constants for these somewhere..
        } else if (phase.equals(ExecutionPhase.ERROR)){
            state = "ERROR";
        } else {
            // nothing we care about. 
            return;
        }
        try {
            pyInterpreter.exec("interp.updateStatus('" + id + "', '" + state + "')");
        } catch (Exception e) {
            throw new ScriptEngineException("Could not update step status",e);
        }        
    }
    
    public void run()  throws ScriptEngineException {
        try {
            pyInterpreter.exec("interp.run()");
        } catch(Exception e) {
            throw new ScriptEngineException("Failed to run interpreter",e);
        }
    }
    
    
}


/* 
$Log: WorkflowInterpreter.java,v $
Revision 1.1  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/