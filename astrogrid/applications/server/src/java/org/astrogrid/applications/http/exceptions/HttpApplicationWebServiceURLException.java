/* $Id: HttpApplicationWebServiceURLException.java,v 1.1 2008/09/10 23:27:18 pah Exp $
 * Created on Jul 27, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
///CLOVER:OFF 
package org.astrogrid.applications.http.exceptions;

import java.io.IOException;


/**
 * Caused by an incorrect or malformed URL
 * @author jdt
 */
public class HttpApplicationWebServiceURLException extends HttpApplicationWebServiceException {


    /**
     * Constructor
     *
     * @param arg0
     */
    public HttpApplicationWebServiceURLException(String arg0) {
        super(arg0);
    }
    /**
     * Constructor
     *
     * @param string
     * @param e
     */
    public HttpApplicationWebServiceURLException(String string, IOException e) {
        super(string, e);
    }
}

/* 
 * $Log: HttpApplicationWebServiceURLException.java,v $
 * Revision 1.1  2008/09/10 23:27:18  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.5  2008/09/03 14:18:45  pah
 * result of merge of pah_cea_1611 branch
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
 * Revision 1.1.2.1  2004/07/27 17:12:44  jdt
 * refactored exceptions and finished tests for HttpServiceClient
 *
 */