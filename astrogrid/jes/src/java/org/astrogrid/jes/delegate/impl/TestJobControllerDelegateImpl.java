package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.Delegate;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.workflow.beans.v1.Workflow;

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

    
    public JobURN submitJob(Workflow j) throws JesDelegateException {
        log.info("Test Job Controller: submitting " + j.toString());
        return(new JobURN("jes:job:dummy"));
    }


    /** always returns a 0-length array.
     * @see org.astrogrid.jes.delegate.JobController#listJobs(java.lang.String, java.lang.String)
     */
    public Workflow[] listJobs(String userId, String community) throws JesDelegateException {
        log.info("Test Job Controller: listing jobs for " + userId + ", " + community);
        return new Workflow[]{};
    } // end of submitJob()
    



} // end of class TestDelegateImpl