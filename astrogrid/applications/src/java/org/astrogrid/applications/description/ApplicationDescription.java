/*
 * $Id: ApplicationDescription.java,v 1.4 2003/12/01 22:24:59 pah Exp $
 * 
 * Created on 14-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
/**
 * This class represents the description of the application. This will be in an extension of wsdl.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO this is only a placeholder until proper implementation is sorted.
 */
public class ApplicationDescription {
   private String name;

   /**
    * The class that is used to instantiate this particular application. 
    */
   private String instanceClass;
   
   private String executionPath;

   /**
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ApplicationInterface
    */
   private Map interfaces;

   /**
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private Set allParameters;
   private Map parameterMap;
   
   public ApplicationDescription()
   {
      this("defaultname");
   }
   
   public ApplicationDescription(String applicationID)
   {
      name = applicationID;
      interfaces = new HashMap();
      allParameters = new HashSet();
      parameterMap = new HashMap();
   }
   /**
    * @return
    */
   public String getName() {
      return name;
   }

   /**
    * @param string
    */
   public void setName(String string) {
      name = string;
   }
   
   /**
    * Add a new interface definition to the application.
    * @param iface the interface to add. should at least have its name property set as this is used as the key under which the parameter is stored.
    */
   public void addInterface(ApplicationInterface iface) {
      //TODO deal with error conditions.
      interfaces.put(iface.getName(), iface);
   }
   
   /**
    * Add a new parameter defintion to the application.
    * @param param the parameter definition - should at least have its name property set as this is used as the key under which the parameter is stored
    */
   public void addParameter(ParameterDescription param)
   {
      //TODO deal with error conditions.
      allParameters.add(param);
      parameterMap.put(param.getName(), param);
   }
   
   /**
    * get the full list of possible parameters definitions for this application.
    * @return the array of parameter definitions
    */
   public ParameterDescription[] getParameters()
   {
      if(allParameters!= null)
      {
         return (ParameterDescription[])allParameters.toArray(new ParameterDescription[0]);
      }
      else
      {
         return null;
      }
      

   }
   /**
    * 
    * @param name
    * @return
    */
   public ParameterDescription getParameter(String name) throws ParameterDescriptionNotFoundException
   {
      if (!parameterMap.containsKey(name))
      {
         throw new ParameterDescriptionNotFoundException();
      }
      else
      return (ParameterDescription)parameterMap.get(name);
   }
   
   /**
    * Gets the named interface.
    * @param name
    * @return
    * @throws InterfaceDescriptionNotFoundException
    */
   public ApplicationInterface getInterface(String name) throws InterfaceDescriptionNotFoundException
   {
      if(interfaces.containsKey(name)){
         return (ApplicationInterface)interfaces.get(name);
      }
      else
        throw new InterfaceDescriptionNotFoundException();
   }
   /**
    * @return
    */
   public String getInstanceClass() {
      return instanceClass;
   }

   /**
    * @param string
    */
   public void setInstanceClass(String string) {
      instanceClass = string;
   }

   /**
    * @return
    */
   public String getExecutionPath() {
      return executionPath;
   }

   /**
    * @param string
    */
   public void setExecutionPath(String string) {
      executionPath = string;
   }

}


