package org.astrogrid.datacenter.i18n;


public class DatacenterException extends Exception {
	
	private Message
	    message ;
	    

	public DatacenterException( Message message ) {
		this.message = message ;
	}


	public DatacenterException( Message message, Exception exception ) {
		super( exception ) ;
		this.message = message ;
	}

    public Message getAstroGridMessage() {
    	return message ;
    }
    
}
