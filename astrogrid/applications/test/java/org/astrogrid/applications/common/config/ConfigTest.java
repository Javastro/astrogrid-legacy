/*
 * $Id: ConfigTest.java,v 1.1 2003/12/01 22:24:59 pah Exp $
 * 
 * Created on 14-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import java.io.File;

import org.astrogrid.applications.common.ApplicationsConstants;

import junit.framework.TestCase;

/**
 * Very simple test of the config operation for tests. The configuration mechanism allows for a different configuration operation when running in test mode.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ConfigTest extends TestCase {

   /**
    * Constructor for ConfigTest.
    * @param name
    */
   public ConfigTest(String name) {
      super(name);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ConfigTest.class);
   }

   final public void testTestConfig() {
      // make sure that the test config is used
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG);
      // the argument below has no real significance, as the test config will simply get the Test.Properties file from the classpath
      ApplicationControllerConfig config = ApplicationControllerConfig.getInstance();
      File f = config.getApplicationConfigFile();
      assertEquals("TestApplicationConfig.xml", f.getName()); 
   }

}
