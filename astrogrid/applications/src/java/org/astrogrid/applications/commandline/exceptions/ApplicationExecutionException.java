/*
 * $Id: ApplicationExecutionException.java,v 1.1 2003/12/07 01:09:48 pah Exp $
 * 
 * Created on 06-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.exceptions;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationExecutionException extends Exception {

   /**
    * 
    */
   public ApplicationExecutionException() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param message
    */
   public ApplicationExecutionException(String message) {
      super(message);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param cause
    */
   public ApplicationExecutionException(Throwable cause) {
      super(cause);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param message
    * @param cause
    */
   public ApplicationExecutionException(String message, Throwable cause) {
      super(message, cause);
      // TODO Auto-generated constructor stub
   }

}
