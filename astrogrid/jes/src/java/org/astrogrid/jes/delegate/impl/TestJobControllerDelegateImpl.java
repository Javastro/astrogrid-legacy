package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.beans.v1.Job;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.jobController.JobControllerDelegate;

import java.text.MessageFormat;

/**
 * The <code>TestDelegateImpl</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.3
 */
public class TestJobControllerDelegateImpl extends JobControllerDelegate {

    public static final String
        RESPONSE_TEMPLATE = 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<response>" +
            "   <message>{0} </message>" +
            "   <joblist userid=\"{1}\" community=\"{2}\" >" +
            "       {3}" +
            "   </joblist>" +
            "</response>" ;
        
    public static final String
        JOB_TEMPLATE = 
            "<job name=\"{0}\" >" +
            "   <description>{1}</description>" +
            "   <status>{2}</status>" +
            "   <time>{3}</time>" +
            "   <jobid>{4}</jobid>" +
            "</job>" ;        
        
    public TestJobControllerDelegateImpl( ){
    }

    
    public void submitJob(Job j) throws JesDelegateException {
        log.info("Test Job Controller: submitting " + j.toString());
  
    } // end of submitJob()
    
    
    public String readJobList( String userid
                             , String community
                             , String communitySnippet
                             , String filter ) throws JesDelegateException {
        
           
         String 
            request = JobControllerDelegate.formatListRequest( userid
                                                             , community
                                                             , communitySnippet
                                                             , filter ),
            response = null ;
            
         StringBuffer
            rBuffer = new StringBuffer(512) ;

         Object []
            inserts = new Object[ 5 ] ;
            
         String
            status ;

             // Format the list itself ...
            for ( int i=0; i < 10; i++ ) {
                
                switch( i%4 ) {
                    
                    case 0:
                        status = org.astrogrid.jes.job.Job.STATUS_INITIALIZED ;
                        break ;
                    case 1:
                         status = org.astrogrid.jes.job.Job.STATUS_RUNNING ;
                         break ;
                    case 2:
                         status = org.astrogrid.jes.job.Job.STATUS_COMPLETED ;
                         break ;
                    case 3:
                         status = org.astrogrid.jes.job.Job.STATUS_IN_ERROR ;
                         break ;    
                        
                    default:
                        status = "unknown status" ;
                        break ;
                    
                }
                
                  inserts[0] = "JobName" + new Integer(i).toString() ;                 
                  inserts[1] = "Description for " +  new Integer(i).toString() ; 
                  inserts[2] = status ;           
                  inserts[3] = new java.util.Date() ;
                  inserts[4] = userid + ":" + community + ":JES@Leicester:" + "1298376" + i ; 
                
                  rBuffer.append( MessageFormat.format( JOB_TEMPLATE, inserts ) ) ;
                 
             } // end for
             
             // Format the header details ...
              inserts = new Object[ 4 ] ;
              inserts[0] = "" ; // no message                 
              inserts[1] = userid ;           
              inserts[2] = community ;
              inserts[3] = rBuffer.toString() ;
             
              return MessageFormat.format( RESPONSE_TEMPLATE, inserts ) ;
              



  
     } // end of readJobList()
     


} // end of class TestDelegateImpl