package org.astrogrid.jes.delegate.jobMonitor.impl;

import org.astrogrid.jes.delegate.jobMonitor.* ;
import org.astrogrid.jes.delegate.JesDelegateException ;

/**
 * The <code>TestDelegateImpl</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 19-Dec-2003
 * @since   AstroGrid 1.
 */
public class TestDelegateImpl extends JobMonitorDelegate {
    
    private static final boolean 
        TRACE_ENABLED = true ; 
        
    public TestDelegateImpl( String targetEndPoint ) {
      super( targetEndPoint ) ;
    }
    
    public TestDelegateImpl( String targetEndPoint, int timeout ) {
      super( targetEndPoint, timeout ) ;
    }
    
    public void monitorJob( String jobURN
                           , int stepNumber
                           , Status status ) throws JesDelegateException {
        this.monitorJob( jobURN, stepNumber, status, "" ) ;                           
    }
    
    
    public void monitorJob( String jobURN
                          , int stepNumber
                          , Status status
                          , String comment ) throws JesDelegateException {
        if(TRACE_ENABLED) trace( "TestDelegateImpl.monitorJob() entry" ) ;
                  
            
        try {
            debug( "jobURN: " + jobURN ) ;
            debug( "stepNumber: " + stepNumber ) ;
            debug( "status: " + status ) ;
            debug( "comment: " + comment ) ;
        }
        finally {
            if(TRACE_ENABLED) trace( "TestDelegateImpl.monitorJob() exit" ) ;   
        }
              
        return ;
  
    } // end of monitorJob()    
     
     
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }
     
} // end of class TestDelegateImpl