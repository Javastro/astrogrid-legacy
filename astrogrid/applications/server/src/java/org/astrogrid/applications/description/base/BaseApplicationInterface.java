/*
 * $Id: BaseApplicationInterface.java,v 1.3 2004/07/26 00:58:22 nw Exp $
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
 * Basic implementation of {@link org.astrogrid.applications.description.ApplicationInterface}
  <p />
  Implements all the methods of <tt>ApplicationInterface</tt>, plus methods to add parameter to the interface description.

 * @author Paul Harison (pah@jb.man.ac.uk)
 * @author Noel Winstanley
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
 * @param parameterName name of the parameter
 * @throws ParameterDescriptionNotFoundException if parameter name is not already defined in the applicationDescription this
 * interface is owned by.
 */
   public void addInputParameter(String parameterName)
      throws ParameterDescriptionNotFoundException {
       applicationDescription.getParameterDescription(parameterName);// will throw if parameter not known.
      inputs.add(parameterName);

   }
    /** @see #addInputParameter */
   public void addOutputParameter(String parameterName)
      throws ParameterDescriptionNotFoundException {
      applicationDescription.getParameterDescription(parameterName);
      outputs.add(parameterName);

   }
   public String[] getArrayofInputs() {
      return (String[])inputs.toArray(new String[0]);
   }
   public String[] getArrayofOutputs() {
      return (String[])outputs.toArray(new String[0]);
   }

   public ParameterDescription getInputParameter(String parameterName)
      throws ParameterNotInInterfaceException {
      ParameterDescription ad = null;
      if (inputs.contains(parameterName)) {
         try {
            ad = applicationDescription.getParameterDescription(parameterName);
         }
         catch (ParameterDescriptionNotFoundException e) {
            logger.error(
               "this should not happen - the checks on the original storage of the parameters should prevent it - internal program error",
               e);
         }
      }
      else {
         throw new ParameterNotInInterfaceException("unknown parameter="+parameterName);
      }
      return ad;
   }
   public ParameterDirection getParameterDirection(String parameterName)
   {
      ParameterDirection retval = ParameterDirection.NOTFOUND;
      if(inputs.contains(parameterName))
      {
         retval = ParameterDirection.INPUT;
      }
      if (outputs.contains(parameterName)) {
         retval = ParameterDirection.OUTPUT;
      }
      return retval;
   }
   public ParameterDescription getOutputParameter(String parameterName)
       throws ParameterNotInInterfaceException {
       ParameterDescription ad = null;
       if (outputs.contains(parameterName)) {
          try {
             ad = applicationDescription.getParameterDescription(parameterName);
          }
          catch (ParameterDescriptionNotFoundException e) {
             logger.error(
                "this should not happen - the checks on the original storage of the parameters should prevent it - internal program error",
                e);
          }
       }
       else {
          throw new ParameterNotInInterfaceException("unknown parameter="+parameterName);
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
