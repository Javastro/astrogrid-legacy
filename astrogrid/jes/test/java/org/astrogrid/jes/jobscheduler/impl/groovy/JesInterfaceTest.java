/*$Id: JesInterfaceTest.java,v 1.3 2004/08/03 16:32:26 nw Exp $
 * Created on 27-Jul-2004
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
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
 *
 */
public class JesInterfaceTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        jes = new MockJes();
        Workflow wf = jes.getWorkflow();
        wf.setId("1");
        Sequence seq = new Sequence();
        seq.setId("2");
        wf.setSequence(seq);
        Step step = new Step();
        step.setId("3");
        seq.addActivity(step);
    }
    protected JesInterface jes;

    public void testGetWorkflow() {
       assertNotNull(jes.getWorkflow());
    }

    public void testGetDispatcher() {
        assertNotNull(jes.getDispatcher());
    }

    public void testGetLog() {
        assertNotNull(jes.getLog());
    }

    public void testId() {
        assertNull(jes.getId("6"));
        assertNotNull(jes.getId("3"));
    }

    /* a pain to test
    public void testDispatchStepWithId() throws JesException {
        Tool t = new Tool();

        jes.dispatchStep("3",t);
        // take a look
        Step s = (Step)jes.getId("3");
        assertNotNull(s);
        assertEquals(1,s.getStepExecutionRecordCount());
        assertEquals(ExecutionPhase.RUNNING,s.getStepExecutionRecord(0).getStatus());
        assertEquals(1, ((MockDispatcher) jes.disp).getCallCount());
       
        
    }
    */

    public void testCompleteStepWithId() {
        //@todo Implement completeStepWithId().
    }

    public void testSetStatus() {
        //@todo Implement setStatus().
    }

}


/* 
$Log: JesInterfaceTest.java,v $
Revision 1.3  2004/08/03 16:32:26  nw
remove unnecessary envId attrib from rules
implemented variable propagation into parameter values.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

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
 
*/