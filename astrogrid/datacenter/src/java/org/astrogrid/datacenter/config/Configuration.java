/*
 * $Id: Configuration.java,v 1.7 2003/09/15 15:47:59 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.config;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.astrogrid.log.Log;


/**
 * A static singleton so that all packages have access to the *same*
 * configuration set (for now) in one runtime environment.  This is partly
 * to make things easier for the user, but also because it'll be quite tricky
 * for a package to understand which service is using it.
 * <p>
 * For example, the SecurityDelegate will need to know where the policy files
 * are, and what security servers are available to carry out
 * authentication/authorisation.  If we run a MySpace and a Datacenter on one
 * Axis server, these will run in a common VM and so this configuration singleton
 * will be the same for both.
 * <p>
 * But also... delegate code from this package will need to run under other services
 * (eg JobFlow) and so will need to read a JobFlow configuration file...
 * <p>
 * So although this configuration is a common access point, it can be used to
 * access several configuration files.  Any application can call the 'load'
 * methods and properties will be loaded from the given file/url/etc.
 * <p>
 * This does mean that packages using the configuration file must make sure their
 * keys are unique. The most reliable way to do this is to prefix each key with
 * the package name (ie the code namespace) but this doesn't produce very nice
 * property files for humans to edit.
 * <p>
 * @todo work out a nice way of ensuring keys are unique.
 *
 * @author M Hill
 */

public abstract class Configuration
{
   /** Made-up constant */
   public static final String DEFAULT_FILENAME = "AstroGridConfig.properties";

   /** For debug purposes - a list of locations that the configuration file
    * has been loaded from @see getLocations() */
   private static String locations = null;

   /** The way this implementation works, from a Properties file */
   private static Properties properties = new Properties();

   /**
    * Loads the file at the given filepath as a properties file
    */
   public static void load(String filepath) throws IOException
   {
      addLocation(filepath);

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
      addLocation(url.toString());

      properties.load(url.openConnection().getInputStream());
   }

   /**
    * Returns the location of the configuration file - useful when reporting
    * errors that things can't be found or are incorrect, so the user really
    * knows they're looking in the right place
    */
   public static String getLocations()
   {
      if (locations == null)
      {
         return "(no file loaded)";
      }
      else
      {
         return locations;
      }
   }

   /**
    * convenient way of adding locations to the string
    */
   private static void addLocation(String newLocation)
   {
      if (locations == null)
      {
         locations = newLocation;
      }
      else
      {
         locations = locations+", "+newLocation;
      }
   }

   /**
    * Returns the property value of the given key.
    */
   public static String getProperty(String key)
   {
      affirmKeyValid(key);

      return properties.getProperty(key);
   }

   /**
    * Sets the given property - useful for tests.
    */
   public static void setProperty(String key, String value)
   {
      affirmKeyValid(key);

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
      affirmKeyValid(key);

      if (properties == null)
      {
         return defaultValue;
      }

      return properties.getProperty(key, defaultValue);
   }


   /**
    * Keys in the current Property implementation must not contain whitespace,
    * colons or equals
    */
   public static void affirmKeyValid(String key)
   {
      Log.affirm(key.indexOf(":") == -1, "Key '"+key+"' contains an illegal character - a colon");
      Log.affirm(key.indexOf(" ") == -1, "Key '"+key+"' contains an illegal character - a space");
      Log.affirm(key.indexOf("=") == -1, "Key '"+key+"' contains an illegal character - an equals sign");
   }

}

