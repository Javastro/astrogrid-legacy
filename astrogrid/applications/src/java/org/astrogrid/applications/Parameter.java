/*
 * $Id: Parameter.java,v 1.10 2004/04/20 09:03:22 pah Exp $
 *
 * Created on 13 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import java.util.List;

import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.description.ParameterDescription;

/**
 * Represents an raw  parameter to the application. This will simply be a string in most cases. The parameter definition is stored in the associated {@link org.astrogrid.applications.description.ParameterDescription}
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @stereotype entity 
 */
abstract public class Parameter implements ParameterReturner, ParameterGetter{
   
   static protected org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(Parameter.class);
   protected AbstractApplication application;
   protected boolean hasReadError = false;
   protected boolean hasWriteBackError=false;
  
/**
 * The name of the parameter.
 */
   protected String name;
   
   /**
    * the raw string that is passed in via the {@link org.astrogrid.applications.manager.ApplicationController#initializeApplication(String, String, String, User, org.astrogrid.applications.ParameterValues)} call.
    */
   protected String rawValue;
   
   /**
    * the processed parameter value(s) contained as a List of strings e.g. after a file reference is retrieved from mySpace
    */
   protected List argValue;
   
   /**
    * The parameter description.
    */
   protected ParameterDescription parameterDescription;
   
   
   public Parameter(AbstractApplication application, ParameterDescription parameterDescription)
   {
      this.application = application;
      this.parameterDescription = parameterDescription;
      this.name=parameterDescription.getName(); // just as a shortcut
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
      return parameterDescription.getName() +" rawval="+ rawValue + " argvals="+argValue;
   }


   /**
    * Performs whatever actions are necessary for the processing the parameter. Most of the real work is done by looking in the {@link ParameterDescription} object.
    * @return true if the processing did not have any errors
    */
   public boolean process() throws CeaException {
 //TODO need to think about errors  
      argValue = parameterDescription.process(this);   
      return true;

   }
   
   /**
    * writes a parameter back to the invoking process. The default is to do nothing, most parameter types do not get written back
    * @return
    */
   public boolean writeBack() throws ParameterWriteBackException
   {
      hasWriteBackError = false;
      return true;
   }

   /**
    * @return
    */
   public List getArgValue() {
      return argValue;
   }

   /**
    * @return
    */
   public ParameterDescription getParameterDescription() {
      return parameterDescription;
   }

   /**
    * @return
    */
   public String getName() {
      return name;
   }

   /**
    * @return
    */
   public AbstractApplication getApplication() {
      return application;
   }

   /**
    * REFACTORME - this should not really be necessary - should be internally generated...
    * @param list
    * @deprecated
    */
   public void setArgValue(List list) {
      argValue = list;
   }

}
