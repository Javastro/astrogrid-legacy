/*
 * $Id: ParameterDescription.java,v 1.2 2003/12/08 17:06:35 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import java.util.List;

import org.astrogrid.applications.CommandLineParameter;
import org.astrogrid.applications.Parameter;
/**
 * The basic parameter definition description. 
 */
public abstract class ParameterDescription {
   public String getName(){ return name; }

   public void setName(String name){ this.name = name; }

   public String getDisplayName(){ return displayName; }

   public void setDisplayName(String displayName){ this.displayName = displayName; }

   public String getDisplayDescription(){ return displayDescription; }

   public void setDisplayDescription(String displayDescription){ this.displayDescription = displayDescription; }

   public String getUcd(){ return ucd; }

   public void setUcd(String ucd){ this.ucd= ucd; }

   private String name;
   private String displayName;
   private String displayDescription;
   private String ucd;
   
   /**
    * Creates a parameter value object. This should be overriden in subclasses where specialzed behaviour is required.
    * @return
    */
   public Parameter createValueObject()
   {
      Parameter param = new CommandLineParameter(this);
      return param;
   }


}
