/*$Id: ReadJobSuccessTest.java,v 1.2 2004/12/03 14:47:40 jdt Exp $
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
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.StringReader;

/** test successful behaviour of reading a job.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 *
 */
public class ReadJobSuccessTest extends AbstractTestForJobController {
    /** Construct a new ReadJobSuccessTest
     * @param s
     */
    public ReadJobSuccessTest(String s) {
        super(s);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        String result = jc.readJob(Castor2Axis.convert(urn)).getValue();
        assertNotNull(result);
        assertTrue(result.trim().length() > 0); // ie. there's something there.
        StringReader sr = new StringReader(result);
        Workflow w = Workflow.unmarshalWorkflow(sr);
        assertNotNull(w);
        assertEquals(urn.getContent(),w.getJobExecutionRecord().getJobId().getContent());
    }
}


/* 
$Log: ReadJobSuccessTest.java,v $
Revision 1.2  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.1.128.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.1  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton
 
*/