/*
 * $Id: ApplicationInterface.java,v 1.6 2004/01/13 00:12:43 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.wsdl.symbolTable.Parameter;

import org
   .astrogrid
   .applications
   .description
   .exception
   .ParameterDescriptionNotFoundException;
import org
   .astrogrid
   .applications
   .description
   .exception
   .ParameterNotInInterfaceException;
/**
 * Describes a named interface to the application. The inferface describes the list of input and output parameters that make up a particular
 * operation for that application. This object actually stores lists of parameter names for the {@link ParameterDescription} objects that are stored in the parent {@link ApplicationDescription} object. 
 * 

 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationInterface {
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(ApplicationInterface.class);
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   private ApplicationDescription application = null;

   private String name;

   /**
    * list of the {@link ParameterDescription} objects that make up the inputs for this interface.
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private List inputs;

   /**
    * list of the  {@link ParameterDescription} objects that make up the outputs for this interface.
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private List outputs;

   public ApplicationInterface() {
      inputs = new ArrayList();
      outputs = new ArrayList();
   }

   /**
    * @return
    */
   public ApplicationDescription getApplication() {
      return application;
   }

   /**
    * @param description
    */
   public void setApplication(ApplicationDescription description) {
      application = description;
   }

   public void addInputParameter(String name)
      throws ParameterDescriptionNotFoundException {
      ParameterDescription pd = application.getParameter(name);
      inputs.add(name);

   }

   public void addOutputParameter(String name)
      throws ParameterDescriptionNotFoundException {
      // check that the parameter exists
      ParameterDescription pd = application.getParameter(name);
      // add the name
      outputs.add(name);

   }
   public String[] getArrayofInputs() {
      return (String[])inputs.toArray(new String[0]);
   }
   public String[] getArrayofOutputs() {
      return (String[])outputs.toArray(new String[0]);
   }

   public ParameterDescription getInputParameter(String name)
      throws ParameterNotInInterfaceException {
      ParameterDescription ad = null;
      if (inputs.contains(name)) {
         try {
            ad = application.getParameter(name);
         }
         catch (ParameterDescriptionNotFoundException e) {
            logger.error(
               "this should not happen - the checks on the original storage of the parameters should prevent it - internal program error",
               e);
         }
      }
      else {
         throw new ParameterNotInInterfaceException("unknown parameter="+name);
      }
      return ad;
   }
   public ParameterDirection parameterType(String name)
   {
      ParameterDirection retval = ParameterDirection.NOTFOUND;
      if(inputs.contains(name))
      {
         retval = ParameterDirection.INPUT;
      }
      if (outputs.contains(name)) {
         retval = ParameterDirection.OUTPUT;
      }
      return retval;
   }
   public ParameterDescription getOutputParameter(String name)
       throws ParameterNotInInterfaceException {
       ParameterDescription ad = null;
       if (outputs.contains(name)) {
          try {
             ad = application.getParameter(name);
          }
          catch (ParameterDescriptionNotFoundException e) {
             logger.error(
                "this should not happen - the checks on the original storage of the parameters should prevent it - internal program error",
                e);
          }
       }
       else {
          throw new ParameterNotInInterfaceException("unknown parameter="+name);
       }
       return ad;
    }
    
    public static class ParameterDirection
    {
       private final String direction;
       private ParameterDirection(String dir)
       {
          direction = dir;
         
       }
       
       public static final ParameterDirection INPUT = new ParameterDirection("input");
       public static final ParameterDirection OUTPUT = new ParameterDirection("output");
       public static final ParameterDirection NOTFOUND = new ParameterDirection("not found");
      /* (non-Javadoc)
       * @see java.lang.Object#toString()
       */
      public String toString() {
         return direction;

      }

    }
    

}
