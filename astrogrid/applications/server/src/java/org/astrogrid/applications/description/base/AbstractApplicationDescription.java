/*
 * $Id: AbstractApplicationDescription.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

package org.astrogrid.applications.description.base;

import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * abstract base implementaiton of {@link ApplicationDescription}
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public abstract class AbstractApplicationDescription implements ApplicationDescription {
   private String name;


   /**
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ApplicationInterface
    */
   private final Map interfaces;

   /**
    *@link aggregation
    *      @associates org.astrogrid.applications.description.ParameterDescription
    */
   private final Map parameterMap;
    protected final ApplicationDescriptionEnvironment env;
   
   /** 
    *  Construct a new AbstractApplicationDescription
    * @param env environment object (useful shared objects for application descriptions)
    */
   public AbstractApplicationDescription(ApplicationDescriptionEnvironment env)
   {
      interfaces = new HashMap();
      parameterMap = new HashMap();
      this.env = env;
   }
   
   public void setName(String name) {
       this.name = name;
   }
   /**
    * @return
    */
   public String getName() {
      return name;
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
    * get the full list of possible parameters definitions for this application.
    * @return the array of parameter definitions
    */
   public ParameterDescription[] getParameterDescriptions()
   {

         return (ParameterDescription[])parameterMap.values().toArray(new ParameterDescription[]{});         
   }
   /**
    * 
    * @param name
    * @return
    */
   public ParameterDescription getParameterDescription(String name) throws ParameterDescriptionNotFoundException
   {
      if (!parameterMap.containsKey(name))
      {
         throw new ParameterDescriptionNotFoundException(name);
      }
      else
      return (ParameterDescription)parameterMap.get(name);
   }
   
   public void addParameterDescription(ParameterDescription param) {
       parameterMap.put(param.getName(),param);
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
         return (BaseApplicationInterface)interfaces.get(name);
      }
      else
        throw new InterfaceDescriptionNotFoundException("unknown interface="+name);
   }
   
   public ApplicationInterface[] getInterfaces() {
       Collection c = interfaces.values();
       return (ApplicationInterface[])c.toArray(new ApplicationInterface[]{});
   }



    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer paramBuff = new StringBuffer();
        StringBuffer interfaceBuff = new StringBuffer();
        ParameterDescription[] paramDescs = getParameterDescriptions();
        for (int i = 0 ; i < paramDescs.length; i++) {
            paramBuff.append("\n");
            paramBuff.append(paramDescs[i].toString());
        }
        ApplicationInterface[] ifaces = getInterfaces();
        for (int i = 0; i < ifaces.length; i++) {
            interfaceBuff.append("\n");
            interfaceBuff.append(ifaces[i].toString());
        }
        return "Application Description: " + this.getName()
        + "\n" + paramBuff.toString()
        + "\n" +  interfaceBuff.toString()
        + "\n-------------------------------------------------------------";
    }

}


