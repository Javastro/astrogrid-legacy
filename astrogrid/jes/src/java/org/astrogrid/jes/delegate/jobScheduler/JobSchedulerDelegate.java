package org.astrogrid.jes.delegate.jobScheduler;

import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException ;
import org.astrogrid.jes.delegate.JesDelegateException ;

/**
 * The <code>JobSchedulerDelegate</code> class. 
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.2
 */
public class JobSchedulerDelegate {
    
    private String 
        targetEndPoint = null ;
    private int
        timeout = 60000 ;
        
    public JobSchedulerDelegate( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;
    }
    
    public JobSchedulerDelegate( String targetEndPoint, int timeout ) {
      this.targetEndPoint = targetEndPoint ;
      this.timeout = timeout ;
    }
    
    public void scheduleJob(String req) throws JesDelegateException {
        
        JobSchedulerServiceSoapBindingStub 
            binding = null ;
            
        try {
            binding = (JobSchedulerServiceSoapBindingStub)
                          new JobSchedulerServiceLocator().getJobSchedulerService( new URL( targetEndPoint ) );                        
            binding.setTimeout( timeout ) ;    
            binding.scheduleJob(req);
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
  
    } // end of scheduleJob()

} // end of class JobSchedulerDelegate