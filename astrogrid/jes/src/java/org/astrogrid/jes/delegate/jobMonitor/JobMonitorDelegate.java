package org.astrogrid.jes.delegate.jobMonitor;

import org.astrogrid.jes.delegate.JesDelegateException ;
import org.astrogrid.jes.delegate.jobMonitor.impl.* ;
import java.text.MessageFormat ;
import java.sql.Timestamp ;
import java.util.Date ;

/**
 * The <code>JobMonitorDelegate</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 19-Dec-2003
 * @since   AstroGrid 1.4
 */
public abstract class JobMonitorDelegate {
    
    public class Status {
            
        private String
            myStatus ;
                   
        private Status() {
        }
                       
        private Status( String someStatus ) {
            this.myStatus = someStatus ;
        }
        
        public String getStatus() {
            return myStatus ;
        }
        
        public String toString() {
             return myStatus ;
         }
            
    }
      
    public final Status
        STATUS_RUNNING = new Status( "RUNNING" ) ,         // Currently executing
        STATUS_COMPLETED = new Status( "COMPLETED" ) ,     // Completed OK
        STATUS_IN_ERROR = new Status( "ERROR" ) ;          // Something bad happened
      
    public static final boolean
        TRACE_ENABLED = true ;
    
    private static final int
        DEFAULT_TIMEOUT = 60000 ;
        
    protected static final String
        JOBMONITOR_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<job name=\"{0}\" jobURN=\"{1}\" userid=\"{2}\" community=\"{3}\" time=\"{4}\" >" +
        "   <jobstep name=\"{5}\" stepNumber=\"{6}\" status=\"{7}\" >" +        "       <comment>{8}</comment>" +        "   </jobstep>" + 
        "</job>" ;          
       
       
    public static JobMonitorDelegate buildDelegate( String targetEndPoint ){
        return JobMonitorDelegate.buildDelegate( targetEndPoint, DEFAULT_TIMEOUT ) ;
    }
    
    
    public static JobMonitorDelegate buildDelegate( String targetEndPoint
                                                  , int timeout ) { 
        if(TRACE_ENABLED) trace( "JobMonitorDelegate.buildDelegate() entry" ) ;
                                                               
        JobMonitorDelegate
            delegate = null ;
         
        try {  
            if( targetEndPoint != null && !targetEndPoint.trim().equals("") ) {
                delegate = new JobMonitorDelegateImpl(targetEndPoint, timeout ) ;
            }
            else {
                delegate = new TestDelegateImpl( targetEndPoint, timeout ) ;
            }
        }
        finally {
            if(TRACE_ENABLED) trace( "JobMonitorDelegate.buildDelegate() exit" ) ;
        }

        return delegate ;
    }       

    private String 
         targetEndPoint = null ;
                
    private int
        timeout = 60000 ;
        
    public JobMonitorDelegate( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;
    }
    
    public JobMonitorDelegate( String targetEndPoint, int timeout ) {
      this.targetEndPoint = targetEndPoint ;
      this.timeout = timeout ;
    }
    
    
    public abstract void monitorJob( String jobURN
                                   , int stepNumber
                                   , Status status
                                   , String comment ) throws JesDelegateException ;
    
    
    public static String formatMonitorRequest( String jobURN
                                             , int stepNumber
                                             , Status status
                                             , String comment ) {
        
       String
           retValue = null ;                                
       Object []
           inserts = new Object[9] ;
            
       inserts[0] = "" ;        // job name
       inserts[1] = jobURN ;    // unique job identifier
       inserts[2] = "" ;        // userid
       inserts[3] = "" ;        // community
       inserts[4] = new Timestamp( new Date().getTime() ).toString() ; // time
       inserts[5] = "" ;        // step name
       inserts[6] = new Integer( stepNumber ).toString() ;
       inserts[7] = status ;    // step status
       inserts[8] = comment ;   // any optional comment
       
       retValue = MessageFormat.format( JOBMONITOR_TEMPLATE, inserts ) ;
        
       debug( "Monitor request: " + retValue ) ;
        
       return retValue  ;
                                            
    } // end of formatMonitorRequest()
    
    
    public void setTargetEndPoint(String targetEndPoint) {
        this.targetEndPoint = targetEndPoint;
    }

    public String getTargetEndPoint() {
        return targetEndPoint;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }    
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

} // end of class JobMonitorDelegate