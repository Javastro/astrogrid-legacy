/*
 * $Id: ParameterTypeNotDefinedException.java,v 1.2 2003/12/03 11:48:48 pah Exp $
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

/**
 * Thrown if an unknown parameter type is encountered when parsing a parameter definition file.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ParameterTypeNotDefinedException extends Exception {

   private String typename;

   /**
    * @param type
    */
   public ParameterTypeNotDefinedException(String type) {
      
     this.typename = type;
   }
   
   

   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return  super.toString() + " unknown type="+typename;
   }
   

}
