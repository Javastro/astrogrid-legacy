/*
 * $Id: MandatoryParameterNotPassedException.java,v 1.1 2004/08/28 07:17:34 pah Exp $
 * 
 * Created on 20-Aug-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

/**
 * Occurs when a mandatory parameter is not passed to an application for a particular interface.
 * @author Paul Harrison (pah@jb.man.ac.uk) 20-Aug-2004
 * @version $Name:  $
 * @since iteration6
 */
public class MandatoryParameterNotPassedException extends CeaException {

    /**
     * @param message
     */
    public MandatoryParameterNotPassedException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public MandatoryParameterNotPassedException(String message, Throwable cause) {
        super(message, cause);
    }

}
