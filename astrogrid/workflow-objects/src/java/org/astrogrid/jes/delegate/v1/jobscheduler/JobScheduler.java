/**
 * JobScheduler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.delegate.v1.jobscheduler;

public interface JobScheduler extends java.rmi.Remote {

    // call to schedule a new job
    public void scheduleNewJob(org.astrogrid.jes.types.v1.JobURN request) throws java.rmi.RemoteException;

    // call to resume execution of a job, after tool completes.
    // 
    public void resumeJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType id, org.astrogrid.jes.types.v1.cea.axis.MessageType feedback) throws java.rmi.RemoteException;
}
