/*
 * $Id: DescriptionLoader.java,v 1.2 2003/11/27 12:40:48 pah Exp $
 *
 * Created on 26 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.description;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.manager.AbstractApplicationController;
import org.astrogrid.applications.manager.ApplicationController;

public class DescriptionLoader {
   private AbstractApplicationController appController;

   private Digester digester;

   /**
    * @label Loads
    * @supplierCardinality 1..* 
    */
   private ApplicationDescription lnkApplicationDescription;

   public DescriptionLoader(AbstractApplicationController ac) {
      appController = ac;
      digester = createDigester();
   }
   public boolean loadDescription(File configFile) {
      ApplicationDescriptions descriptions = null;
      boolean success = false;
      try {
         descriptions = (ApplicationDescriptions)digester.parse(configFile);
         success = true;
      }
      catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         success = false;
      }
      catch (SAXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         success = false;
      }
      appController.setApplicationDescriptions(descriptions);
      return success;
   }
   Digester createDigester() {
      //create the digester object

      Digester d = new Digester();
      d.setValidating(false); //TODO would be better to make this validate...

      d.addObjectCreate("ApplicationController", ApplicationDescriptions.class);

      d.addObjectCreate(
         "ApplicationController/Application",
         ApplicationDescription.class);
      // set the appropriate attributes
      d.addSetProperties("ApplicationController/Application");

      d.addSetNext("ApplicationController/Application", "addDescription");
      // finally add the application description to the list

      return d;
   }
}
