/*
 * $Id: ParameterMySpaceReferenceNotFound.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 19-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import org.astrogrid.applications.CeaException;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ParameterMySpaceReferenceNotFound extends CeaException {

   /**
    * @param message
    */
   public ParameterMySpaceReferenceNotFound(String message) {
      super(message);
      // TODO Auto-generated constructor stub
   }

   /**
    * @param message
    * @param cause
    */
   public ParameterMySpaceReferenceNotFound(String message, Throwable cause) {
      super(message, cause);
      // TODO Auto-generated constructor stub
   }

}
