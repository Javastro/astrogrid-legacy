/*
 * $Id: AbstractApplicationController.java,v 1.14 2004/03/23 12:51:26 pah Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig;
import org.astrogrid.applications.beans.v1.CommandLineApplication;
import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.description.ApplicationDescriptionNotLoadedException;
import org.astrogrid.applications.description.ApplicationDescriptions;
import org.astrogrid.applications.description.DescriptionLoader;
import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.applications.description.SimpleDescriptionLoader;

/**
 * Provides a some generic applicationController methods.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public abstract class AbstractApplicationController implements CommonExecutionController {
   protected CeaControllerConfig config;
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
   protected Status status;
   protected MySpaceLocator mySpaceLocator;
   protected RegistryQueryLocator registryLocator;
   

      protected CommandLineExecutionControllerConfig clconf; //REFACTORME - should use the base type of all of the ControllerConfigs? should be initialized in the subclass
   AbstractApplicationController(CeaControllerConfig config, RegistryQueryLocator registryQueryLocator, MySpaceLocator mySpaceLocator) {
      logger.info("initializing application controller");
      status = new Status();
      simpleDescriptions = new HashMap();
      // get the datasource
      this.config = config;
      this.mySpaceLocator = mySpaceLocator;
      this.registryLocator = registryQueryLocator;
      db = config.getDataSource();

      
      URL applicationConfigFile;
      try {
         // load the application descriptions
         DescriptionLoader dl = new DescriptionLoader(this);
         applicationConfigFile = config.getApplicationConfigFile();
         dl.loadDescription(applicationConfigFile);
         status.addMessage("loaded application descriptions");
         //now the simple descriptions (really just not parsing the input XML as much - this is a bit of a cheat, perhaps there should be a serializer for the ApplicationDescription objects.
         SimpleDescriptionLoader sdl = new SimpleDescriptionLoader(this);
         sdl.loadDescription(applicationConfigFile);
         status.addMessage("loaded simple descriptions");

         Unmarshaller um = new Unmarshaller(CommandLineExecutionControllerConfig.class);
     
         InputSource saxis;
         

         saxis = new InputSource(config.getApplicationConfigFile().openStream());
         clconf = (CommandLineExecutionControllerConfig)um.unmarshal( saxis);
         
         
         }
      catch (MalformedURLException e1) {
         status.addError("failed to find application config file");
         logger.error("failed to find application config file", e1);
      } catch (ApplicationDescriptionNotLoadedException e) {
         status.addError("failed to load application config");
         logger.error("failed to load application config", e);
      } catch (IOException e) {
         status.addError("failed to load application config");
         logger.error("failed to load application config", e);
      } catch (CastorException e) {
         status.addError("failed to load application config");
         logger.error("failed to load application config", e);
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

 
   public void addSimpleDescription(SimpleApplicationDescription d) {
      simpleDescriptions.put(d.getName(), d);
   }

   /**
    * @return
    */
   public MySpaceLocator getMySpaceLocator() {
      return mySpaceLocator;
   }

   /**
    * @return
    */
   public RegistryQueryLocator getRegistryLocator() {
      return registryLocator;
   }

   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#getApplicationDescription(java.lang.String)
    */
   public org.astrogrid.applications.beans.v1.ApplicationBase getApplicationDescription(
      String applicationID)
      throws CeaException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("AbstractApplicationController.getApplicationDescription() not implemented");
   }

   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#listApplications()
    */
   public ApplicationList listApplications() throws CeaException {
      ApplicationList result = new ApplicationList();
      
      for (int i = 0; i < clconf.getApplicationCount(); i++) {
         
         org.astrogrid.applications.beans.v1.CommandLineApplication app = clconf.getApplication(i);
         
         result.addApplicationDefn(app);
      }
      
      return result;
      
   }

   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#returnRegistryEntry()
    */
   public String returnRegistryEntry() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("AbstractApplicationController.returnRegistryEntry() not implemented");
   }

}
