/*
 * $Id: ConfigTest.java,v 1.2 2004/03/23 12:51:26 pah Exp $
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

package org.astrogrid.applications;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.astrogrid.applications.common.ApplicationsConstants;

import junit.framework.TestCase;

/**
 * Very simple test of the config operation for tests. The configuration mechanism allows for a different configuration operation when running in test mode.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ConfigTest extends BaseTestCase {

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
      try {
         URL f = config.getApplicationConfigFile();
         assertTrue(f.getPath().endsWith("TestApplicationConfig.xml")); 
      }
      catch (MalformedURLException e) {
         fail("the url reference to the test config file is wrong");
      }
   }

}
