package org.astrogrid.jes.delegate.jobMonitor;

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
public class JobMonitorDelegate {
    
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
    
    public void monitorJob(String req) throws JesDelegateException {
        
        JobMonitorServiceSoapBindingStub 
            binding = null ;
            
        try {
            binding = (JobMonitorServiceSoapBindingStub)
                          new JobMonitorServiceLocator().getJobMonitorService( new URL( targetEndPoint ) );                        
            binding.setTimeout( timeout ) ;    
            binding.monitorJob(req);
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
  
    } // end of monitorJob()

} // end of class JobMonitorDelegate