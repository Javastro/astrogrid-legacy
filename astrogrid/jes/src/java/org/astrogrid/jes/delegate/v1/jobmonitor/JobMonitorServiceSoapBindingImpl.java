/**
 * JobMonitorServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 * 
 * Edited by NWW to construt and then delegate to a {@link org.astrogrid.jes.jobmonitor.JobMonitor}
 */

package org.astrogrid.jes.delegate.v1.jobmonitor;

import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import java.rmi.RemoteException;

public class JobMonitorServiceSoapBindingImpl implements org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor{
    public JobMonitorServiceSoapBindingImpl() {
        SchedulerNotifier notifier = ComponentManager.getInstance().getNotifier();
        monitor = new org.astrogrid.jes.jobmonitor.JobMonitor(notifier);
    }
    protected final org.astrogrid.jes.jobmonitor.JobMonitor monitor;

    /**
     * @see org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor#monitorJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public void monitorJob(JobIdentifierType arg0, MessageType arg1) throws RemoteException {
        monitor.monitorJob(arg0,arg1);
    }


}
