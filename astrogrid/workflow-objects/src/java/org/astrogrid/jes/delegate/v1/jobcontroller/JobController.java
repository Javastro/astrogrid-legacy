/**
 * JobController.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.delegate.v1.jobcontroller;

public interface JobController extends java.rmi.Remote {
    public org.astrogrid.jes.types.v1.JobURN submitWorkflow(org.astrogrid.jes.types.v1.WorkflowString workflowDocument) throws java.rmi.RemoteException, org.astrogrid.jes.delegate.v1.jobcontroller.JesFault;
    public void cancelJob(org.astrogrid.jes.types.v1.JobURN urn) throws java.rmi.RemoteException;
    public void deleteJob(org.astrogrid.jes.types.v1.JobURN urn) throws java.rmi.RemoteException;
    public org.astrogrid.jes.types.v1.WorkflowSummary[] readJobList(org.astrogrid.community.beans.v1.axis._Account account) throws java.rmi.RemoteException, org.astrogrid.jes.delegate.v1.jobcontroller.JesFault;
    public org.astrogrid.jes.types.v1.WorkflowString readJob(org.astrogrid.jes.types.v1.JobURN urn) throws java.rmi.RemoteException, org.astrogrid.jes.delegate.v1.jobcontroller.JesFault;
}
