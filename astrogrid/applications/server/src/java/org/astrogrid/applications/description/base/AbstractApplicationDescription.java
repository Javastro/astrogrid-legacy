/*
 * $Id: AbstractApplicationDescription.java,v 1.4 2004/07/26 12:07:38 nw Exp $
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
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base implementaiton of {@link org.astrogrid.applications.description.ApplicationDescription}
 * <p />
 * Implements all methods in the <tt>ApplicationDescription</tt> interface apart from {@link org.astrogrid.applications.description.ApplicationDescription#initializeApplication(String, User, Tool)},
 * which is left abstract
 * <p />
 * Implemetation is based on two maps - for interfaces and parameters. In addition to the methods of the <tt>ApplicationDescription</tt> interface, this class
 * provides methods for adding items to these maps ({@link #addInterface(ApplicationInterface)}, {@link #addParameterDescription(ParameterDescription)}), and
 * an implementation of {@link #toString()} that produces a formatted dump of the application description. 
 * @author Noel WInstanley
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public abstract class AbstractApplicationDescription implements ApplicationDescription {
   private String name;
   /** the interfaces supported by this application */
   private final Map interfaces;

   /** the parameters defined by this applicaiton */
   private final Map parameterMap;
   /** an environment object of supporting components (name generation, etc).*/
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
   /** set the name of the application description */
   public void setName(String name) {
       this.name = name;
   }

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
   

   public ParameterDescription[] getParameterDescriptions()
   {

         return (ParameterDescription[])parameterMap.values().toArray(new ParameterDescription[]{});         
   }

   public ParameterDescription getParameterDescription(String parameterName) throws ParameterDescriptionNotFoundException
   {
      if (!parameterMap.containsKey(parameterName))
      {
         throw new ParameterDescriptionNotFoundException(parameterName);
      }
      else
      return (ParameterDescription)parameterMap.get(parameterName);
   }
   
   /** Add a parameter description to this application description 
 * @param param the description to add
 */
public void addParameterDescription(ParameterDescription param) {
       parameterMap.put(param.getName(),param);
   }
   

   public ApplicationInterface getInterface(String interfaceName) throws InterfaceDescriptionNotFoundException
   {
      if(interfaces.containsKey(interfaceName)){
         return (BaseApplicationInterface)interfaces.get(interfaceName);
      }
      else
        throw new InterfaceDescriptionNotFoundException("unknown interface="+interfaceName);
   }
   
   public ApplicationInterface[] getInterfaces() {
       Collection c = interfaces.values();
       return (ApplicationInterface[])c.toArray(new ApplicationInterface[]{});
   }



    /** produce a nicely-formatted dump of the metadata for this application 
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


