/*
 * $Id: PersistenceException.java,v 1.1 2004/04/19 17:34:08 pah Exp $
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

package org.astrogrid.applications.manager;

import org.astrogrid.applications.CeaException;

/**
 * Indicates a problem with the  persistence engine.
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class PersistenceException extends CeaException {

   /**
    * @param message
    */
   public PersistenceException(String message) {
      super(message);
   }

   /**
    * @param message
    * @param cause
    */
   public PersistenceException(String message, Throwable cause) {
      super(message, cause);
   }

}
