/*$Id: InvalidArgumentException.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 01-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Aug-2005
 *
 */
public class InvalidArgumentException extends ACRException {

    /** Construct a new InvalidArgumentException
     * 
     */
    public InvalidArgumentException() {
        super();
    }

    /** Construct a new InvalidArgumentException
     * @param message
     */
    public InvalidArgumentException(String message) {
        super(message);
    }

    /** Construct a new InvalidArgumentException
     * @param cause
     */
    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }

    /** Construct a new InvalidArgumentException
     * @param message
     * @param cause
     */
    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: InvalidArgumentException.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/