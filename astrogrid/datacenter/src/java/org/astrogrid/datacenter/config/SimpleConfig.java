/*
 * $Id: SimpleConfig.java,v 1.1 2003/08/26 23:21:51 mch Exp $
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
   public SimpleConfig() throws IOException
   {
      properties.load(new FileInputStream(filename));

      if (properties.getProperty("PARSER.VALIDATION") == null)
      {
         throw new IOException("could not locate PARSER.VALIDATION in config file - is the file missing?");
      }
   }

   public static String getProperty(String key, String category)
   {
      return instance.properties.getProperty(key);
   }
}

