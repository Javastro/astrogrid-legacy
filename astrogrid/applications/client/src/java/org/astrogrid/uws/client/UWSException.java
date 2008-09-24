/*
 * $Id: UWSException.java,v 1.1 2008/09/24 13:47:18 pah Exp $
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

public class UWSException extends Exception {

     public UWSException(String message) {
        super(message);
      
    }

    public UWSException(Throwable cause) {
        super(cause);
    }

    public UWSException(String message, Throwable cause) {
        super(message, cause);
    }

}


/*
 * $Log: UWSException.java,v $
 * Revision 1.1  2008/09/24 13:47:18  pah
 * added generic UWS client code
 *
 */
