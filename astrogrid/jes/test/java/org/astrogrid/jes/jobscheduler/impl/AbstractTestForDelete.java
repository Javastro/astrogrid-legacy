/*$Id: AbstractTestForDelete.java,v 1.2 2004/07/30 15:42:34 nw Exp $
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

import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Apr-2004
 *
 */
public abstract class AbstractTestForDelete extends AbstractTestForSchedulerImpl {
    /**
     * Constructor for DeleteJobTest.
     * @param arg0
     */
    public AbstractTestForDelete(String arg0) {
        super(arg0);
    }

    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        // we know it already exists. 
        scheduler.deleteJob(JesUtil.castor2axis(urn));
        try {
            this.fac.findJob(urn);
            fail("expected not to reach this");
        } catch (NotFoundException e) {
            // expected
        }        
    }
}


/* 
$Log: AbstractTestForDelete.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.3  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.2  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.1  2004/04/08 14:47:12  nw
added delete and abort job functionality
 
*/