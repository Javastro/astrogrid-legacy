package org.astrogrid.jes.i18n;


public class JesException extends Exception {
	
	private Message
	    message ;
	    

	public JesException( Message message ) {
		this.message = message ;
	}


	public JesException( Message message, Exception exception ) {
		super( exception ) ;
		this.message = message ;
	}

    public Message getAstroGridMessage() {
    	return message ;
    }
    
}
