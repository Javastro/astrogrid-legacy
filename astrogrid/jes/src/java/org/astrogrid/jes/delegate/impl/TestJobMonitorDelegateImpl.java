package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.delegate.Delegate;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;



/**

    Dummy implementation of a job monitor delegate - does nothing.
 */
public class TestJobMonitorDelegateImpl extends JobMonitorDelegate {
        
    public TestJobMonitorDelegateImpl( ){
        this.targetEndPoint = Delegate.TEST_URI;
    }
    
 
    /**
     * @see org.astrogrid.jes.delegate.JobMonitor#monitorJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public void monitorJob(JobIdentifierType id, MessageType info) throws JesDelegateException {
        log.info( "Test Delegate: " +id.toString()) ;
    } // end of monitorJob()    
     

     
} // end of class TestDelegateImpl