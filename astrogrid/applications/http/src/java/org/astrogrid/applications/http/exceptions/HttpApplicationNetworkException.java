/* $Id: HttpApplicationNetworkException.java,v 1.2 2004/07/27 17:49:57 jdt Exp $
 * Created on Jul 27, 2004
 * Copyright (C) 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 */
package org.astrogrid.applications.http.exceptions;

import java.io.IOException;

/**
 * Caused by problems on the network such as timeouts
 * @author jdt
 */
public class HttpApplicationNetworkException extends HttpApplicationWebServiceException {


    /**
     * Constructor
     *
     * 
     */
    public HttpApplicationNetworkException() {
        super();
    }
    /**
     * Constructor
     *
     * @param arg0
     */
    public HttpApplicationNetworkException(String arg0) {
        super(arg0);
    }

    /**
     * Constructor
     *
     * @param string
     * @param e
     */
    public HttpApplicationNetworkException(String string, IOException e) {
        super(string, e);
        // @TODO Auto-generated constructor stub
    }
}

/* 
 * $Log: HttpApplicationNetworkException.java,v $
 * Revision 1.2  2004/07/27 17:49:57  jdt
 * merges from case3 branch
 *
 * Revision 1.1.4.1  2004/07/27 17:20:25  jdt
 * merged from applications_JDT_case3
 *
 * Revision 1.1.2.1  2004/07/27 17:12:44  jdt
 * refactored exceptions and finished tests for HttpServiceClient
 *
 */