/*
 * $Id: ApplicationExecutionException.java,v 1.4 2004/04/28 16:10:11 pah Exp $
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

package org.astrogrid.applications;

/**
 * General problem execution an application.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationExecutionException extends CeaException {

 
   /**
    * @param message
    */
   public ApplicationExecutionException(String message) {
      super(message);
   }

 
   /**
    * @param message
    * @param cause
    */
   public ApplicationExecutionException(String message, Throwable cause) {
      super(message, cause);
   }

}
