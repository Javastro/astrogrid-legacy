/*
 * $Id: ParameterDescriptionNotFoundException.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 28-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.exception;

import org.astrogrid.applications.CeaException;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterDescriptionNotFoundException extends CeaException {

   /**
    * @param message
    * @param cause
    */
   public ParameterDescriptionNotFoundException(String message) {
      super(message);
   }

 
}
