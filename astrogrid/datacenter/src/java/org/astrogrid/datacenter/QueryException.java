package org.astrogrid.datacenter;

import org.astrogrid.datacenter.i18n.* ;

public class QueryException extends DatacenterException {

    public QueryException( Message message ) {
    	super( message ) ;
    }

    public QueryException(  Message message, Exception exception ) {
		super( message, exception ) ;
    }
    
}
