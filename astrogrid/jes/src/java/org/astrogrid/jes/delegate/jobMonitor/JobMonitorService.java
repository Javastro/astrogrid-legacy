/**
 * JobMonitorService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.delegate.jobMonitor;

public interface JobMonitorService extends javax.xml.rpc.Service {
    public java.lang.String getJobMonitorServiceAddress();

    public org.astrogrid.jes.delegate.jobMonitor.JobMonitor getJobMonitorService() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.jes.delegate.jobMonitor.JobMonitor getJobMonitorService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
