/*
 * $Id: ExecutionIDNotFoundException.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

package org.astrogrid.applications.manager.persist;


/**
 * Cannot find the executionID in the persistence engine.
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Apr-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ExecutionIDNotFoundException extends PersistenceException {

   /**
    * @param message
    */
   public ExecutionIDNotFoundException(String message) {
      super(message);
   }

   /**
    * @param message
    * @param cause
    */
   public ExecutionIDNotFoundException(String message, Throwable cause) {
      super(message, cause);
   }

}
