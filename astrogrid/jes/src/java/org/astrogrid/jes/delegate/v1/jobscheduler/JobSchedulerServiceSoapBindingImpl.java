/**
 * JobSchedulerServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 * 
 * edited by NWW to construct and then delegate to a {@link org.astrogrid.jes.jobscheduler.JobScheduler}
 */

package org.astrogrid.jes.delegate.v1.jobscheduler;

import org.astrogrid.jes.component.ComponentManager;

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.types.v1.JobURN;

import java.rmi.RemoteException;

public class JobSchedulerServiceSoapBindingImpl implements JobScheduler{
    public JobSchedulerServiceSoapBindingImpl() {
        scheduler = ComponentManager.getInstance().getScheduler();
    }
    protected final JobScheduler scheduler;
    /**
     * @see org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler#scheduleNewJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void scheduleNewJob(JobURN request) throws RemoteException {

        scheduler.scheduleNewJob(request);
    }
    /**
     * @see org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler#resumeJob(org.astrogrid.jes.types.v1.JobInfo)
     */
    public void resumeJob(JobIdentifierType id,MessageType jobInfo) throws RemoteException {
        scheduler.resumeJob(id,jobInfo);
    }

}
