/*
 * $Id: AttomConfig.java,v 1.3 2004/02/17 12:36:16 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.ServiceUnavailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * All Things To All Men Configuration file.
 *
 * A comprehensive facade singleton provding fallback access to Jndi and
 * standard text configuration files.
 *
 * The fallback works like this:
 * - Look in local cache (so setProperty can override others)
 * - Look in jndi for the property.
 * - If that fails, then look in the configuration file (see below for how this is located)
 * - If that fails, then look in the system environments.
 * - If that fails, throw an exception unless a default has been supplied.
 *
 * The configuration file locator works like this:
 * - look in jndi for the key "org.astrogrid.properties.url"
 * - if that fails, look in the system environment vars for the same key
 * - if that fails, look for the file "astrogrid.properties" on the classpath (not yet implemented)
 * - if that fials, look for the file "astrogrid.properties" in the working directory
 *
 * The configuration file lookup stops at the first find, so we don't get *too* confused
 * with lots of configuration files around, and properties being found in one
 * but not others.  If at any point it is referred to (ie JNDI name for it exists) but
 * there is a problem loading the file, it fails.
 *
 * Initialisation is 'lazy' - particularly as we may not want to go looking for
 * configuration files if everything is in Jndi. However given the dangers of
 * double-checked locking, the initialisation routines are synchronised and
 * checked *within* for the initialisation flag
 *
 * Failures are *all* reported as exceptions, unless a default is given.  So
 * if you think a value might be missing and you don't want your app to fallover,
 * supply a default
 *
 * We could think about adding resource bundles instead of property files, but
 * I think JNDI provides all the complexity we need if we need that much...?
 *
 * @author mch
 */


public class AttomConfig {

   /** Cache - only used for setProperty at the moment, so that Jndi can
    * reload on-the-fly as necessary */
   private static Hashtable cache = new Hashtable();
   
   /** The logging instance */
   protected static Log log = LogFactory.getLog(AttomConfig.class);
//   protected static Logger log = LogManager.getLogger(AttomConfig.class);

   /** initialised flags */
   private static boolean jndiInitialised = false;
   private static boolean fileInitialised = false;
   
   /** Jndi context */
   private static InitialContext jndiContext = null;
   
   /** Properties file context */
   private static Properties properties = null;
   
   /** key used to find the properties file */
   private static String propertyfileKey = "org.astrogrid.properties.url";
   
   private static String jndiPrefix = "java:comp/env/";

   /** Default filename for properties file in classpath, etc */
   private static String configFilename = "astrogrid.properties";
   

   /**
    * Call this before the first getProperty if you want to use a different
    * property file (eg for testing).  Throws an exception if properties
    * already loaded, as it's too late then
    */
   public static void setConfigFilename(String newName)
   {
      if (fileInitialised) {
         throw new ConfigException("Configuration already initialised - too late to set config filename",null);
      }
      configFilename = newName;
   }
      
   
   /**
    * Initialise Jndi access.  Note that the context may be null (if there is
    * no Jndi service) even after initialisation
    */
   private synchronized static void initialiseJndi() throws ConfigException {
      
      if (!jndiInitialised) {
         
         jndiInitialised = true;
         
         try {
            jndiContext = new InitialContext();
            
            try {
               jndiContext.lookup("Dummy");  //force lookup in case initialisation above is lazy
            }
            catch (NameNotFoundException nnfe) {} //ignore for this one
            
            log.info("Config access to JNDI initialised ("+jndiContext+")");
         }
         catch (ServiceUnavailableException sue) { jndiContext = null; } //not a problem
         catch (NoInitialContextException nice)  { jndiContext = null; } //not a problem
         catch (NamingException ne) {
            throw new ConfigException("Initialising Jndi Access", ne);
         }
      }
   }
   
