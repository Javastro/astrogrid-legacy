/*$Id: ResponseConvertorException.java,v 1.1 2003/11/18 11:48:14 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java.response;

import org.astrogrid.datacenter.http2java.LegacyServiceException;

/** Exception subtype representing a failure in the response conversion.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class ResponseConvertorException extends LegacyServiceException {

    /**
     * 
     */
    public ResponseConvertorException() {
        super();
    }

    /**
     * @param message
     */
    public ResponseConvertorException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ResponseConvertorException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ResponseConvertorException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: ResponseConvertorException.java,v $
Revision 1.1  2003/11/18 11:48:14  nw
mavenized http2java

Revision 1.1  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/