/**
 * JobMonitorServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 * 
 * Edited by NWW to construt and then delegate to a {@link org.astrogrid.jes.jobmonitor.JobMonitor}
 */

package org.astrogrid.jes.delegate.v1.jobmonitor;

import org.astrogrid.jes.component.JesComponentManagerFactory;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

public class JobMonitorServiceSoapBindingImpl implements org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JobMonitorServiceSoapBindingImpl.class);

    public JobMonitorServiceSoapBindingImpl() {
        JobMonitor tmpMonitor = null;
        try {
        tmpMonitor = JesComponentManagerFactory.getInstance().getMonitor();
        } catch (Throwable t) {
            logger.fatal("Could not acquire monitor service",t);
        }
        monitor = tmpMonitor;
    }
    protected final JobMonitor monitor;

    /**
     * @see org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor#monitorJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public void monitorJob(JobIdentifierType arg0, MessageType arg1) throws RemoteException {
        monitor.monitorJob(arg0,arg1);
    }


}
