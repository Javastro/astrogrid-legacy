/*$Id: DeleteJobTest.java,v 1.1 2004/03/09 14:23:54 nw Exp $
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

import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** test bejaviour of delete job
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class DeleteJobTest extends AbstractTestForJobController {
    public DeleteJobTest(String s) {
        super(s);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        jc.deleteJob(JesUtil.castor2axis(urn));
        // check its been cancelled. - should be present, and marked as completed.
        try {
            Workflow wf = fac.findJob(urn);
            fail("expected to barf");
        } catch (NotFoundException e) {
            // expected.
        }
    }
}


/* 
$Log: DeleteJobTest.java,v $
Revision 1.1  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton
 
*/