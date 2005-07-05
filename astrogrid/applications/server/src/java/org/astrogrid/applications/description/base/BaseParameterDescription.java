/*
 * $Id: BaseParameterDescription.java,v 1.4 2005/07/05 08:27:01 clq2 Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description.base;

import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ParameterDescription;
/**
 *Basic implementation of a {@link org.astrogrid.applications.description.ParameterDescription}.
 *<p />
 *Simple data bean - adds setters for each of the getters defined in <tt>ParameterDescription</tt>
 *@author Noel Winstanley
 */
public class BaseParameterDescription implements ParameterDescription {

   static protected org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(BaseParameterDescription.class);
   public String getName(){ return name; }

   public void setName(String name){ this.name = name; }

   public String getDisplayName(){ return displayName; }

   public void setDisplayName(String displayName){ this.displayName = displayName; }

   public String getDisplayDescription(){ return displayDescription; }

   public void setDisplayDescription(String displayDescription){ this.displayDescription = displayDescription; }

   public String getUcd(){ return ucd; }

   public void setUcd(String ucd){ this.ucd= ucd; }

   protected String name;
   protected String displayName = "";
   protected String displayDescription = "";
   protected String ucd = "";
   protected ParameterTypes type;
   protected String acceptEncodings = "";
   protected String subtype = "";
   protected String defaultValue = "";
   protected String units = "";
   public String getAcceptEncodings() {return acceptEncodings;}
   public void setAcceptEncodings(String a) {this.acceptEncodings = a;}
   public String getSubType() {return subtype;}
   public void setSubType(String st) {this.subtype = st;}
   
   public String getUnits() {return units;}
   public void setUnits(String u) {this.units = u;}
   
   public String getDefaultValue() {return defaultValue;}
   public void setDefaultValue(String s) {this.defaultValue = s;}
   
   public ParameterTypes getType() { return type;}
   public void setType(ParameterTypes type) {this.type= type;}
   
  
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Parameter: " + getName() + " /" + getType()  +  (getSubType() != null ? "(" + getSubType() + ")/" : "")                        
            + (getDefaultValue() != "" ? "::Default Value: " + getDefaultValue(): "")
            + (getDisplayName() != "" ? " ::Display Name: " + getDisplayName():"")
            + (getDisplayDescription() != "" ? "::Display Description: " + getDisplayDescription():"")
            + (getUcd() != "" ? "::UCD: " + getUcd():"")
            + (getUnits() != "" ? "::Units: " + getUnits():"")
            + (getAcceptEncodings() != "" ? "::Accept Encodings: " + getAcceptEncodings():"");
            
    }

}
