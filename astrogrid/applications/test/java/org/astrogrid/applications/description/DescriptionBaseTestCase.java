/*
 * $Id: DescriptionBaseTestCase.java,v 1.4 2004/03/24 17:13:15 pah Exp $
 * 
 * Created on 04-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.common.config.ConfigLoader;
import org.astrogrid.applications.manager.CommandLineApplicationController;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DescriptionBaseTestCase extends TestCase {

   /**
    * Small class to indicate that we really do want to create a CeaControllerConfig
    * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
    * @version $Name:  $
    * @since iteration5
    */
   private static class ThisConfig extends CeaControllerConfig {
      public static CeaControllerConfig getInstance() {
         return CeaControllerConfig.getInstance();
      }
   }

   /**
    * 
    */
   public DescriptionBaseTestCase() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param arg0
    */
   public DescriptionBaseTestCase(String arg0) {
      super(arg0);
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG);
   }

   protected CommandLineApplicationController ac;


   protected void setUp() throws Exception {
      super.setUp();
      config = ThisConfig.getInstance();
      inputFile = config.getApplicationConfigFile();
      assertNotNull("application config file", inputFile);
      ac = new CommandLineApplicationController(); //TODO will not need this on IoC
      assertNotNull("Cannot create application controller", ac);
   }


   protected URL inputFile;

   protected String TESTAPPNAME = TestAppConst.TESTAPP_NAME;

   protected CeaControllerConfig config;
   
}
