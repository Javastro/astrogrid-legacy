package org.astrogrid.datacenter;

public class VOTableException extends java.lang.Exception {
	 
    public VOTableException() {
    	super() ;
    }

    public VOTableException( String message ) {
    	super( message ) ;
    }

    public VOTableException( Exception exception ) {
    	super( exception ) ;
    }

    public VOTableException( String message, Exception exception ) {
    	super( message, exception ) ;
    }
    
}
