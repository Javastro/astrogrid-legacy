package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.Delegate;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.types.v1.JobInfo;

/**

    Dummy implementation of a job monitor delegate - does nothing.
 */
public class TestJobMonitorDelegateImpl extends JobMonitorDelegate {
        
    public TestJobMonitorDelegateImpl( ){
        this.targetEndPoint = Delegate.TEST_URI;
    }
    
    
    public void monitorJob( JobInfo j )throws JesDelegateException {

            log.info( "Test Delegate: " +j.toString()) ;

    } // end of monitorJob()    
     

     
} // end of class TestDelegateImpl