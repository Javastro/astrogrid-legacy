package org.astrogrid.datacenter.job;

import org.astrogrid.datacenter.i18n.*;

public class JobException extends DatacenterException {

    public JobException( Message message ) {
		super( message ) ;
    }

    public JobException( Message message, Exception exception ) {
		super( message, exception ) ;
    }
    
}
