/*
 * $Id: ParameterWriteBackException.java,v 1.1 2004/04/19 17:34:08 pah Exp $
 * 
 * Created on 19-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

/**
 * Indicates that there was a problem when trying to return the results of an application run.
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ParameterWriteBackException extends CeaException {

   /**
    * @param message
    */
   public ParameterWriteBackException(String message) {
      super(message);
   }

   /**
    * @param message
    * @param cause
    */
   public ParameterWriteBackException(String message, Throwable cause) {
      super(message, cause);
   }

}
