/*
 * $Id: RegistryEntryBuilder.java,v 1.1 2004/03/24 17:13:15 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.beans.resource.VOResource;

/**
 * Creates a bean based description of the CEA entries for a CommonExecutionController instance. 
 * Requires a template file of the VODescription basic parts to create the
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class RegistryEntryBuilder {

   private VODescription template;

   private ApplicationList applist;

   /**
    * 
    */
   public RegistryEntryBuilder(ApplicationList apps, VODescription template) {
      
      this.applist = apps;
      this.template = template;
   }
   
   public VODescription makeEntry()
   {
      VODescription vodesc = new VODescription();
      VOResource voResourceTemplate = (VOResource)template.findXPathValue("//VOResource");
      
      String voResourceTemplateXML;
      
      return vodesc;
      
      
   }
   
   /**
    * Make an ApplicationList from a full configuration. This is a not a deep copy - references to the applicationDescription objects are placed in the ApplicationList
    * @param clecConfig a configuration for a command line execution controller
    * @return
    * @TODO might be better to refactor the original schema so that there was a base type for the common execution contoller configs...
    */
   public static ApplicationList makeApplist(CommandLineExecutionControllerConfig clecConfig)
   {
      ApplicationList result = new ApplicationList();
      
      for (int i = 0; i < clecConfig.getApplicationCount(); i++) {
         result.addApplicationDefn(clecConfig.getApplication(i));
      }
      return result;
   }
}