   /**
    * Initialise access to properties file.  Looks first in jndi for url to file,
    * then system environment, then classpath, finally working directory.
    */
   private synchronized static void initialiseFile() throws ConfigException {
      
      if (!fileInitialised) {
         
         fileInitialised = true;

         URL fileUrl = null;
         
         //if jndi server is running, look up url in that
         if (jndiContext != null) {
            String jndiKey = jndiPrefix+propertyfileKey;
            
            try {
               fileUrl = new URL(jndiContext.lookup(jndiKey).toString());

               loadProperties(fileUrl.openStream());

               log.info("Configuration file loaded from '"+fileUrl.toString()+"' (from JNDI Key="+jndiKey+")");
               
               return;
            }
            catch (NameNotFoundException nnfe) { } //not a problem, continue on
            catch (MalformedURLException mue) {
               throw new ConfigException("Configuration file url given in JNDI (key="+jndiKey+") is malformed",mue);
            }
            catch (NamingException ne) {
               throw new ConfigException("Exception locating key="+jndiKey+" in JNDI", ne);
            }
            catch (IOException ioe) {
               throw new ConfigException("Exception loading property file at '"+fileUrl+"' (returned by JNDI key="+jndiKey+")", ioe);
            }
         }

         //look up url in system environment
         String sysEnvKey = propertyfileKey;
         String sysEnvUrl = System.getProperty(sysEnvKey);
         if (sysEnvUrl != null) {
            try {
               fileUrl = new URL(sysEnvUrl);

               loadProperties(fileUrl.openStream());
               
               log.debug("Configuration file loaded from '"+fileUrl.toString()+"' (from SYS ENV="+sysEnvKey+")");
               
               return;
            }
            catch (MalformedURLException mue) {
               throw new ConfigException("Configuration file url given in system environment variable '"+sysEnvKey+"' is malformed",mue);
            }
            catch (IOException ioe) {
               throw new ConfigException("Exception loading property file at '"+fileUrl+"' (returned by system environment variable '"+sysEnvKey+"')", ioe);
            }
         }
         
         //look for file in classpath.
         //see http://www.javaworld.com/javaworld/javaqa/2003-08/01-qa-0808-property.html
         log.info("Looking for '"+configFilename+"' on classpath");
         URL urlForInfo = ClassLoader.getSystemResource(configFilename);
         InputStream in = ClassLoader.getSystemResourceAsStream(configFilename);
         if (in != null) {
            try {
               loadProperties(in);
               
               log.info("Configuration file loaded from '"+urlForInfo+"' (from classpath)");
               
               return;
               
            } catch (IOException ioe) {
               throw new ConfigException("Exception loading property file at '"+urlForInfo+"' (from classpath)", ioe);
            }
         }
         
         
         //look for it in the working directory
         File f = new File(configFilename);
         if (f.exists()) {
            try {
               loadProperties(new BufferedInputStream(new FileInputStream(f)));
               log.info("Configuration file loaded from working directory '"+f.getAbsolutePath()+"'");
               return;
            }
            catch (IOException ioe) {
               throw new ConfigException("Exception loading property file at '"+f.getAbsolutePath()+"' (ie in working directory)", ioe);
            }
         }
         
         //well we haven't found one anywhere...
      }
   }
   
   /**
    * Loads the properties from the given stream.  While this should only be
    * called once during part of the normal initialisation process, we also
    * allow public access so that test harnesses etc can load their own
    * properties differently.
    */
   public static synchronized void loadProperties(InputStream in) throws IOException   {
      if (properties == null) {
         properties = new Properties();
      }
      properties.load(in);
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

   /**
    * General get property.  Throws exception if property not found in Jndi,
    * then configuration file, then system environment.
    */
   public static Object getProperty(String key)  {

      assertKeyValid(key);

      String lookedIn = "Cache";
      
      //first look in cache
      if (cache.containsKey(key)) {
         return cache.get(key);
      }
         
      //first look in jndi
      try {
         if (!jndiInitialised) initialiseJndi();
      
         if (jndiContext != null) {
            lookedIn = lookedIn + ", JNDI";
            return jndiContext.lookup(jndiPrefix+key);
         }
         else {
            lookedIn = lookedIn + ", (No JNDI)";
         }
      }
      catch (NameNotFoundException nnfe) { } //ignore, we'll look somewhere else
      catch (NamingException ne) {
         throw new ConfigException("Exception locating key="+key+" in JNDI", ne);
      }

      //try the properties file
      if (!fileInitialised) initialiseFile();

      if (properties == null) {
         lookedIn = lookedIn +", (no config file)";
      }
      else {
         String value = properties.getProperty(key);
         lookedIn = lookedIn +", configfile";
      
         if (value != null) {
            return value;
         }
      }
      
      //try the system environment
      lookedIn = lookedIn +", sysenv";
      String value = System.getProperty(key);
      if (value != null) {
         return value;
      }
      
      throw new PropertyNotFoundException("Could not find '"+key+"' in: "+lookedIn);
   }

   /**
    * Get Property with default - does not throw exception if property not found,
    * just returns the default
    */
   public static Object getProperty(String key, Object defaultValue) {
      
      try {
         return getProperty(key);
      }
      catch (PropertyNotFoundException nnfe) {
         return defaultValue;
      }
      
   }

   /**
    * Set property.  Stores in cache so it overrides all other properties
    * with the same key.
    */
   public static void setProperty(String key, Object value) {
      cache.put(key, value);
   }

   /**
    * Convenience string getProperty
    */
   public static String getString(String key) {
      return getProperty(key).toString();
   }
   
   /**
    * Convenience string getProperty
    */
   public static String getString(String key, String defaultValue) {
      return getProperty(key, defaultValue).toString();
   }

   /**
    * Typed getProperty - returns URL.  If property is not a valid url, throws
    * a wrapping ConfigException as a runtime error
    */
   public static URL getUrl(String key) {
      
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
   public static URL getUrl(String key, URL defaultValue) {
      
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
   public static int getInt(String key) {
      
      String value = getProperty(key).toString();
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
   public static int getInt(String key, int defaultValue) {
      
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
/*
$Log: AttomConfig.java,v $
Revision 1.3  2004/02/17 12:36:16  mch
Fixed self import

Revision 1.2  2004/02/17 03:54:35  mch
Nughtily large number of fixes for demo

 */



