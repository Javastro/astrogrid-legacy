/*
 * $Id: SimpleConfig.java,v 1.2 2003/08/27 10:08:17 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.config;


/**
 * A simple accessor to the properties config file until I can work out
 * what is wrong what the factory/implementation/etc version
 *
 * @author M Hill
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import sun.security.krb5.internal.crypto.e;

public class SimpleConfig
{
   private static String filename = "ASTROGRID_datasetconfig.properties";

   private Properties properties = new Properties();

   private static SimpleConfig instance = new SimpleConfig();

   /** class initialisation - loads the properties.
    */
   public SimpleConfig()
   {

      try
      {
         properties.load(new FileInputStream(filename));

         if (properties.getProperty("PARSER.VALIDATION") == null)
         {
            throw new IOException("could not locate PARSER.VALIDATION in config file - is the file missing?");
         }
      }
      catch (IOException e)
      {
         System.out.println("ERROR: Could not load property file "+filename);
         e.printStackTrace(System.out);
      }
   }

   public static String getProperty(String key, String category)
   {
      return instance.properties.getProperty(key);
   }
}

