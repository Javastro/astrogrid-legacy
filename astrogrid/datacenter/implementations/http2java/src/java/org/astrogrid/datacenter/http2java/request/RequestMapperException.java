/*$Id: RequestMapperException.java,v 1.1 2003/11/18 11:48:14 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.request;

import org.astrogrid.datacenter.http2java.LegacyServiceException;

/** Exception subclass that indicates a failure in doing the request.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class RequestMapperException extends LegacyServiceException {

    /**
     * 
     */
    public RequestMapperException() {
        super();
    }

    /**
     * @param message
     */
    public RequestMapperException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public RequestMapperException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public RequestMapperException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: RequestMapperException.java,v $
Revision 1.1  2003/11/18 11:48:14  nw
mavenized http2java

Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/