/*
 * $Id: FailbackConfig.java,v 1.2 2004/02/26 17:31:30 mch Exp $
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


public class FailbackConfig extends Config {

   /** Cache - only used for setProperty at the moment, so that Jndi can
    * reload on-the-fly as necessary */
   private Hashtable cache = new Hashtable();

   /** initialised flags */
   private boolean jndiInitialised = false;
   private boolean fileInitialised = false;
   
   /** Jndi context */
   private InitialContext jndiContext = null;
   
   /** Properties file context */
   private Properties properties = null;
   
   /** key used to find the properties file */
   private static String propertyfileKey = "org.astrogrid.config.url";
   
   /** prefix to keys when accessing JNDI services.  No idea why this is required... */
   private static String jndiPrefix = "java:comp/env/";

   /** Default filename for properties file in classpath, etc */
   private static String configFilename = "astrogrid.properties";
   
   /**
    * Protected constructor because we shouldn't be able to make one of these.
    * At the moment the given context is ignored
    */
   protected FailbackConfig(Object context) {
   }
   
   /**
    * Call this before the first getProperty if you want to use a different
    * property file (eg for testing).  Throws an exception if properties
    * already loaded, as it's too late then
    */
   public void setConfigFilename(String newName)
   {
      if (fileInitialised) {
         throw new ConfigException("Configuration already initialised - too late to set config filename",null);
      }
      configFilename = newName;
   }
      
   
   /**
    * Initialise Jndi access.  Note that the context may be null (if there is
    * no Jndi service) even after initialisation.  Synchronized for thread safety
    */
   private synchronized void initialiseJndi()  {
      
      if (!jndiInitialised) {
         
         jndiInitialised = true;
         
         try {
            jndiContext = new InitialContext();
            
            try {
               jndiContext.lookup("Dummy");  //force lookup in case initialisation above is lazy
               
               addLoadedFrom("JNDI");
            }
            catch (NameNotFoundException nnfe) { /*ignore if we can't find 'Dummy' */ }
            
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
   private synchronized void initialiseFile()  {
      
      if (!fileInitialised) {
         
         fileInitialised = true;

         URL fileUrl = null;
         
         //if jndi server is running, look up url in that
         if (jndiContext != null) {
            String jndiKey = jndiPrefix+propertyfileKey;
            
            try {
               fileUrl = new URL(jndiContext.lookup(jndiKey).toString());

               loadUrl(fileUrl);

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
               throw new ConfigException("Exception loading property file at '"+fileUrl+
                                            "' (returned by JNDI key="+jndiKey+")", ioe);
            }
         }

         //look up url in system environment
         String sysEnvKey = propertyfileKey;
         String sysEnvUrl = System.getProperty(sysEnvKey);
         if (sysEnvUrl != null) {
            try {
               fileUrl = new URL(sysEnvUrl);

               loadUrl(fileUrl);
               
               log.debug("Configuration file loaded from '"+fileUrl.toString()+"' (from SYS ENV="+sysEnvKey+")");
               
               return;
            }
            catch (MalformedURLException mue) {
               throw new ConfigException("Configuration file url given in system environment variable '"+
                                            sysEnvKey+"' is malformed",mue);
            }
            catch (IOException ioe) {
               throw new ConfigException("Exception loading property file at '"+fileUrl+
                                            "' (returned by system environment variable '"+sysEnvKey+"')", ioe);
            }
         }
         
         //look for file in classpath.
         //see http://www.javaworld.com/javaworld/javaqa/2003-08/01-qa-0808-property.html
         log.info("Looking for '"+configFilename+"' on classpath");
         URL configUrl = ClassLoader.getSystemResource(configFilename);
         if (configUrl != null) {
            try {
               loadUrl(configUrl);
               
               log.info("Configuration file loaded from '"+configUrl+"' (from classpath)");
               
               return;
               
            } catch (IOException ioe) {
               throw new ConfigException("Exception loading property file at '"+configUrl+"' (from classpath)", ioe);
            }
         }
         
         
         //look for it in the working directory
         File f = new File(configFilename);
         if (f.exists()) {
            try {
               loadUrl(f.toURL());
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
   public synchronized void loadUrl(URL url) throws IOException   {
      if (properties == null) {
         properties = new Properties();
      }
      properties.load(url.openStream());

      addLoadedFrom(url.toString());
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
   public Object getProperty(String key)  {

      assertKeyValid(key);

      String lookedIn = "Cache";
      
      //first look in cache
      if (cache.containsKey(key)) {
         return cache.get(key);
      }
         
      //first look in jndi
      try {
         if (!jndiInitialised) { initialiseJndi(); }
      
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
      if (!fileInitialised) { initialiseFile(); }

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
    * Set property.  Stores in cache so it overrides all other properties
    * with the same key.
    */
   public void setProperty(String key, Object value) {
      //note that we cannot store in the 'Properties' instance as that can
      //only handle strings
      cache.put(key, value);
   }

}
/*
$Log: FailbackConfig.java,v $
Revision 1.2  2004/02/26 17:31:30  mch
Fixed org.astrogrid.config.url from org.astrogrid.properties.url

Revision 1.1  2004/02/24 15:35:12  mch
Refactoring to include FailbackConfig

Revision 1.5  2004/02/17 14:47:15  mch
Increased test coverage

Revision 1.4  2004/02/17 14:31:49  mch
Minor changes to please checkstyle

Revision 1.3  2004/02/17 12:36:16  mch
Fixed self import

Revision 1.2  2004/02/17 03:54:35  mch
Nughtily large number of fixes for demo

 */



