/*
 * $Id: InterfaceDescriptionNotFoundException.java,v 1.1 2008/10/06 12:12:36 pah Exp $
 * 
 * Created on 01-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
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
 * Cannot find the iterface description for an application.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class InterfaceDescriptionNotFoundException extends CeaException {

 
   /**
    * @param message
    */
   public InterfaceDescriptionNotFoundException(String message) {
      super(message);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param message
    * @param cause
    */
   public InterfaceDescriptionNotFoundException(
      String message,
      Throwable cause) {
      super(message, cause);
   }

}
