/*
 * $Id: SyspropConfig.java,v 1.3 2004/03/23 12:51:25 pah Exp $
 * 
 * Created on 09-Sep-2003 by pah
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import javax.sql.DataSource;

/**
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class SyspropConfig implements RawPropertyConfig {
   private static SyspropConfig instance;

   private SyspropConfig()
   {
      
   }
   public static SyspropConfig getInstance() {
      if (instance == null) {
         instance = new SyspropConfig();
         
      }

      return instance;
   }
   public String getProperty(String propertyKey) {
      return System.getProperty(propertyKey); // compatible with Dave's setup for now....
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.common.config.Config#getDataSource(java.lang.String)
    */
   public DataSource getDataSource(String key) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("SyspropConfig.getDataSource() not implemented");
   }

}
