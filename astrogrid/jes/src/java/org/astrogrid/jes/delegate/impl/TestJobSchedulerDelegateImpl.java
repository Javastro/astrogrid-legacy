/*$Id: TestJobSchedulerDelegateImpl.java,v 1.3 2004/02/27 00:46:03 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate.impl;
import org.astrogrid.jes.delegate.Delegate;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.types.v1.JobInfo;
import org.astrogrid.jes.types.v1.JobURN;

/**
 * Dummy implementation of a job scheduler - does nothing.
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public class TestJobSchedulerDelegateImpl extends JobSchedulerDelegate {

    public TestJobSchedulerDelegateImpl(){
        this.targetEndPoint = Delegate.TEST_URI;
    }


    /**
     * @see org.astrogrid.jes.delegate.JobScheduler#scheduleNewJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void scheduleNewJob(JobURN j) throws JesDelegateException {
        log.info("Scheduling new job " + j.toString());
    }

    /**
     * @see org.astrogrid.jes.delegate.JobScheduler#resumeJob(org.astrogrid.jes.types.v1.JobInfo)
     */
    public void resumeJob(JobInfo info) throws JesDelegateException {
        log.info("Resuming job " + info.getJobURN().toString());
    }
}


/* 
$Log: TestJobSchedulerDelegateImpl.java,v $
Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.2.5  2004/02/19 13:40:49  nw
updated to match new interfaces for scheduler

Revision 1.2.2.4  2004/02/17 12:40:30  nw
Initialized targetEndpoint to TEST_URI

Revision 1.2.2.3  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.2.2.2  2004/02/17 11:00:15  nw
altered delegate interfaces to fit strongly-types wsdl2java classes

Revision 1.2.2.1  2004/02/11 16:09:10  nw
refactored delegates (again)

Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/