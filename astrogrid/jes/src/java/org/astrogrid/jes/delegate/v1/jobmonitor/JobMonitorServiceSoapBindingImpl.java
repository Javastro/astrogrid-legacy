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

public class JobMonitorServiceSoapBindingImpl implements org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor{
    public JobMonitorServiceSoapBindingImpl() {
        SchedulerNotifier notifier = ComponentManager.getInstance().getNotifier();
        monitor = new org.astrogrid.jes.jobmonitor.JobMonitor(notifier);
    }
    protected final org.astrogrid.jes.jobmonitor.JobMonitor monitor;
    public void monitorJob(org.astrogrid.jes.types.v1.JobInfo jobInfo) throws java.rmi.RemoteException {
        monitor.monitorJob(jobInfo);
    }

}
