/*$Id: GroovyDeleteJobTest.java,v 1.3 2004/12/03 14:47:40 jdt Exp $
 * Created on 18-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Aug-2004
 *
 */
public class GroovyDeleteJobTest extends AbstractTestForSchedulerImpl{

    /** Construct a new GroovyDeleteJobTest
     * @param arg0
     */
    public GroovyDeleteJobTest(String arg0) {
        super(arg0);
    }

    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    protected void performTest(JobURN urn) throws Exception {
        // we know it already exists. 
        scheduler.deleteJob(Castor2Axis.convert(urn));
        try {
            this.fac.findJob(urn);
            fail("expected not to reach this");
        } catch (NotFoundException e) {
            // expected
        }        
    }

}


/* 
$Log: GroovyDeleteJobTest.java,v $
Revision 1.3  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.2.14.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.2  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.1.46.1  2004/11/05 16:08:26  nw
tidied imports

Revision 1.1  2004/08/18 21:50:59  nw
worked on tests
 
*/