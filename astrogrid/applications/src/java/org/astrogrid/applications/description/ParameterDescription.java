/*
 * $Id: ParameterDescription.java,v 1.7 2004/03/23 12:51:26 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.CommandLineParameter;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.commandline.CmdLineApplication;
/**
 * The basic parameter definition description. 
 */
public abstract class ParameterDescription {
   static protected org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(ParameterDescription.class);
   public String getName(){ return name; }

   public void setName(String name){ this.name = name; }

   public String getDisplayName(){ return displayName; }

   public void setDisplayName(String displayName){ this.displayName = displayName; }

   public String getDisplayDescription(){ return displayDescription; }

   public void setDisplayDescription(String displayDescription){ this.displayDescription = displayDescription; }

   public String getUcd(){ return ucd; }

   public void setUcd(String ucd){ this.ucd= ucd; }

   protected String name;
   protected String displayName;
   protected String displayDescription;
   protected String ucd;
   
   /**
    * The string that makes up the command switch. If this is not specified then it is assumed that the switch is the same as the name
    */
   private String commandSwitch=null;
   /**
    * The switchtype can be "normal" i.e. it is a -switch form or "keyword" where is is of the form switch=par
    */
   private String switchType = "normal";
   /**
    * The commandPosition indicates where on the command line the parameter is to be placed - The first parameter position is 1. If this value is specified then it means that no switch will be output, but the parameter value will be placed directly on the command line at that position.
    */
   private int commandPosition = -1;
   
   /**
    * Creates a parameter value object. This should be overriden in subclasses where specialzed behaviour is required.
    * @return
    */
   public Parameter createValueObject(AbstractApplication app)
   {
      Parameter param = new CommandLineParameter(app, this);
      return param;
   }

   /**
    * @param parameter The actual parameter for this instance...
    * @return a string representing how this parameter should be represented on the command line.
    */
   public List process(Parameter parameter) throws CeaException {
      return addCmdlineAdornment(parameter.getRawValue());
   }


   /**
    * Adds any necessary switches to the commandline parameter. This is controlled by the @link #commandPosition, @link #commandSwitch and @link #switchType fields. 
    * If the commandPosition is anything other than -1 then no adornment is added. If a switch string is to be added then the style is controlled by switchType and the
    * string for the switch is given by commandSwitch, or if that is null the parameter name is used. 
    * @param val
    * @return
    */
   protected List addCmdlineAdornment(String val)
   {
      List cmdarg = new ArrayList(); 
      
      if (commandPosition == -1) {
         // if not a command position type parameter then we need to add a switch
         String sw = name;
         if(commandSwitch != null)
         {
            sw = commandSwitch;
         }
         if (switchType.equalsIgnoreCase("normal")) {
            cmdarg.add( "-"+sw);
            cmdarg.add(val);
            
         }
         else {
            cmdarg.add(sw+"="+val);
   
         }
       
      }
      else
      {
         cmdarg.add(val);
      }
      
      return cmdarg;
   }
   
   /**
    * @return
    */
   public int getCommandPosition() {
      return commandPosition;
   }

   /**
    * @return
    */
   public String getCommandSwitch() {
      return commandSwitch;
   }

   /**
    * @return
    */
   public String getSwitchType() {
      return switchType;
   }

   /**
    * @param i
    */
   public void setCommandPosition(int i) {
      commandPosition = i;
   }

   /**
    * @param string
    */
   public void setCommandSwitch(String string) {
      commandSwitch = string;
   }

   /**
    * @param string
    */
   public void setSwitchType(String string) {
      switchType = string;
   }

}
