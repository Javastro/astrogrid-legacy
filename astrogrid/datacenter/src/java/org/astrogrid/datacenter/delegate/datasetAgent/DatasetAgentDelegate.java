/*
 * $Id: DatasetAgentDelegate.java,v 1.2 2003/08/06 14:06:29 mch Exp $
 */

package org.astrogrid.datacenter.delegate.datasetAgent;

import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException ;
import org.astrogrid.datacenter.delegate.DatacenterDelegateException ;

/**
 * The <code>DatasetAgentDelegate</code> class.
 *
 * @author  Jeff Lusted
 * @version 1.0 02-Aug-2003
 * @since   AstroGrid 1.2
 */
public class DatasetAgentDelegate {
    
    private String
        targetEndPoint = null ;
    private int
        timeout = 60000 ;
        
    public DatasetAgentDelegate( String targetEndPoint ) {
      this.targetEndPoint = targetEndPoint;
    }
    
    public DatasetAgentDelegate( String targetEndPoint, int timeout ) {
      this.targetEndPoint = targetEndPoint ;
      this.timeout = timeout ;
    }
    
    public void runQuery(String req) throws DatacenterDelegateException {
        
        DatasetAgentSoapBindingStub
            binding = null ;
            
        try {
            binding = (DatasetAgentSoapBindingStub)
                          new DatasetAgentServiceLocator().getDatasetAgent( new URL( targetEndPoint ) );
            binding.setTimeout( timeout ) ;
            binding.runQuery(req);
        }
        catch( MalformedURLException mex ) {
            throw new DatacenterDelegateException( mex ) ;
        }
        catch( RemoteException rex) {
            throw new DatacenterDelegateException( rex ) ;
        }
        catch( javax.xml.rpc.ServiceException sex ) {
            throw new DatacenterDelegateException( sex ) ;
        }
              
        return ;
  
    } // end of runQuery()

} // end of class DatasetAgentDelegate

/*
$Log: DatasetAgentDelegate.java,v $
Revision 1.2  2003/08/06 14:06:29  mch
Added CVS Log & ID for change history

 */
