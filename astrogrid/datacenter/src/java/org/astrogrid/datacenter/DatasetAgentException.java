

package org.astrogrid.datacenter;

import org.astrogrid.datacenter.i18n.*;

public class DatasetAgentException extends DatacenterException {

    public DatasetAgentException( Message message ) {
    	super( message ) ;
    }

    public DatasetAgentException( Message message, Exception exception ) {
    	super( message, exception ) ;
    }
     
}
