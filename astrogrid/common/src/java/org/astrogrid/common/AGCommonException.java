/*
 * $Id: AGCommonException.java,v 1.2 2008/09/17 08:16:06 pah Exp $
 * 
 * Created on 7 Jan 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.common;

public class AGCommonException extends Exception {

    /**
     * @param message
     * @param cause
     */
    public AGCommonException(String message, Throwable cause) {
	super(message, cause);

   }

    /**
     * @param message
     */
    public AGCommonException(String message) {
	super(message);

   }

}


/*
 * $Log: AGCommonException.java,v $
 * Revision 1.2  2008/09/17 08:16:06  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:14  pah
 * safety checkin before interop
 *
 */
