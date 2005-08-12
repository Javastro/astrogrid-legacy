/*$Id: ServiceException.java,v 1.2 2005/08/12 08:45:15 nw Exp $
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

/** A low-level failure of service or network
 * 
 * <p>
 * e.g. a socket connection times out because a server is down.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2005
 *
 */
public class ServiceException extends ACRException {

    /** Construct a new ServiceException
     * 
     */
    public ServiceException() {
        super();
    }

    /** Construct a new ServiceException
     * @param message
     */
    public ServiceException(String message) {
        super(message);
    }

    /** Construct a new ServiceException
     * @param cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /** Construct a new ServiceException
     * @param message
     * @param cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: ServiceException.java,v $
Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/