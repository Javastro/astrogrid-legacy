/*$Id: WorkflowInterpreterTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;

import junit.framework.TestCase;

/** tests for the workflow interpreter.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-May-2004
 *
 */
public class WorkflowInterpreterTest extends TestCase {

    /**
     * Constructor for WorkflowInterpreterTest.
     * @param arg0
     */
    public WorkflowInterpreterTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        fac = new WorkflowInterpreterFactory(new DevelopmentJarPaths());
        Workflow wf = new Workflow();
        wf.setName("Workflow Interpreter Test");
        env  = new InterpreterEnvironment(wf,null) ;
        interp = fac.newWorkflowInterpreter("import workflows; interp = workflows.Interpreter()",env);

    }
    protected WorkflowInterpreter interp;
    protected WorkflowInterpreterFactory fac;
    protected  InterpreterEnvironment env;
    
    public void testInitialEnvironment() {
       // interp.pyInterpreter.exec("import jes");
        assertEquals("0",interp.pyInterpreter.eval("_jes.log.debugEnabled").toString());
    }
    
    
    public void testPickling() throws Exception {
        // add something to discriminate by.
        interp.pyInterpreter.exec("interp.foo = 1");
        final Workflow wf = new Workflow();
        wf.setJobExecutionRecord(new JobExecutionRecord());
        interp.pickleTo(wf);
        assertEquals(wf.getJobExecutionRecord().getExtensionCount(),1);
        //try recreating.
        InterpreterEnvironment env = new InterpreterEnvironment(wf,null); 
        WorkflowInterpreter interp1 = fac.unpickleFrom(env);
        assertEquals("1",interp1.pyInterpreter.eval("interp.foo").toString());
        
    }
}


/* 
$Log: WorkflowInterpreterTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/