/*
 * $Id: BaseTestCase.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 17-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications;

import junit.framework.TestCase;

import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.common.config.RawPropertyConfig;
import org.astrogrid.applications.common.config.ConfigLoader;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 17-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public abstract class BaseTestCase extends TestCase {
  
  
   protected CeaControllerConfig config;
   static protected org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(BaseTestCase.class);

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
    * Very base test case to make sure that the test config is loaded.
    */
   public BaseTestCase() {
      this("applications integration");
      // TODO Auto-generated constructor stub
   }

   /**
    * @param arg0
    */
   public BaseTestCase(String arg0) {
      super(arg0);
// not done any more...      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG); // set up the test config as early as possible
      config = ThisConfig.getInstance();
      assertNotNull("cannot load config", config);

   }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */

   /** 
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

}
