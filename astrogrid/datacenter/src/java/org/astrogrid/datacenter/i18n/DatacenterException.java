/*
 * @(#)DatacenterException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
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
