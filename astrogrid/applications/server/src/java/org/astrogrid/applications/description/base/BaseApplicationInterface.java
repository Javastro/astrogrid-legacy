/*
 * $Id: BaseApplicationInterface.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description.base;

import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.ParameterDirection;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * basic implementaiton of {@link AppllicationInterface}
 

 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class BaseApplicationInterface implements ApplicationInterface {
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(BaseApplicationInterface.class);
   /**
    *  Construct a new BaseApplicationInterface
    * @param name the name of the interface
    * @param description the applicationDescription this interface belongs to
    */
    public BaseApplicationInterface(String name, ApplicationDescription description) {
        this.name = name;
        this.applicationDescription = description;
    } 
       
      
   public String getName() {
      return name;
   }

   private final ApplicationDescription applicationDescription;

   protected final String name;

   /**
    * list of the {@link ParameterDescription} objects that make up the inputs for this interface.
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private final  List inputs = new ArrayList();

   /**
    * list of the  {@link ParameterDescription} objects that make up the outputs for this interface.
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private final List outputs = new ArrayList();


   /**
    * @return
    */
   public ApplicationDescription getApplicationDescription() {
      return applicationDescription;
   }

/** add input parameter to the inteface.
 * 
 * @param name name of the parameter
 * @throws ParameterDescriptionNotFoundException if parameter name is not already defined in the applicationDescription this
 * interface is owned by.
 */
   public void addInputParameter(String name)
      throws ParameterDescriptionNotFoundException {
      ParameterDescription pd = applicationDescription.getParameterDescription(name);
      inputs.add(name);

   }
    /** @see #addInputParameter */
   public void addOutputParameter(String name)
      throws ParameterDescriptionNotFoundException {
      // check that the parameter exists
      ParameterDescription pd = applicationDescription.getParameterDescription(name);
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
            ad = applicationDescription.getParameterDescription(name);
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
   public ParameterDirection getParameterDirection(String name)
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
             ad = applicationDescription.getParameterDescription(name);
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
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Interface: " + getName() 
        + " \n\tinputs: " + Arrays.asList(getArrayofInputs()).toString()
        + "\n\toutputs: " +  Arrays.asList(getArrayofOutputs()).toString();
    }

}
