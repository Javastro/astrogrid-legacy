/*$Id: InterpreterEnvironmentTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
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

import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.TestCase;

/** Exercises methods of the interpreter environment
 * @todo - implement
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jul-2004
 *
 */
public class InterpreterEnvironmentTest extends TestCase {
    /**
     * Constructor for InterpreterEnvironmentTest.
     * @param arg0
     */
    public InterpreterEnvironmentTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        wf = new Workflow();
        wf.setName("Interpreter Environment Test");
        wf.setSequence(new Sequence());

        disp = new MockDispatcher(); 
        env = new InterpreterEnvironment(wf,disp);
    }
    protected InterpreterEnvironment env;
    protected Workflow wf;
    protected MockDispatcher disp;
    public void testInterpreterEnvironment() {
    }
    public void testGetWorkflow() {
        assertNotNull(env.getWorkflow());
        assertEquals(wf,env.getWorkflow());
    }
    public void testGetDispatcher() {
        assertNotNull(env.getDispatcher());
        assertEquals(disp,env.getDispatcher());
    }
    public void testGetLog() {
        assertNotNull(env.getLog());
    }
    public void testFindStepForId() {
        fail("unimplemented test");
    }
    public void testDispatchStepWithId() {

        fail("unimplemented test");        
    }
    public void testCompleteStepWithId() {

        fail("unimplemented test");        
    }
    public void testSetStatus() {

        fail("unimplemented test");        
    }
}


/* 
$Log: InterpreterEnvironmentTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/