package org.astrogrid.jes.delegate.impl;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;


/**
 * Abstract class for a job controller delegate.
 */
public abstract class JobControllerDelegate extends AbstractDelegate implements org.astrogrid.jes.delegate.JobController {

    public static JobControllerDelegate buildDelegate( String targetEndPoint ){
        return JobControllerDelegate.buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
    }
    
    
    public static JobControllerDelegate buildDelegate( String targetEndPoint
                                                     , int timeout ) { 

            if( AbstractDelegate.isTestDelegateRequired(targetEndPoint)){
                return new TestJobControllerDelegateImpl( ) ;
            } else {
                return new JobControllerDelegateImpl(targetEndPoint, timeout ) ;                
            }
    }
    
    
        
    /** @deprecated  implemented in terms of listJobs*/
    public final JobSummary[] readJobList(Account acc) throws JesDelegateException {
        WorkflowSummaryType[] arr = listJobs(acc);
        JobSummary[] results = new JobSummary[arr.length];
        for (int i = 0; i < arr.length; i++) {
            results[i] = new JobSummary(arr[i]);
        }
        return results;
    }
} // end of class JobControllerDelegate