package org.astrogrid.jes.delegate.jobMonitor;

import org.astrogrid.jes.beans.v1.Job;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JobMonitor;
import org.astrogrid.jes.delegate.impl.AbstractDelegate;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegateImpl;
import org.astrogrid.jes.delegate.impl.TestJobControllerDelegateImpl;
import org.astrogrid.jes.delegate.impl.TestJobMonitorDelegateImpl;

import org.exolab.castor.xml.CastorException;

import java.io.StringReader;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

/**
 * The <code>JobMonitorDelegate</code> class. 
 *
 * @deprecated use {@link org.astrogrid.jes.delegate.JesDelegateFactory} instead
 * @author  Jeff Lusted
 * @version 1.0 19-Dec-2003
 * @version 1.1 05-Jan-2004 - removed dependency on step number
 * @since   AstroGrid 1.4
 */
public abstract class JobMonitorDelegate  extends AbstractDelegate implements org.astrogrid.jes.delegate.JobMonitor{
    
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

    protected static final String
        JOBMONITOR_TEMPLATE =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<job name=\"{0}\" jobURN=\"{1}\" userid=\"{2}\" community=\"{3}\" time=\"{4}\" >" +
        "   <jobstep name=\"{5}\" stepNumber=\"{6}\" status=\"{7}\" >" +        "       <comment>{8}</comment>" +        "   </jobstep>" + 
        "</job>" ;          
       
       
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
    
    public abstract void monitorJob(Job jobToMonitor) throws JesDelegateException;
    
    /** @deprecated - use oo-based calls instead */
    public final void monitorJob( String jobURN , Status status, String comment ) throws JesDelegateException {
        // create job, pass to new call.
        String str = formatMonitorRequest(jobURN,status,comment);
        try {
            Job job = Job.unmarshal(new StringReader(str));
            monitorJob(job);
        } catch (CastorException e) {
            throw new JesDelegateException("Could not marshall old request into new object model",e);      
    }
    }
    
    /**
     * @deprecated will go real soon now. use oo-based calls instead
     * @param jobURN
     * @param status
     * @param comment
     * @return
     */
    public static String formatMonitorRequest( String jobURN
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
       inserts[6] = jobURN.substring( jobURN.lastIndexOf(':')+1 ) ;
       inserts[7] = status ;    // step status
       inserts[8] = comment ;   // any optional comment
       
       return MessageFormat.format( JOBMONITOR_TEMPLATE, inserts ) ;
                                          
    } // end of formatMonitorRequest()
    
    
    

} // end of class JobMonitorDelegate