package org.astrogrid.jes.delegate.jobController.impl;

import org.astrogrid.jes.delegate.jobController.* ;
import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException ;

import java.util.ListIterator ;
import java.text.MessageFormat ;

import org.xml.sax.* ;
import java.io.StringReader ;
import org.apache.axis.utils.XMLUtils ;
import org.w3c.dom.* ;


import org.astrogrid.jes.delegate.JesDelegateException ;

/**
 * The <code>TestDelegateImpl</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.3
 */
public class TestDelegateImpl extends JobControllerDelegate {
    
    private static final boolean 
        TRACE_ENABLED = true ;
        
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
        
    public TestDelegateImpl( String targetEndPoint ) {
      super( targetEndPoint ) ;
    }
    
    public TestDelegateImpl( String targetEndPoint, int timeout ) {
      super( targetEndPoint, timeout ) ;
    }
    
    public void submitJob(String req) throws JesDelegateException {
        
        JobControllerServiceSoapBindingStub 
            binding = null ;
            
        try {
            binding = (JobControllerServiceSoapBindingStub)
                new JobControllerServiceLocator().getJobControllerService( new URL( this.getTargetEndPoint() ) );                        
            binding.setTimeout( this.getTimeout() ) ;    
            binding.submitJob(req);
        }
        catch( MalformedURLException mex ) {
            throw new JesDelegateException( mex ) ;
        }
        catch( RemoteException rex) {
            throw new JesDelegateException( rex ) ;            
        }
        catch( javax.xml.rpc.ServiceException sex ) {
            throw new JesDelegateException( sex ) ;    
        }
              
        return ;
  
    } // end of submitJob()
    
    
    public String readJobList( String userid
                             , String community
                             , String communitySnippet
                             , String filter ) throws JesDelegateException {
        
         if( TRACE_ENABLED ) trace( "TestDelegateImpl.readJobList() entry") ;    
           
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
                    
         try {
             // Format the list itself ...
            for ( int i=0; i < 10; i++ ) {
                
                  status = "" ;
                
                  inserts[0] = "JobName" + new Integer(i).toString() ;                 
                  inserts[1] = "Description for " +  new Integer(i).toString() ; 
                  inserts[2] = org.astrogrid.jes.job.Job.STATUS_COMPLETED ;           
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
             
              response = MessageFormat.format( RESPONSE_TEMPLATE, inserts ) ;
              
         }
         finally {
             if( TRACE_ENABLED ) trace( "TestDelegateImpl.readJobList() exit") ;  
             if( TRACE_ENABLED ) trace( "response: " + response ) ;     
         }
              
         return response ;
  
     } // end of readJobList()
     
     
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }
     

} // end of class TestDelegateImpl