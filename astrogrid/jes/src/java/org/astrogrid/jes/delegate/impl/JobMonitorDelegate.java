package org.astrogrid.jes.delegate.impl;


/**
 * Abstract implementation of a job monitor delegate
 */
public abstract class JobMonitorDelegate   extends AbstractDelegate implements org.astrogrid.jes.delegate.JobMonitor{

    public static JobMonitorDelegate buildDelegate( String targetEndPoint ){
        return JobMonitorDelegate.buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
    }
    
    /**

     * @param targetEndPoint
     * @param timeout
     * @return
     */
    public static JobMonitorDelegate buildDelegate( String targetEndPoint
                                                  , int timeout ) { 
            if( AbstractDelegate.isTestDelegateRequired(targetEndPoint) ) {
                 return new TestJobMonitorDelegateImpl();
            } else {
                return new JobMonitorDelegateImpl(targetEndPoint, timeout ) ;
            }   
    }       
    

  

} // end of class JobMonitorDelegate