/*
 * $Id: BaseApplicationInterface.java,v 1.6 2004/08/27 12:49:06 pah Exp $
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
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.ParameterDirection;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

   private final  Map inputs = new HashMap();
   private final Map outputs = new HashMap();


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
       this.addInputParameter(parameterName,Cardinality.MANDATORY);
   }
   
   public void addInputParameter(String parameterName,Cardinality card)
   throws ParameterDescriptionNotFoundException {
       applicationDescription.getParameterDescription(parameterName);// will throw if parameter not known.
      inputs.put(parameterName,card);

   }
 
   public void addInputParameter(String parameterName,int minoccurs, int maxoccurs)
   throws ParameterDescriptionNotFoundException {
       applicationDescription.getParameterDescription(parameterName);// will throw if parameter not known.
      inputs.put(parameterName, new Cardinality(minoccurs,maxoccurs));

   }
   public void addOutputParameter(String parameterName)
      throws ParameterDescriptionNotFoundException {
       this.addOutputParameter(parameterName,Cardinality.MANDATORY);
   }
   public void addOutputParameter(String parameterName,Cardinality card)
   throws ParameterDescriptionNotFoundException {   
      applicationDescription.getParameterDescription(parameterName);
      outputs.put(parameterName,card);

   }
   
   public void addOutputParameter(String parameterName,int minoccurs, int maxoccurs)
   throws ParameterDescriptionNotFoundException {   
      applicationDescription.getParameterDescription(parameterName);
      outputs.put(parameterName, new Cardinality(minoccurs,maxoccurs));

   }
   
   
   
   public String[] getArrayofInputs() {
      return (String[])inputs.keySet().toArray(new String[0]);
   }
   public String[] getArrayofOutputs() {
      return (String[])outputs.keySet().toArray(new String[0]);
   }

   public ParameterDescription getInputParameter(String parameterName)
      throws ParameterNotInInterfaceException {
      ParameterDescription ad = null;
      if (inputs.containsKey(parameterName)) {
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
      if(inputs.containsKey(parameterName))
      {
         retval = ParameterDirection.INPUT;
      }
      if (outputs.containsKey(parameterName)) {
         retval = ParameterDirection.OUTPUT;
      }
      return retval;
   }
   public ParameterDescription getOutputParameter(String parameterName)
       throws ParameterNotInInterfaceException {
       ParameterDescription ad = null;
       if (outputs.containsKey(parameterName)) {
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
     * @see org.astrogrid.applications.description.ApplicationInterface#getParameterCardinality(java.lang.String)
     */
    public Cardinality getParameterCardinality(String name) throws ParameterNotInInterfaceException {
        if (outputs.containsKey(name)) {
            return (Cardinality)outputs.get(name);
        } else if (inputs.containsKey(name)) {
            return (Cardinality)inputs.get(name);
        } else {
            throw new ParameterNotInInterfaceException(name);
        }
        
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ApplicationInterface:");
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append("\n\t applicationDescription: ");
        buffer.append(applicationDescription.getName());

        buffer.append("\n\t inputs: ");
        buffer.append(inputs);
        buffer.append("\n\t outputs: ");
        buffer.append(outputs);
        buffer.append("\n]");
        return buffer.toString();
    }
}
