package org.astrogrid.jes.delegate.impl;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.delegate.Delegate;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.rmi.RemoteException;

/**
 * Test implementation of a job controller delegate - performs no communication, returns dummy data.
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.3
 */
public class TestJobControllerDelegateImpl extends JobControllerDelegate {


    public TestJobControllerDelegateImpl( ){
        this.targetEndPoint = Delegate.TEST_URI;
    }





    /**
     * @see org.astrogrid.jes.delegate.JobController#submitWorkflow(java.lang.String)
     */
    public JobURN submitWorkflow(Workflow j) throws JesDelegateException {
        log.info("Test Job Controller: submitting " + j.toString());
        JobURN result = new JobURN();
        result.setContent("jes:job:dummy");
        return result;
    }





    /**
     * @see org.astrogrid.jes.delegate.JobController#cancelJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public void cancelJob(JobURN urn) throws JesDelegateException {
        log.info("Test Job Controller: cancelling " + urn.getContent());
    }





    /**
     * @see org.astrogrid.jes.delegate.JobController#deleteJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public void deleteJob(JobURN urn) throws JesDelegateException {
        log.info("Test Job Controller: deleting " + urn.getContent());
    }





    /**
     * @see org.astrogrid.jes.delegate.JobController#readJobList(org.astrogrid.community.beans.v1.Account)
     */
    public JobSummary[] readJobList(Account acc) throws JesDelegateException {
        log.info("Test Job Controller: reading job list ");
        return new JobSummary[]{};
    }





    /**
     * @see org.astrogrid.jes.delegate.JobController#readJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public Workflow readJob(JobURN urn) throws JesDelegateException {
        log.info("Test Job Controller: reading workflow for " + urn.getContent());
        return new Workflow();
    }






} // end of class TestDelegateImpl