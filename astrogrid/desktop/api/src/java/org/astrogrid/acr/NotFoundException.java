/*$Id: NotFoundException.java,v 1.3 2006/02/02 14:19:47 nw Exp $
 * Created on 28-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr;

/** Indicates a required resource or service was not found.
 * 
 * e.g. a missing registry resource, or a non-existent muyspace file..
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Jul-2005
 *
 */
public class NotFoundException extends ACRException {

    /** Construct a new NotFoundException
     * 
     */
    public NotFoundException() {
        super();
    }

    /** Construct a new NotFoundException
     * @param message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /** Construct a new NotFoundException
     * @param cause
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

    /** Construct a new NotFoundException
     * @param message
     * @param cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}


/* 
$Log: NotFoundException.java,v $
Revision 1.3  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.2  2005/08/12 08:45:15  nw
souped up the javadocs

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/