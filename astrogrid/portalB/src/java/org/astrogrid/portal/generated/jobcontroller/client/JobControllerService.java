/**
 * JobControllerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.generated.jobcontroller.client;

public interface JobControllerService extends javax.xml.rpc.Service {
    public java.lang.String getJobControllerServiceAddress();

    public org.astrogrid.wslink.jobController.JobController getJobControllerService() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.wslink.jobController.JobController getJobControllerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
