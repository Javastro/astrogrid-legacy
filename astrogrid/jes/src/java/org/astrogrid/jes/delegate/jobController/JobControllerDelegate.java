package org.astrogrid.jes.delegate.jobController;

import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException ;
import org.astrogrid.jes.delegate.JesDelegateException ;

/**
 * The <code>JobControllerDelegate</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.2
 */
public class JobControllerDelegate {
    
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

} // end of class JobControllerDelegate