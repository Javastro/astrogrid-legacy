/*
 * $Id: PropertyConfig.java,v 1.2 2003/10/07 22:21:27 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A proeprty-based implementation of the configuration file
 *
 * @author M Hill
 */

public class PropertyConfig extends Config
{
   /** Constant used to search JNDI for file */
   public static final String JNDI = "org.astrogrid.config.propertyurl";

   /** Constant used to search system environment for properties file */
   public static final String SYS_ENV = "AG_CONFIG";

   /** Default filename (look in working directory) */
   public static final String DEFAULT_FILENAME = "AstroGridNode.cfg";
   
   /** The set of key/value property pairs, using the java Properties class */
   private Properties properties = new Properties();

   /**
    * Private constructor so that we keep it a singleton
    */
   protected PropertyConfig()
   {
   }
   
   /**
    * Autoload looks for the properties file in jndi, then the environment variables,
    * then the classpath, then the local (working) directory
    */
   public void autoLoad() {

      try {
         loadJndiUrl(JNDI);

         loadSysEnvUrl(SYS_ENV);
         
         loadFile(DEFAULT_FILENAME);
      }
      catch (FileNotFoundException fnfe) {
         //not a problem if the default file is not found
         log.debug(fnfe);
      }
      catch (IOException ioe) {
         log.warn(ioe);
      }
   }

   /**
    * Loads the properties at the given inputstream.  Called by the various
    * superclasses loads
    */
   protected void loadStream(InputStream in) throws IOException
   {
      properties.load(in);
   }

   /**
    * Sets the given property - useful for tests.
    */
   public void setProperty(String key, String value)
   {
      assertKeyValid(key);

      properties.setProperty(key, value);
   }

   /**
    * Returns the property value of the given key.
    */
   public String getProperty(String key)
   {
      assertKeyValid(key);

      return properties.getProperty(key);
   }

   /**
    * Returns the property value of the given key, or the given default if
    * the key is not found or the properties file has not been created (eg
    * this has not been initialised)
    */
   public String getProperty(String key, String defaultValue)
   {
      assertKeyValid(key);

      if (properties == null)
      {
         return defaultValue;
      }

      return properties.getProperty(key, defaultValue);
   }


   /**
    * Keys in the current JDK Property implementation must not contain whitespace,
    * colons or equals
    */
   public static void assertKeyValid(String key)
   {
      assert key.indexOf(":") == -1 : "Key '"+key+"' contains an illegal character - a colon";
      assert key.indexOf(" ") == -1 : "Key '"+key+"' contains an illegal character - a space";
      assert key.indexOf("=") == -1 : "Key '"+key+"' contains an illegal character - an equals sign";
   }

}

