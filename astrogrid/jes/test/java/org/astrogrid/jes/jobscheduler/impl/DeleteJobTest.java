/*$Id: DeleteJobTest.java,v 1.1 2004/04/08 14:47:12 nw Exp $
 * Created on 08-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl;

import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Apr-2004
 *
 */
public class DeleteJobTest extends AbstractTestForSchedulerImpl {
    /**
     * Constructor for DeleteJobTest.
     * @param arg0
     */
    public DeleteJobTest(String arg0) {
        super(arg0);
    }

    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        // we know it already exists. 
        js.deleteJob(JesUtil.castor2axis(urn));
        try {
            this.fac.findJob(urn);
            fail("expected not to reach this");
        } catch (NotFoundException e) {
            // expected
        }        
    }
}


/* 
$Log: DeleteJobTest.java,v $
Revision 1.1  2004/04/08 14:47:12  nw
added delete and abort job functionality
 
*/