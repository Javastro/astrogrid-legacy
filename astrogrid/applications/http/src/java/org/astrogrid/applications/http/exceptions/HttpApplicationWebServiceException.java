/* $Id: HttpApplicationWebServiceException.java,v 1.6 2008/09/03 14:18:45 pah Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
///CLOVER:OFF 
package org.astrogrid.applications.http.exceptions;


/**
 * Exceptions caused by problems connecting to the webservice
 * @author jdt
 */
public class HttpApplicationWebServiceException extends HttpApplicationException {

     /**
     * Constructor
     *
     * @param arg0
     */
    public HttpApplicationWebServiceException(String arg0) {
        super(arg0);
    }
    
    /**
     * Constructor
     * @param string fault description
     * @param e original exception
     */
    public HttpApplicationWebServiceException(String string, Throwable e) {
       super(string, e);
    }
}

/* 
 * $Log: HttpApplicationWebServiceException.java,v $
 * Revision 1.6  2008/09/03 14:18:45  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.5.6.1  2008/04/01 13:50:06  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.5  2007/08/29 09:51:27  gtr
 * It can now be constructed from a generic Exception.
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
 * Revision 1.1.2.2  2004/07/26 16:20:30  jdt
 * Added some tests.
 *
 * Revision 1.1.2.1  2004/07/24 17:16:16  jdt
 * Borrowed from javaclass application....stealing is always quicker.
 *
 */
