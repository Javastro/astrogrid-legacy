/*
 * $Id: ConfigTest.java,v 1.1 2003/09/15 21:51:45 pah Exp $
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

package org.astrogrid.community.common;

import junit.framework.TestCase;

/**
 * Very simple test of the config operation for tests. The configuration mechanism allows for a different configuration operation when running in test mode.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
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
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG);
      Config config = ConfigLoader.LoadConfig();
      assertEquals("policy", config.getProperty(CommunityConstants.DATABASE_NAME_KEY)); // this name will probably change in future, so the test needs to be updated.
   }

}
