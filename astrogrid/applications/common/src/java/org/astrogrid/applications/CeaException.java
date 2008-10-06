/*
 * $Id: CeaException.java,v 1.1 2008/10/06 12:12:37 pah Exp $
 * 
 * Created on 17-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
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
 * The base class for exceptions in CEA.
 * @author Paul Harrison (pah@jb.man.ac.uk) 17-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class CeaException extends Exception {

   /**
    * @param message
    */
   public CeaException(String message) {
      super(message);
   }

   /**
    * @param message
    * @param cause
    */
   public CeaException(String message, Throwable cause) {
      super(message, cause);
   }

}
