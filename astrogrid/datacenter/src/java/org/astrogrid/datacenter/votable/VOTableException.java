package org.astrogrid.datacenter.votable;

import org.astrogrid.datacenter.i18n.*;

public class VOTableException extends DatacenterException {
	 

    public VOTableException( Message message ) {
    	super( message ) ;
    }

    public VOTableException( Message message, Exception exception ) {
    	super( message, exception ) ;
    }
    
}
