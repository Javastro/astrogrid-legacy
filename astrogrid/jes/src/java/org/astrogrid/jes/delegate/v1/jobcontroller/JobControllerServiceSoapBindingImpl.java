/**
 * JobControllerServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 * 
 * Edited by NWW to construct and then delegate to a {@link org.astrogrid.jes.jobcontroller.JobController}
 *  
 */

package org.astrogrid.jes.delegate.v1.jobcontroller;

import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.job.BeanFacade;

public class JobControllerServiceSoapBindingImpl implements org.astrogrid.jes.delegate.v1.jobcontroller.JobController{
    public JobControllerServiceSoapBindingImpl() {
        facade = ComponentManager.getInstance().getFacade();
        SchedulerNotifier notifier = ComponentManager.getInstance().getNotifier();
        jc = new org.astrogrid.jes.jobcontroller.JobController(facade,notifier);
    }
    protected final BeanFacade  facade;
    protected final org.astrogrid.jes.jobcontroller.JobController jc;
    public org.astrogrid.jes.types.v1.SubmissionResponse submitJob(java.lang.String workflowXML) throws java.rmi.RemoteException {

        return jc.submitJob(workflowXML);  

    }

    public org.astrogrid.jes.types.v1.WorkflowList readJobList(org.astrogrid.jes.types.v1.ListCriteria criteria) throws java.rmi.RemoteException {
        return jc.readJobList(criteria);
    }

}
