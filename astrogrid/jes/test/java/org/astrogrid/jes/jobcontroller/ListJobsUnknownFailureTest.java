/*$Id: ListJobsUnknownFailureTest.java,v 1.1 2004/03/09 14:23:54 nw Exp $
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

import org.astrogrid.community.beans.v1.axis.Identifier;
import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.types.v1.WorkflowSummary;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** test behaviour on querying with unknown user.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class ListJobsUnknownFailureTest extends AbstractTestForJobController {
    /** Construct a new ListJobsUnknownFailureTest
     * @param s
     */
    public ListJobsUnknownFailureTest(String s) {
        super(s);
    }
    protected void setUp() throws Exception {
        super.setUp();
        this.acc = new _Account();
        acc.setCommunity(new Identifier("UNKWOWN"));
        acc.setName(new Identifier("NOONE"));
    }
    protected _Account acc;
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(JobURN urn) throws Exception {

        WorkflowSummary[] wl = jc.readJobList(acc);
        assertNotNull(wl);
        assertEquals(0,wl.length);
    }

}


/* 
$Log: ListJobsUnknownFailureTest.java,v $
Revision 1.1  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton
 
*/