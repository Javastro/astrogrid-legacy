/*
 * $Id: Configuration.java,v 1.3 2003/08/28 15:53:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.config;


/**
 * A static singleton so that all packages have access to the *same*
 * configuration file (for now) in one runtime environment.  This is partly
 * to make things easier for the user, but also because it'll be quite tricky
 * (and unnecessary?) for a package to understand which service is using it.
 * <p>
 * For example, the SecurityDelegate will need to know where the policy files
 * are, and what security servers are available to carry out
 * authentication/authorisation.  If we run a MySpace and a Datacenter on one
 * Axis server, these will run in a common VM and this singleton will be the
 * same to both.
 * <p>
 * But... delegate code from this package will need to run under other services
 * (eg JobFlow) and so will need to read a JobFlow configuration file...
 * <p>
 * During the meanwhilst, here is a simple static singleton, that needs to be
 * initialised with a call to init() before being used.
 * <p>
 * In fact this might be sufficient - each package that wants to include a
 * new properties file can just call init() again...
 *
 * @author M Hill
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public abstract class Configuration
{
   public static String defaultFilename = "AstroGridConfig.properties";

   private static Properties properties = new Properties();

   /**
    * Null initialisation - used for test harnesses etc
    *
   public static void init()
   {
      properties = new Properties();
   }

   /**
    * Loads the file at the given filepath as a properties file
    */
   public static void load(String filepath) throws IOException
   {
      load(new FileInputStream(filepath));
   }

   /**
    * Loads the properties at the given inputstream
    */
   public static void load(InputStream in) throws IOException
   {
      properties.load(in);
   }

   /**
    * Loads the properties from the given url
    */
   public static void load(URL url) throws IOException
   {
      properties.load(url.openConnection().getInputStream());
   }

   /**
    * Returns the property value of the given key.
    */
   public static String getProperty(String key)
   {
      return properties.getProperty(key);
   }

   /**
    * Sets the given property - useful for tests.
    */
   public static void setProperty(String key, String value)
   {
      properties.setProperty(key, value);
   }

   /**
    * For backwards compatibility - returns the value of the given
    * catalogue+"."+key
    *
   public static String getCatProperty(String key, String category)
   {
      return properties.getProperty(category+"."+key);
   }
    */

   /**
    * Returns the property value of the given key, or the given default if
    * the key is not found or the properties file has not been created (eg
    * this has not been initialised)
    */
   public static String getProperty(String key, String defaultValue)
   {
      if (properties == null)
      {
         return defaultValue;
      }

      return properties.getProperty(key, defaultValue);
   }
}

