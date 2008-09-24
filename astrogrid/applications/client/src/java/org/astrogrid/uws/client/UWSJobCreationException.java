/*
 * $Id: UWSJobCreationException.java,v 1.1 2008/09/24 13:47:18 pah Exp $
 * 
 * Created on 22 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.uws.client;

public class UWSJobCreationException extends UWSException {

    public UWSJobCreationException(String message, Throwable cause) {
        super(message, cause);
   
    }

    public UWSJobCreationException(String message) {
        super(message);
      
    }

    public UWSJobCreationException(Throwable cause) {
        super(cause);
      
    }

}


/*
 * $Log: UWSJobCreationException.java,v $
 * Revision 1.1  2008/09/24 13:47:18  pah
 * added generic UWS client code
 *
 */
