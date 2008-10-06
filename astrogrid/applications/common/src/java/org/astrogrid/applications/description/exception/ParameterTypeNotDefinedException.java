/*
 * $Id: ParameterTypeNotDefinedException.java,v 1.1 2008/10/06 12:12:36 pah Exp $
 * 
 * Created on 28-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.exception;

import org.astrogrid.applications.CeaException;

/**
 * Thrown if an unknown parameter type is encountered when parsing a parameter definition file.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterTypeNotDefinedException extends CeaException {


   /**
    * @param type
    */
   public ParameterTypeNotDefinedException(String type) {
      
     super(type);
   }
   
  

}
