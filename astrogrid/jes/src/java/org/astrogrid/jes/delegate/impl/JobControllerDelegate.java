package org.astrogrid.jes.delegate.impl;


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
        

} // end of class JobControllerDelegate