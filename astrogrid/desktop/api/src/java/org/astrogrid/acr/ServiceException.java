/*$Id: ServiceException.java,v 1.8 2008/09/25 16:02:09 nw Exp $
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

/** A low-level failure of service or network.
 * 
 * For example, a socket connection times out because a server is down.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 29-Jul-2005
 *
 */
public class ServiceException extends ACRException {

    static final long serialVersionUID = -8406921231044186670L;

    /** Construct a new ServiceException
     * @exclude
     */
    public ServiceException() {
        super();
    }

    /** Construct a new ServiceException
     * @param message
     * @exclude
     */
    public ServiceException(final String message) {
        super(message);
    }

    /** Construct a new ServiceException
     * @param cause
     * @exclude
     */
    public ServiceException(final Throwable cause) {
        super(cause);
    }

    /** Construct a new ServiceException
     * @param message
     * @param cause
     * @exclude
     */
    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: ServiceException.java,v $
Revision 1.8  2008/09/25 16:02:09  nw
documentation overhaul

Revision 1.7  2008/08/21 11:34:18  nw
doc tweaks

Revision 1.6  2007/01/24 14:04:45  nw
updated my email address

Revision 1.5  2006/10/30 12:12:36  nw
documentation improvements.

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