/*$Id: ResultBuilderException.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

/** Exception subtype that represents a failure in  building the result of a legacy web method call.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class ResultBuilderException extends LegacyServiceException {

    /**
     * 
     */
    public ResultBuilderException() {
        super();
    }

    /**
     * @param message
     */
    public ResultBuilderException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ResultBuilderException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ResultBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: ResultBuilderException.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/