package org.astrogrid.jes.delegate.jobController;

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
 * The <code>JobControllerDelegate</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.2
 */
public class JobControllerDelegate {
    
    private static final String
        JOBLIST_TEMPLATE =
        "<?xml version='1.0' encoding='UTF-8'?>" +
        "<joblist userid=\"{0}\" community=\"{1}\" >" +
        "   <filter>{2}</filter>" +        "   {3}" +      // community snippet goes here
        "</joblist>" ;  
    
    private String 
        targetEndPoint = null ;
    private int
        timeout = 60000 ;
        
    public JobControllerDelegate( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;
    }
    
    public JobControllerDelegate( String targetEndPoint, int timeout ) {
      this.targetEndPoint = targetEndPoint ;
      this.timeout = timeout ;
    }
    
    public void submitJob(String req) throws JesDelegateException {
        
        JobControllerServiceSoapBindingStub 
            binding = null ;
            
        try {
            binding = (JobControllerServiceSoapBindingStub)
                new JobControllerServiceLocator().getJobControllerService( new URL( targetEndPoint ) );                        
            binding.setTimeout( timeout ) ;    
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
        
         
         String 
            request = this.formatListRequest( userid, community, communitySnippet, filter ),
            response = null ;
         JobControllerServiceSoapBindingStub 
            binding = null ;
                    
         try {
             binding = (JobControllerServiceSoapBindingStub)
                           new JobControllerServiceLocator().getJobControllerService( new URL( targetEndPoint ) );                        
             binding.setTimeout( timeout ) ;    
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
     
     
     private String formatListRequest( String userid
                                     , String community
                                     , String communitySnippet
                                     , String filter ) {
        
        String
            response = null ;                                
        Object []
            inserts = new Object[4] ;
            
        inserts[0] = userid ;
        inserts[1] = community ;
        inserts[2] = filter ;
        inserts[3] = communitySnippet ;
        response = MessageFormat.format( JOBLIST_TEMPLATE, inserts ) ;
        
        return response  ;
                                            
     } // end of formatListRequest()
    

} // end of class JobControllerDelegate