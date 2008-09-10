/*
 * $Id: HttpParameterProcessingException.java,v 1.1 2008/09/10 23:27:18 pah Exp $
 * 
 * Created on 31 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.http.exceptions;

import org.astrogrid.applications.CeaException;

public class HttpParameterProcessingException extends HttpApplicationException {

    public HttpParameterProcessingException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public HttpParameterProcessingException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

}


/*
 * $Log: HttpParameterProcessingException.java,v $
 * Revision 1.1  2008/09/10 23:27:18  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.2  2008/09/03 14:18:45  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/04/01 13:50:06  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 */
