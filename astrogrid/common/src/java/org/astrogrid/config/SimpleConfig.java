/*
 * $Id: SimpleConfig.java,v 1.3 2003/10/08 14:38:56 maven Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;


import java.io.IOException;
import java.net.URL;

/**
 * A static singleton so that all packages have access to the *same*
 * configuration set in one runtime environment, although this may be loaded
 * from several files.  So although this configuration is a common access point, it can be used to
 * access several configuration files.  Any application can call the 'loadXxxx'
 * methods and properties will be loaded from the given file/url/etc.
 * <p/>
 * This does mean that packages using the configuration file must make sure their
 * keys are unique. The most reliable way to do this is to prefix each key with
 * the package name (ie the code namespace) but this doesn't produce very nice
 * property files for humans to edit.
 * <p/>
 * @todo work out a nice way of ensuring keys are unique.
 * <p/>
 * Not entirely happy with the mixed static/instanceness of this - maybe ought
 * to break it into two but don't see it's necessary yet...
 *
 * @author M Hill
 */

public abstract class SimpleConfig
{
   /** Singleton Instance used to provide load methods */
   private static final PropertyConfig instance = new PropertyConfig();
   
   /**
    * Autoload looks for the properties file in jndi, then the environment variables,
    * then the classpath, then the local (working) directory
    */
   public static void autoLoad() throws IOException {
      instance.autoLoad();
   }

   /**
    * Static access to load from a file
    */
   public static void load(String filepath) throws IOException
   {
      instance.loadFile(filepath);
   }

   /**
    * Static access to load from a url
    */
   public static void load(URL url) throws IOException
   {
      instance.loadUrl(url);
   }
   
   /**
    * Static access to the instance method
    */
   public static String getLocations()
   {
      return instance.getLocations();
   }

   /**
    * Sets the given property - useful for tests.
    */
   public static void setProperty(String key, String value)
   {
      instance.setProperty(key, value);
   }

   /**
    * Returns the property value of the given key.
    */
   public static String getProperty(String key)
   {
      return instance.getProperty(key);
   }

   /**
    * Returns the property value of the given key, or the given default if
    * the key is not found or the properties file has not been created (eg
    * this has not been initialised)
    */
   public static String getProperty(String key, String defaultValue)
   {
      return instance.getProperty(key, defaultValue);
   }


}

