/*
 * $Id: ConfigLoader.java,v 1.2 2004/03/23 12:51:25 pah Exp $
 *
 * Created on 13 September 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.common.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Loads an instance of a Config. This class has a default
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration3
 */
public class ConfigLoader {
   private static RawPropertyConfig theconfig;
   public final static int SYSPROP_CONFIG = 1;
   public final static int TEST_CONFIG = 2;
   public final static int JNDI_CONFIG = 3;
   private static int configType;
   static {
      configType = JNDI_CONFIG;  // the default type of config
   }
   private static Map configs = new HashMap(); // A store of configurations
   /**
    * This class only has static members do not allow it to be instatiated.
    */
   private ConfigLoader() {
   }

    protected static RawPropertyConfig LoadConfig(String key)
    {
       switch (configType) {
         case SYSPROP_CONFIG  :
         {      
             return SyspropConfig.getInstance();
         }
         case TEST_CONFIG:{
             return TestConfig.getInstance();
         }
         case JNDI_CONFIG: {
            synchronized(configs)
            {
               
               if(configs.containsKey(key))
               {
                  return (RawPropertyConfig)configs.get(key);
                  
               }
               else
               {
                  theconfig =  new JNDIConfig(key);
                  //fallback to the test config if jndi not present...
                  if(!((JNDIConfig)theconfig).isPropertiesLoaded())
                  {
                     theconfig = TestConfig.getInstance();
                  }
                  configs.put( key, theconfig);
                  return theconfig;
               }
               
               
            }
            
         }
         default :
         {
         
         throw new RuntimeException("unknown configuration type"); // IMPL perhaps should be another exception?
         } 
      }
       
    }

  
   /**
    * @param i
    * @deprecated will start to use a fallover type of config - easier for tests....
    */
   public static void setConfigType(int i) {
      configType = i;
   }

}
