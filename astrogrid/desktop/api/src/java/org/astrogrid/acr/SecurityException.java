/*$Id: SecurityException.java,v 1.4 2006/04/18 23:25:45 nw Exp $
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

/** Indicates that the user is not authorized to perform this operation.
 * 
 * e.g. user is attempting a valid operation, but lackes the permissions to do it (e.g. read a myspace file not owned by them)
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2005
 *
 */
public class SecurityException extends ACRException {

    static final long serialVersionUID = -6623514730873587267L;

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
Revision 1.4  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.3.6.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.3  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/