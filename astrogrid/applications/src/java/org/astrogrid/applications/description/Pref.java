/*
 * $Id: Pref.java,v 1.1 2003/12/05 22:52:16 pah Exp $
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
 */
public class Pref {
   private ParameterDescription parameter;
   private boolean optional;
   private boolean repeatable;

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
