/*
 * $Id: PropertyConfig.java,v 1.3 2003/10/07 22:41:10 mch Exp $
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
   /** The set of key/value property pairs, using the java Properties class */
   private Properties properties = new Properties();

   /**
    * Protected constructor so that we keep the creations under control
    */
   protected PropertyConfig()
   {
   }
   
   /** Returns the default configuration filename  */
   protected String getDefaultFilename() { return "AstroGrid.cfg"; }
   
   /** Returns the jndi key used to find the url of the configuration file */
   protected String getJndiKey()         { return "org.astrogrid.config.url"; }
   
   /** Returns the System Environment variable used to find the url of the configuration file */
   protected String getSysEnvKey()       { return "AG_CONFIG"; }
   

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


