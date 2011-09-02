/* $Id: TestHttpApplicationLibrary.java,v 1.3 2011/09/02 21:55:53 pah Exp $
 * Created on 30-July-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.astrogrid.applications.http.test;

import java.io.IOException;
import java.net.URL;

import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.BaseApplicationDescriptionFactory;
import org.astrogrid.applications.description.ConfigFileReadingDescriptionLibrary;

/**
 * Returns pretend meta data about the TestWebServer test services. Acts as both
 * a test registryQuerier, and a RegistryService.
 *
 * @author jdt
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Sep 2008 - made into an {@link ApplicationDescriptionLibrary}
 */
public class TestHttpApplicationLibrary extends ConfigFileReadingDescriptionLibrary {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(TestHttpApplicationLibrary.class);

   /**
    * Ctor
 * @param df 
 * @throws IOException 
    *
    */
   public TestHttpApplicationLibrary(URL locator, BaseApplicationDescriptionFactory df) 
          {
      super(locator, df);
      addApplication("/helloWorld-app.xml");
      addApplication("/Echoer-app.xml");
      addApplication("/HelloYou-app.xml");
      addApplication("/Adder-app.xml");
      addApplication("/Adder-post-app.xml");
      addApplication("/Bad404-app.xml");
      addApplication("/BadTimeOut-app.xml");
      addApplication("/BadMalformedURL-app.xml");
      addApplication("/Adder-preprocess-app.xml");

   }

   /**
    * Add a CeaHttpApplicationDefinition to our "registry" given the object serialised
    * to xml
    *
    * @param file
    *           filename of serialised object
    */
   private void addApplication(String file)
   {
       assert loadFileOrDirectory(TestHttpApplicationLibrary.class.getResource(file)): "failed to load "+file;
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
    */
   @Override
public String getName() {
      return "TestRegistryQuerier";
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
    */
   @Override
public String getDescription() {
      return "A test RegistryQuerier for unit testing";
   }

   /*
    * (non-Javadoc)
    *
    * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
    *      No installation tests required, since only used for unit testing.
    */
   @Override
public Test getInstallationTest() {
      return null;
   }


}