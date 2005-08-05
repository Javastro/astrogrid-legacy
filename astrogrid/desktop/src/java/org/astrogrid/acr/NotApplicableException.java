/*$Id: NotApplicableException.java,v 1.1 2005/08/05 11:46:55 nw Exp $
 * Created on 29-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

/** thrown when an operation is attempted with invalid parameters / invalid state.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2005
 *
 */
public class NotApplicableException extends ACRException {

    /** Construct a new NotApplicableException
     * 
     */
    public NotApplicableException() {
        super();
    }

    /** Construct a new NotApplicableException
     * @param message
     */
    public NotApplicableException(String message) {
        super(message);
    }

    /** Construct a new NotApplicableException
     * @param cause
     */
    public NotApplicableException(Throwable cause) {
        super(cause);
    }

    /** Construct a new NotApplicableException
     * @param message
     * @param cause
     */
    public NotApplicableException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: NotApplicableException.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/