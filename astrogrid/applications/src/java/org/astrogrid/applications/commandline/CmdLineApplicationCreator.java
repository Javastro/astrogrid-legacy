/*
 * $Id: CmdLineApplicationCreator.java,v 1.5 2003/12/31 00:56:17 pah Exp $
 *
 * Created on 14 October 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.commandline;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.ApplicationFactory;
import org.astrogrid.applications.datacentre.DataCentreApplication;
import org.astrogrid.applications.description.ApplicationDescriptions;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.manager.CommandLineApplicationController;
import org.astrogrid.community.User;

public class CmdLineApplicationCreator extends ApplicationFactory {
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(CmdLineApplicationCreator.class);
   private static CmdLineApplicationCreator instance = null;
   private CommandLineApplicationController ad;
   private CmdLineApplicationCreator(CommandLineApplicationController controller)
   {
      ad = controller;
   }
   
   public static CmdLineApplicationCreator getInstance(CommandLineApplicationController controller){
      if (instance == null) {
         synchronized(CmdLineApplicationCreator.class)
         {
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
   public AbstractApplication createApplication(String applicationId, User user) throws ApplicationDescriptionNotFoundException {
      //TODO - need to make this create the appropriate application class if required by the applicationDescription
      logger.info("creating application instance of "+applicationId);
      CmdLineApplication app = new CmdLineApplication(ad, user);
         app.setApplicationDescription(ad.getApplicationDescriptions().getDescription(applicationId));

      return app;
   }

 }
