/*
 * $Id: AbstractApplicationController.java,v 1.6 2003/12/04 13:26:25 pah Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptions;
import org.astrogrid.applications.description.DescriptionLoader;
import org.astrogrid.applications.description.SimpleApplicationDescription;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class AbstractApplicationController implements ApplicationController {
   /**
    * The place where the application controller stores local execution status. 
    */
   private DataSource db;

   /**
    * The store for the descriptions of the applications that this application controller manages.
    */
   private ApplicationDescriptions applicationDescriptions;
   private Map simpleDescriptions;
   
   AbstractApplicationController()
   {
      simpleDescriptions = new HashMap();
      // get the datasource
      ApplicationControllerConfig config = ApplicationControllerConfig.getInstance();
      db = config.getDataSource();
      
      // load the application descriptions
      DescriptionLoader dl = new DescriptionLoader(this);
      dl.loadDescription(config.getApplicationConfigFile());
      
     
   }
   
   /**
    * @return
    */
   public ApplicationDescriptions getApplicationDescriptions() {
      return applicationDescriptions;
   }

   /**
    * @param descriptions
    */
   public void setApplicationDescriptions(ApplicationDescriptions descriptions) {
      applicationDescriptions = descriptions;
   }

   public SimpleApplicationDescription getApplicationDescription(String applicationID) {
      return (SimpleApplicationDescription)simpleDescriptions.get(applicationID);
   }

   public String[] listApplications() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.listApplications() not implemented");
   }

   public String returnRegistryEntry() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.returnRegistryEntry() not implemented");
   }
   
   public void addSimpleDescription(SimpleApplicationDescription d)
   {
      simpleDescriptions.put(d.getName(), d);
   }

}
