/*
 * $Id: Parameter.java,v 1.3 2003/12/08 17:06:35 pah Exp $
 *
 * Created on 13 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.description.ParameterDescription;
/**
 * @stereotype entity 
 */
abstract public class Parameter {
   protected String name;
   protected String rawValue;
   protected ParameterDescription parameterDescription;
   
   public Parameter(ParameterDescription parameterDescription)
   {
      this.parameterDescription = parameterDescription;
   }
   
   
   /**
    * @return
    */
   public String getRawValue() {
      return rawValue;
   }

   /**
    * @param string
    */
   public void setRawValue(String string) {
      rawValue = string;
   }


   /* (non-Javadoc)
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return parameterDescription.getName() +" rawval="+ rawValue;
   }
   
   

}
