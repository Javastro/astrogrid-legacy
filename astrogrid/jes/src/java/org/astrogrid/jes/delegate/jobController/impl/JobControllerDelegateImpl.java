package org.astrogrid.jes.delegate.jobController.impl;

import org.astrogrid.jes.delegate.jobController.* ;
import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException ;
import org.astrogrid.jes.delegate.JesDelegateException ;


/**
 * The <code>JobControllerDelegateImpl</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.3
 */
public class JobControllerDelegateImpl extends JobControllerDelegate {
    
    public static final String 
        SUBMISSION_SUCCESS = "AGJESI00050" ,
        NO_RESPONSE_MESSAGE_RECEIVED = "No response message received from JobController" ;
        
    public JobControllerDelegateImpl( String targetEndPoint ) {
      super( targetEndPoint ) ;
    }
    
    public JobControllerDelegateImpl( String targetEndPoint, int timeout ) {
      super( targetEndPoint, timeout ) ;
    }
    
    public void submitJob(String req) throws JesDelegateException {
        
        String 
            response = null ,
            message = null ;
               
        JobControllerServiceSoapBindingStub 
            binding = null ;
            
        try {
            binding = (JobControllerServiceSoapBindingStub)
                new JobControllerServiceLocator().getJobControllerService( new URL( this.getTargetEndPoint() ) );                        
            binding.setTimeout( this.getTimeout() ) ;    
            response = binding.submitJob(req);
            message = this.extractMessageFromSubmissionResponse( response ) ;
            if( message.indexOf( SUBMISSION_SUCCESS ) == -1) {
                throw new JesDelegateException( message ) ;
            }
        }
        catch( MalformedURLException mex ) {
            throw new JesDelegateException( mex ) ;
        }
        catch( RemoteException rex) {
            rex.printStackTrace() ;
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
        
         
         String 
            request = JobControllerDelegate.formatListRequest( userid, community, communitySnippet, filter ),
            response = null ;
         JobControllerServiceSoapBindingStub 
            binding = null ;
                    
         try {
             binding = (JobControllerServiceSoapBindingStub)
                           new JobControllerServiceLocator().getJobControllerService( new URL( this.getTargetEndPoint() ) );                        
             binding.setTimeout( this.getTimeout() ) ;    
             response = binding.readJobList( request ) ;
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
              
         return response ;
  
     } // end of readJobList()
     
     
     private String extractMessageFromSubmissionResponse( String submissionResponse ) {
         
         String message = null ;
         
         if( submissionResponse != null ) {
             int index = submissionResponse.indexOf( "<message>" ) ;
             if(index != -1) {
                 message = submissionResponse.substring( index + "<message>".length()
                                                       , submissionResponse.indexOf("</message>"));
             }
             else {
                 message = NO_RESPONSE_MESSAGE_RECEIVED ;
             }
         }
         else {
             message = NO_RESPONSE_MESSAGE_RECEIVED ;
         }

         return message ;
         
     } // end of extractMessageFromSubmissionResponse()

} // end of class JobControllerDelegateImpl