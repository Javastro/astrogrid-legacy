/*
 * $Id: CmdLineApplicationCreator.java,v 1.1 2004/01/16 22:18:58 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications;

import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.datacentre.DataCentreApplication;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationDescriptions;
import org
   .astrogrid
   .applications
   .description
   .exception
   .ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.manager.CommandLineApplicationController;
import org.astrogrid.community.User;

public class CmdLineApplicationCreator extends ApplicationFactory {
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(CmdLineApplicationCreator.class);
   private static CmdLineApplicationCreator instance = null;
   private CommandLineApplicationController appcon;
   private CmdLineApplicationCreator(CommandLineApplicationController controller) {
      appcon = controller;
   }
// I think that this only works on primitives....
   public static CmdLineApplicationCreator getInstance(CommandLineApplicationController controller) {
      if (instance == null) {
         synchronized (CmdLineApplicationCreator.class) {
            if (instance == null) {
               instance = new CmdLineApplicationCreator(controller);
            }
         }

      }
      return instance;

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.ApplicationFactory#createApplication(java.lang.String)
    */
   public AbstractApplication createApplication(String applicationId, User user)
      throws ApplicationDescriptionNotFoundException {
      logger.info("creating application instance of " + applicationId);
      CmdLineApplication app = null;
      ApplicationDescription description =
         appcon.getApplicationDescriptions().getDescription(applicationId);
      String instanceClass = description.getInstanceClass();
      // if a specialized version of the class is required
      if (instanceClass != null && instanceClass.length() > 0) {
         try {
            Class clazz = Class.forName(instanceClass);
            app = (CmdLineApplication)clazz.newInstance();
            app.controller = appcon;
            app.user = user;
            
         }
         catch (Exception e) {
           logger.error("Could not find the application specialization class falling back on the default",e);
           app = new CmdLineApplication(appcon, user);
         } 
      }
      else {
         // otherwise just use the standard commandline application
         app = new CmdLineApplication(appcon, user);
         
      }
      app.setApplicationDescription(description);
      return app;
   }

}
