/*$Id: SecurityException.java,v 1.1 2005/08/11 10:15:00 nw Exp $
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

/** exception thrown when user is not authenticated or no authorised to perform an operation
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2005
 *
 */
public class SecurityException extends ACRException {

    /** Construct a new SecurityException
     * 
     */
    public SecurityException() {
        super();
    }

    /** Construct a new SecurityException
     * @param message
     */
    public SecurityException(String message) {
        super(message);
    }

    /** Construct a new SecurityException
     * @param cause
     */
    public SecurityException(Throwable cause) {
        super(cause);
    }

    /** Construct a new SecurityException
     * @param message
     * @param cause
     */
    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: SecurityException.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/