package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.JobScheduler;

/**
Abstract implementation of a job scheduler delegate
 */
public abstract class JobSchedulerDelegate extends AbstractDelegate implements JobScheduler{
    public static JobSchedulerDelegate buildDelegate( String targetEndPoint ){
        return buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
    }
    
    /**

     * @param targetEndPoint
     * @param timeout
     * @return
     */
    public static JobSchedulerDelegate buildDelegate( String targetEndPoint
                                                  , int timeout ) { 
            if( AbstractDelegate.isTestDelegateRequired(targetEndPoint) ) {
                 return new TestJobSchedulerDelegateImpl();
            } else {
                return new JobSchedulerDelegateImpl(targetEndPoint, timeout ) ;
            }   
    }      
    

 

} // end of class JobSchedulerDelegate