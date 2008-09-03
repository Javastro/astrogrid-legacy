/* $Id: HttpApplicationException.java,v 1.5 2008/09/03 14:18:45 pah Exp $
 * Created on Jul 27, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
///CLOVER:OFF 
package org.astrogrid.applications.http.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.astrogrid.applications.CeaException;

/**
 * A superclass for exceptions of the httpApplication server, just in case you want to just catch HttpApplicationExceptions
 * @author jdt
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 31 Mar 2008 - turned into class...
 */
public class HttpApplicationException extends CeaException {

    /**
     * @param message
     * @param cause
     */
    public HttpApplicationException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * @param message
     */
    public HttpApplicationException(String message) {
	super(message);
   }
 

}

/* 
 * $Log: HttpApplicationException.java,v $
 * Revision 1.5  2008/09/03 14:18:45  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.4.212.2  2008/08/02 13:32:32  pah
 * safety checkin - on vacation
 *
 * Revision 1.4.212.1  2008/04/01 13:50:06  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.4  2004/09/01 15:42:26  jdt
 * Merged in Case 3
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.3  2004/07/27 17:12:44  jdt
 * refactored exceptions and finished tests for HttpServiceClient
 *
 */