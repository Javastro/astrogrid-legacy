/*
 * $Id: AbstractApplicationController.java,v 1.12 2003/12/31 00:56:17 pah Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.astrogrid.applications.common.config.ApplicationControllerConfig;
import org.astrogrid.applications.description.ApplicationDescriptions;
import org.astrogrid.applications.description.DescriptionLoader;
import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.applications.description.SimpleDescriptionLoader;

public abstract class AbstractApplicationController implements ApplicationController {
   /**
    * The place where the application controller stores local execution status. 
    */
   protected DataSource db;
   static protected org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(AbstractApplicationController.class);
   /**
    * The store for the descriptions of the applications that this application controller manages.
    */
   protected ApplicationDescriptions applicationDescriptions;
   protected Map simpleDescriptions;

   AbstractApplicationController() {
      logger.info("initializing application controller");
      simpleDescriptions = new HashMap();
      // get the datasource
      ApplicationControllerConfig config = ApplicationControllerConfig.getInstance();
      db = config.getDataSource();

      // load the application descriptions
      DescriptionLoader dl = new DescriptionLoader(this);
      logger.info(
         "loading application descriptions from "
            + config.getApplicationConfigFile().getAbsoluteFile());
      if (dl.loadDescription(config.getApplicationConfigFile())) {
         logger.info("application descriptions loaded successfully");
      }
      else {
         logger.error("application descriptions were not loaded properly");
      }

      //now the simple descriptions (really just not parsing the input XML as much - this is a bit of a cheat, perhaps there should be a serializer for the ApplicationDescription objects.
      SimpleDescriptionLoader sdl = new SimpleDescriptionLoader(this);
      if (sdl.loadDescription(config.getApplicationConfigFile())) {
         logger.info("loaded simple descriptions");
      }
      else {
         logger.error("simple application descriptions were not loaded properly");
      }

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
      return applicationDescriptions.getApplicationNames();

   }

   public String returnRegistryEntry() {
      //TODO implement this
      return "this is not implemented yet";
   }

   public void addSimpleDescription(SimpleApplicationDescription d) {
      simpleDescriptions.put(d.getName(), d);
   }

}
