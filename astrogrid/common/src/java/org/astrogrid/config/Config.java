/*
 * $Id: Config.java,v 1.10 2004/02/24 15:29:14 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Defines the methods that a Configurator must implement.
 * Also provides many of the convenience methods, such as getUrl().
 *
 * There is no store yet but we could add this later
 *
 * NB - Have deliberately NOT included a loadStream(Stream) method - although
 * this might be generically useful, it makes it difficult to track through
 * the Config package where properties have come from in a human fashio. If
 * you need to do this, write your stream to a file and then load from File.toURL()
 *
 */

public abstract class Config {

   /** The logging instance */
   protected Log log = LogFactory.getLog(Config.class);
//   protected static Logger log = LogManager.getLogger(Config.class);

   /** String used to identify locations loaded from */
   private String loadedFrom = null;

   /** Returns the property identified by the given key.  If the property
    * is not found, throws a PropertyNotFoundException */
   public abstract Object getProperty(String key) throws PropertyNotFoundException;

   /** Set property.  Stores in cache so it overrides all other properties
    * with the same key.   */
   public abstract void setProperty(String key, Object value);
   
   /** Loads the properties from the file at the given URL  */
   public abstract void loadUrl(URL url) throws IOException;

   
   /**
    * Adds the given string to the list of places the values are being found
    */
   protected void addLoadedFrom(String s) {
      if (loadedFrom == null) {
         loadedFrom = s;
      }
      loadedFrom = loadedFrom + ", "+s;
   }

   /**
    * Returns information about where the values are being found
    */
   public String loadedFrom() {
      if (loadedFrom == null) {
         return "(config not loaded yet)";
      }
      return loadedFrom;
   }
   

   /** Returns the property identified by the given key.  If the property
    * is not found, returns the given defaultValue */
   public Object getProperty(String key, Object defaultValue) {
      
      try {
         return getProperty(key);
      }
      catch (PropertyNotFoundException nnfe) {
         return defaultValue;
      }
      
   }

   /**
    * Convenience string of getProperty
    */
   public String getString(String key) {
      return getProperty(key).toString();
   }
   
   /**
    * Convenience string of getProperty
    */
   public String getString(String key, String defaultValue) {
      return getProperty(key, defaultValue).toString();
   }

   /**
    * Typed getProperty - returns URL.  If property is not a valid url, throws
    * a wrapping ConfigException as a runtime error
    */
   public URL getUrl(String key) {
      
      String value = getProperty(key).toString();
      try {
         return new URL(value);
      }
      catch (MalformedURLException mue) {
         throw new ConfigException("Key '"+key+"' returns invalid URL '"+value+"'", mue);
      }
   }
   
   /**
    * Typed getProperty - returns URL.  If property is not a valid url, throws
    * a wrapping ConfigException as a runtime error
    */
   public URL getUrl(String key, URL defaultValue) {
      
      String value = getProperty(key).toString();
      try {
         return new URL(value);
      }
      catch (MalformedURLException mue) {
         throw new ConfigException("Key '"+key+"' returns invalid URL '"+value+"'", mue);
      }
      catch (PropertyNotFoundException nfe) {
         return defaultValue;
      }
   }

   /**
    * Typed getProperty - returns integer.  If property is not a valid int, throws
    * a wrapping ConfigException as a runtime error
    */
   public int getInt(String key) {
      
      String value = getProperty(key).toString().trim();
      try {
         return Integer.parseInt(value);
      }
      catch (NumberFormatException nfe) {
         throw new ConfigException("Key '"+key+"' returns invalid integer '"+value+"'", nfe);
      }
   }
   
   /**
    * Typed getProperty - returns integer.  If property is not a valid int, throws
    * a wrapping ConfigException as a runtime error
    */
   public int getInt(String key, int defaultValue) {
      
      String value = getProperty(key).toString();
      try {
         return Integer.parseInt(value);
      }
      catch (NumberFormatException nfe) {
         throw new ConfigException("Key '"+key+"' returns invalid integer '"+value+"'", nfe);
      }
      catch (PropertyNotFoundException nfe) {
         return defaultValue;
      }
         
   }
   
   
}


