/*
 * $Id: ApplicationDescriptionNotFoundException.java,v 1.1 2003/11/26 22:07:24 pah Exp $
 * 
 * Created on 26-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
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
public class ApplicationDescriptionNotFoundException extends Exception {
   private String name;
   
   public ApplicationDescriptionNotFoundException(String name)
   {
      this.name = name;
   }

}
