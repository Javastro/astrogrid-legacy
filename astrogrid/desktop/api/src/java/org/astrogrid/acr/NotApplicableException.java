/*$Id: NotApplicableException.java,v 1.8 2008/09/25 16:02:09 nw Exp $
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

/**An operation was attempted with invalid parameters or state.
 * 
 * 
 * For example, attempting to list the children of a file, instead of a directory.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 29-Jul-2005
 *
 */
public class NotApplicableException extends ACRException {

    static final long serialVersionUID = 6698250597517398250L;

    /** Construct a new NotApplicableException
     * @exclude
     */
    public NotApplicableException() {
        super();
    }

    /** Construct a new NotApplicableException
     * @exclude
     * @param message
     */
    public NotApplicableException(final String message) {
        super(message);
    }

    /** Construct a new NotApplicableException
     * @exclude
     * @param cause
     */
    public NotApplicableException(final Throwable cause) {
        super(cause);
    }

    /** Construct a new NotApplicableException
     * @exclude
     * @param message
     * @param cause
     */
    public NotApplicableException(final String message, final Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: NotApplicableException.java,v $
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