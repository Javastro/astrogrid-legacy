/*
 * $Id: CommunitySyspropConfig.java,v 1.2 2003/09/15 21:51:45 pah Exp $
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

package org.astrogrid.community.common;

/**
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class CommunitySyspropConfig implements Config {
   private static CommunitySyspropConfig instance;

   private CommunitySyspropConfig()
   {
      
   }
   public static CommunitySyspropConfig getInstance() {
      if (instance == null) {
         instance = new CommunitySyspropConfig();
         
      }

      return instance;
   }
   public String getProperty(String propertyKey) {
      return System.getProperty(propertyKey); // compatible with Dave's setup for now....
   }

}
