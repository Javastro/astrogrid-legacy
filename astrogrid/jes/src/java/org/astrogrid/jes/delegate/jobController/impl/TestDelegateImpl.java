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

} // end of class TestDelegateImpl