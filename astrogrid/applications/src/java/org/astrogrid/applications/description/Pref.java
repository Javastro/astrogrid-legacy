/*
 * $Id: Pref.java,v 1.2 2003/12/12 21:30:46 pah Exp $
 * 
 * Created on 05-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

/**
 * A parameter reference in an interface.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO need to actually use this object in the interface descriptions - at the moment the interface is simply storing parameter names, which means that the extra metainformation that is represented by this class is lost from the parameter description
 */
public class Pref {
   private ParameterDescription parameter;
   private boolean optional;
   private boolean repeatable; //REFACTORME would be better to have a min/max occurances

   /**
    * @return
    */
   public boolean isOptional() {
      return optional;
   }

   /**
    * @return
    */
   public boolean isRepeatable() {
      return repeatable;
   }

   /**
    * @param b
    */
   public void setOptional(boolean b) {
      optional = b;
   }

   /**
    * @param b
    */
   public void setRepeatable(boolean b) {
      repeatable = b;
   }

   /**
    * @return
    */
   public ParameterDescription getParameter() {
      return parameter;
   }

   /**
    * @param description
    */
   public void setParameter(ParameterDescription description) {
      parameter = description;
   }

}
