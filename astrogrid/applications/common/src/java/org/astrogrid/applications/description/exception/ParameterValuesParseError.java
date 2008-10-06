/*
 * $Id: ParameterValuesParseError.java,v 1.1 2008/10/06 12:12:36 pah Exp $
 * 
 * Created on 01-Apr-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.exception;

import org.astrogrid.applications.CeaException;

/**
 * Error occured when parsing the parameter values that are part of the tool invocation.
 * @author Paul Harrison (pah@jb.man.ac.uk) 01-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ParameterValuesParseError extends CeaException {

   /**
    * @param message
    */
   public ParameterValuesParseError(String message) {
      super(message);
   }

   /**
    * @param message
    * @param cause
    */
   public ParameterValuesParseError(String message, Throwable cause) {
      super(message, cause);
   }

}
