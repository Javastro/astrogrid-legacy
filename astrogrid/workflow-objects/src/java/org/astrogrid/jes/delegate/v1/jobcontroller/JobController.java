/**
 * JobController.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.delegate.v1.jobcontroller;

public interface JobController extends java.rmi.Remote {
    public org.astrogrid.jes.types.v1.SubmissionResponse submitJob(java.lang.String workflowXML) throws java.rmi.RemoteException;
    public org.astrogrid.jes.types.v1.WorkflowList readJobList(org.astrogrid.jes.types.v1.ListCriteria criteria) throws java.rmi.RemoteException;
}
