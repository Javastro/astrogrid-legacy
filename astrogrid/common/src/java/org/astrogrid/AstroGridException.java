/*
 * @(#)AstroGridException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid ;

import org.astrogrid.i18n.AstroGridMessage;


public class AstroGridException extends Exception {
	
	private AstroGridMessage
	    message = null ;
	    
	public AstroGridException( AstroGridMessage message ) {
		this.message = message ;
	}


	public AstroGridException( AstroGridMessage message, Throwable throwable ) {
		super( throwable ) ;
		this.message = message ;
	}
    
    
    public AstroGridException( Throwable throwable ) {
        super( throwable ) ;
    }


    public AstroGridMessage getAstroGridMessage() {
    	return message ;
    }
    
}
