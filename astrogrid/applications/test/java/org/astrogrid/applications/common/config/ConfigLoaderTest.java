/*
 * $Id: ConfigLoaderTest.java,v 1.2 2004/03/23 12:51:25 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import org.astrogrid.applications.common.ApplicationsConstants;

import junit.framework.TestCase;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class ConfigLoaderTest extends TestCase {

   /**
    * Constructor for ConfigLoaderTest.
    * @param arg0
    */
   public ConfigLoaderTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(ConfigLoaderTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

   final public void testLoadConfig() {
      RawPropertyConfig config = ConfigLoader.LoadConfig("rubbish");
      assertNotNull(config);
      String value = config.getProperty(ApplicationsConstants.CONFIGFILEKEY);
      assertNotNull(value);
   }

}
