/*
 * @(#)JesDelegateException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.delegate ;

public class JesDelegateException extends java.lang.Exception {
	
    public JesDelegateException( String message ) { 
        super( message ) ;
    }

    public JesDelegateException( String message, Throwable exception ) {
        super( message, exception ) ;
    }
    
	public JesDelegateException( Throwable exception ) { 
		super( exception ) ; 
	}
    
}
