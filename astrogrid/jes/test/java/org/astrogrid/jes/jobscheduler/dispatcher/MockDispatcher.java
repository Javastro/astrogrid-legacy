/*$Id: MockDispatcher.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 13-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.dispatcher;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.types.v1.JobInfo;
import org.astrogrid.jes.types.v1.Status;

import java.rmi.RemoteException;


/** Mock implementation of a dispatcher.
 * can be configured to fail.

 * @author Noel Winstanley nw@jb.man.ac.uk 13-Feb-2004
 *
 */
public class MockDispatcher implements Dispatcher {
    /** Construct a new MockDispatcher
     * 
     */
    public MockDispatcher() {
        this(true);
    }
    
    public MockDispatcher(boolean willSucceed) {
        this.willSucceed = willSucceed;
    }
    
    /** construct a mock that will send confirmations back through this job monitor */
    public MockDispatcher(JobMonitor jm, boolean willSucceed) {
        this(willSucceed);
        this.monitor = jm;
    }
    protected JobMonitor monitor;   
    protected final boolean willSucceed;
    protected int callCount;
    
    public void setMonitor(JobMonitor jm) {
        this.monitor = jm;
    }
    
    public int getCallCount() {
        return callCount;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.Dispatcher#dispatchStep(java.lang.String, org.astrogrid.jes.job.JobStep)
     */
    public void dispatchStep(String communitySnippet, JobStep js) throws JesException {
        callCount ++;
        if (monitor == null) {
            if (!willSucceed) {
                throw new JesException("You wanted me to barf");
            }
        } else {
            // we've got a monitor, lets talk / barf through that.
            JobInfo info = new JobInfo();
            info.setJobURN(js.getParent().getId());
            info.setStepNumber(js.getStepNumber());
            if (willSucceed) {
                info.setComment("OK");
                info.setStatus(Status.COMPLETED);
            } else {
                info.setComment("You wanted me to fail");
                info.setStatus(Status.ERROR);
            }
            try {
                monitor.monitorJob(info);
            } catch (RemoteException e) {
                throw new JesException("Mock Dispatcher had problems talking to job monitor",e);
            }
        }
    }
}


/* 
$Log: MockDispatcher.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 12:57:11  nw
improved documentation
 
*/