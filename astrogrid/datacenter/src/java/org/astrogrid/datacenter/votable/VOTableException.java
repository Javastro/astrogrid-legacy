/*
 * @(#)VOTableException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.votable;

import org.astrogrid.datacenter.DatacenterException;
import org.astrogrid.i18n.AstroGridMessage;
/** exception type for the VOTable component */ 
public class VOTableException extends DatacenterException {
	 

    public VOTableException( AstroGridMessage message ) {
    	super( message ) ;
    }

    public VOTableException( AstroGridMessage message, Exception exception ) {
    	super( message, exception ) ;
    }
    
}
