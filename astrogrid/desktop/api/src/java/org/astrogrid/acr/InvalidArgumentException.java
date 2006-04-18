/*$Id: InvalidArgumentException.java,v 1.4 2006/04/18 23:25:45 nw Exp $
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

/** Indicates that n incorrect / malformed value was passed as an input parameter.
 * 
 * e.g. an unrecognized form of URI was passed in.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Aug-2005
 *
 */
public class InvalidArgumentException extends ACRException {

    static final long serialVersionUID = 3710373580318214188L;

    /** Construct a new InvalidArgumentException.
     * 
     */
    public InvalidArgumentException() {
        super();
    }

    /** Construct a new InvalidArgumentException.
     * @param message
     */
    public InvalidArgumentException(String message) {
        super(message);
    }

    /** Construct a new InvalidArgumentException.
     * @param cause
     */
    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }

    /** Construct a new InvalidArgumentException.
     * @param message
     * @param cause
     */
    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: InvalidArgumentException.java,v $
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