/*
 * $Id: CmdLineApplicationCreator.java,v 1.3 2003/12/08 15:00:47 pah Exp $
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

public class CmdLineApplicationCreator extends ApplicationFactory {
   private static CmdLineApplicationCreator instance = null;
   private ApplicationDescriptions ad;
   private CmdLineApplicationCreator(ApplicationDescriptions appdisc)
   {
      ad = appdisc;
   }
   
   public static CmdLineApplicationCreator getInstance(ApplicationDescriptions appdisc){
      if (instance == null) {
         synchronized(CmdLineApplicationCreator.class)
         {
            if (instance == null) {
               instance = new CmdLineApplicationCreator(appdisc);
            }
         }
         
      }
      return instance;

   }


   /* (non-Javadoc)
    * @see org.astrogrid.applications.ApplicationFactory#createApplication(java.lang.String)
    */
   public AbstractApplication createApplication(String applicationId) throws ApplicationDescriptionNotFoundException {
      //TODO - need to make this create the appropriate application class if required by the applicationDescription
      CmdLineApplication app = new CmdLineApplication();
         app.setApplicationDescription(ad.getDescription(applicationId));

      return app;
   }

 }
