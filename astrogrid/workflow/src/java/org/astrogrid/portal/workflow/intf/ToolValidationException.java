/*$Id: ToolValidationException.java,v 1.2 2004/03/11 13:53:36 nw Exp $
 * Created on 11-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

/** Thrown when a tool instance does not match the definition given in an application description.
 * @see ApplicationDescription#validate
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Mar-2004
 *
 */
public class ToolValidationException extends Exception {

    /** Construct a new InvalidToolException
     * @param message
     */
    public ToolValidationException(String message) {
        super(message);
    }
    /** Construct a new InvalidToolException
     * @param cause
     */
    public ToolValidationException(Throwable cause) {
        super(cause);
    }
    /** Construct a new InvalidToolException
     * @param message
     * @param cause
     */
    public ToolValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}


/* 
$Log: ToolValidationException.java,v $
Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:36:46  nw
tidied up interfaces, documented
 
*/