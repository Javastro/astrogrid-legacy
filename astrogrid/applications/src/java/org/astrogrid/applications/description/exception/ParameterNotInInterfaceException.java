/*
 * $Id: ParameterNotInInterfaceException.java,v 1.2 2003/12/03 11:48:48 pah Exp $
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

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterNotInInterfaceException extends Exception {
   


   /**
    * @param message
    */
   public ParameterNotInInterfaceException(String message) {
      super(message);
     }

}
