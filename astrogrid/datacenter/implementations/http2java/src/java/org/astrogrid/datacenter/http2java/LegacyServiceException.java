/*$Id: LegacyServiceException.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java;

/** Root exception class for this system.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class LegacyServiceException extends Exception {

    /**
     * 
     */
    public LegacyServiceException() {
        super();
    }

    /**
     * @param message
     */
    public LegacyServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public LegacyServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public LegacyServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: LegacyServiceException.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/