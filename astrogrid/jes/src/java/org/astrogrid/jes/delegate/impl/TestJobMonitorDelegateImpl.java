package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.beans.v1.Job;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.jobMonitor.JobMonitorDelegate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The <code>TestDelegateImpl</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 19-Dec-2003
 * @version 1.1 05-Jan-2004 - removed dependency on step number
 * @since   AstroGrid 1.4
 */
public class TestJobMonitorDelegateImpl extends JobMonitorDelegate {
        
    public TestJobMonitorDelegateImpl( ){
    }
    
    
    public void monitorJob( Job j)throws JesDelegateException {

            log.info( "Test Delegate: " +j.toString()) ;

    } // end of monitorJob()    
     

     
} // end of class TestDelegateImpl