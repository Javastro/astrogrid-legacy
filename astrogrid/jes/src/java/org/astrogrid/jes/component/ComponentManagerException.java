/*$Id: ComponentManagerException.java,v 1.2 2004/03/07 21:04:38 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component;

/** Exception thrown when something goes wrong with a component manager - usually during the instantiaion process.
 * unchecked exception, becuase there's no point trying to catch it - if something goes wrong enough to throw this, thats it. log & die.
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class ComponentManagerException extends RuntimeException {
    /** Construct a new ComponentManagerException
     * 
     */
    public ComponentManagerException() {
        super();
    }
    /** Construct a new ComponentManagerException
     * @param message
     */
    public ComponentManagerException(String message) {
        super(message);
    }
    /** Construct a new ComponentManagerException
     * @param cause
     */
    public ComponentManagerException(Throwable cause) {
        super(cause);
    }
    /** Construct a new ComponentManagerException
     * @param message
     * @param cause
     */
    public ComponentManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}


/* 
$Log: ComponentManagerException.java,v $
Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:47  nw
reimplemented component-manager framework to use picocontainer
 
*/