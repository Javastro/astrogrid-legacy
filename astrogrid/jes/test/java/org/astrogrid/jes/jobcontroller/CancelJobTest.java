/*$Id: CancelJobTest.java,v 1.4 2004/12/03 14:47:40 jdt Exp $
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

import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.jobscheduler.impl.MockSchedulerImpl;
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
        jc.cancelJob(Castor2Axis.convert(urn));
        // test that the request was passed on to the scheduler.
        assertEquals(1,ms.getCallCount());

    }
}


/* 
$Log: CancelJobTest.java,v $
Revision 1.4  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.3.102.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.3  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.2  2004/04/08 14:47:12  nw
added delete and abort job functionality

Revision 1.1  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton
 
*/