/*$Id: CancelJobTest.java,v 1.2 2004/04/08 14:47:12 nw Exp $
 * Created on 08-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobcontroller;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.jobscheduler.impl.MockSchedulerImpl;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** test behaviour of cancel job
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class CancelJobTest extends AbstractTestForJobController {
    public CancelJobTest(String s) {
        super(s);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        MockSchedulerImpl ms = (MockSchedulerImpl)nudger;
        ms.resetCallCount();      
        jc.cancelJob(JesUtil.castor2axis(urn));
        // test that the request was passed on to the scheduler.
        assertEquals(1,ms.getCallCount());

    }
}


/* 
$Log: CancelJobTest.java,v $
Revision 1.2  2004/04/08 14:47:12  nw
added delete and abort job functionality

Revision 1.1  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton
 
*/