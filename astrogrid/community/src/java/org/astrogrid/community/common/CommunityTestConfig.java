/*
 * $Id: CommunityTestConfig.java,v 1.1 2003/09/15 05:45:42 pah Exp $
 *
 * Created on 13 September 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.community.common;

import java.util.ResourceBundle;

public class CommunityTestConfig implements Config {
   private ResourceBundle bundle;
   private CommunityTestConfig() {
    bundle = ResourceBundle.getBundle(this.getClass().getPackage().getName()+".Test");
    
   }
   private static CommunityTestConfig instance;

   /**
    * Gets the singleton instance of the class. It creates the class if necessary
    * @return the singleton instance.
    */
   public static CommunityTestConfig getInstance() {
      if (instance == null) {
         instance = new CommunityTestConfig();

      }

      return instance;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.community.common.Config#getProperty(java.lang.String)
    */
   public String getProperty(String key) {
     return bundle.getString(key);
   }
   
}
