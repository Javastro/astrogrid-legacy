/**
 * JobSchedulerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.jobScheduler;

public interface JobSchedulerService extends javax.xml.rpc.Service {
    public java.lang.String getJobSchedulerServiceAddress();

    public org.astrogrid.wslink.jobScheduler.JobScheduler getJobSchedulerService() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.wslink.jobScheduler.JobScheduler getJobSchedulerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
