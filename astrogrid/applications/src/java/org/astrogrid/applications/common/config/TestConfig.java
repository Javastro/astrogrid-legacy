/*
 * $Id: TestConfig.java,v 1.5 2004/03/23 19:46:04 pah Exp $
 *
 * Created on 13 September 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.common.config;

import java.util.ResourceBundle;

import javax.sql.DataSource;

public class TestConfig implements RawPropertyConfig {
   private ResourceBundle bundle;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(TestConfig.class);
   private TestConfig() {
//      bundle = ResourceBundle.getBundle(this.getClass().getPackage().getName()+".Test");
      bundle = ResourceBundle.getBundle("Test");
    
   }
   private static TestConfig instance;

   /**
    * Gets the singleton instance of the class. It creates the class if necessary
    * @return the singleton instance.
    */
   public static TestConfig getInstance() {
      if (instance == null) {
         instance = new TestConfig();

      }

      return instance;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.common.Config#getProperty(java.lang.String)
    */
   public String getProperty(String key) {
     return bundle.getString(key);
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.common.config.Config#getDataSource(java.lang.String)
    */
   public DataSource getDataSource(String key) {
      logger.info("the test config does not do this - need to use the @link BaseDBTestCase instead during tests");
      return null; 
   }

}
