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

import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.component.JesComponentManagerFactory;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.WorkflowString;
import org.astrogrid.jes.types.v1.WorkflowSummary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.rmi.RemoteException;

public class JobControllerServiceSoapBindingImpl implements org.astrogrid.jes.delegate.v1.jobcontroller.JobController{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JobControllerServiceSoapBindingImpl.class);

    public JobControllerServiceSoapBindingImpl() {
        JobController tmpJc = null;
        try {
            tmpJc = JesComponentManagerFactory.getInstance().getController();
        } catch (Throwable t) {
            logger.fatal("Could not acquire job controller",t);
        }
        jc = tmpJc;
    }
    protected final JobController jc;


    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#submitWorkflow(java.lang.String)
     */
    public JobURN submitWorkflow(WorkflowString arg0) throws RemoteException {
        return jc.submitWorkflow(arg0);
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#cancelJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void cancelJob(JobURN arg0) throws RemoteException {
        jc.cancelJob(arg0);
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#deleteJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void deleteJob(JobURN arg0) throws RemoteException {
        jc.deleteJob(arg0);
    }



    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#readJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public WorkflowString readJob(JobURN arg0) throws RemoteException {
        return jc.readJob(arg0);
    }


    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#readJobList(org.astrogrid.community.beans.v1.axis._Account)
     */
    public WorkflowSummary[] readJobList(_Account arg0) throws RemoteException, JesFault {
        return jc.readJobList(arg0);
    }

}
