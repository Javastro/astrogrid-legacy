package org.astrogrid.datacenter;

import org.astrogrid.datacenter.i18n.*;

public class AllocationException extends DatacenterException{
	

    public AllocationException( Message message) {
    	super( message ) ;
    }

    public AllocationException( Message message, Exception exception ) {
		super( message, exception ) ;
    }
    
}
