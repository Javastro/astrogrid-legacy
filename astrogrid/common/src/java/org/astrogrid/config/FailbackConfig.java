/*
 * $Id: FailbackConfig.java,v 1.12 2004/03/05 12:32:29 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.ServiceUnavailableException;

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
 * - look in jndi for the key "org.astrogrid.config.url" which gives the url to the file
 * - look in jndi for the key "org.astrogrid.config" which gives the property filename
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
   
   /** JNDI key to url that locates the properties file */
   private static String propertyUrlKey = "org.astrogrid.config.url";
   
   /** JNDI key to properties file  */
   private static String propertyKey = "org.astrogrid.config.filename";

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
         catch (ServiceUnavailableException sue) {
            //not a problem, but note
            log.debug("No JNDI service found ("+sue+") so will not use JNDI for config...");
            jndiContext = null;
         }
         catch (NoInitialContextException nice)  {
            //not a problem
            log.debug("No JNDI service found ("+nice+") so will not use JNDI for config...");
            jndiContext = null;
         }
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
            String jndiUrlKey = jndiPrefix+propertyUrlKey;
            String jndiFileKey = jndiPrefix+propertyKey;
            String keyUsed = jndiUrlKey;
            String urlValue = null; //so we can use in reporting exception
            String filename = null;
            
            try {
               try {
                  urlValue = jndiContext.lookup(jndiUrlKey).toString();//so we can report in exception
                  fileUrl = new URL(urlValue);
               } catch (NameNotFoundException nnfe) { } //ignore carry on
               log.debug("Config: JNDI key "+jndiUrlKey+" => "+fileUrl);
               try {
                  filename = jndiContext.lookup(jndiFileKey).toString();
               } catch (NameNotFoundException nnfe) { } //ignore carry on
               log.debug("Config: JNDI key "+jndiFileKey+" => "+filename);
               
               //if they are both defined, throw an exception - sysadmin should only
               //define one, then we know where we are
               if ((fileUrl != null) && (filename != null)) {
                  throw new ConfigException("Both "+jndiUrlKey+" and "+jndiFileKey+" defined in JNDI; specify only one");
               }

               //if filename given, locate
               if (filename != null) {
                  File propertyFile = new File(filename);
                  fileUrl = propertyFile.toURL();
                  keyUsed = jndiFileKey;
               }

               if (fileUrl != null) {
                  loadFromUrl(fileUrl);

                  log.info("Configuration file loaded from '"+fileUrl.toString()+"' (from JNDI Key="+keyUsed+")");
                  
                  return;
               }
            }
            catch (MalformedURLException mue) {
               throw new ConfigException("Configuration file url ("+urlValue+") given in JNDI (key="+jndiUrlKey+") is malformed",mue);
            }
            catch (FileNotFoundException fnfe) {
               throw new ConfigException("Configuration file ("+filename+") given in JNDI (key="+jndiFileKey+") cannot be found",fnfe);
            }
            catch (NamingException ne) {
               throw new ConfigException("Using key '"+jndiUrlKey+"' or '"+jndiFileKey+"' in JNDI gave: ", ne);
            }
            catch (IOException ioe) {
               throw new ConfigException(ioe+" loading property file at '"+fileUrl+
                                            "' (returned by JNDI key "+keyUsed+")", ioe);
            }
         }

         //look up url in system environment
         String sysEnvKey = propertyUrlKey;
         String sysEnvUrl = System.getProperty(sysEnvKey);
         log.debug("Config: Sys Env key "+sysEnvKey+" => "+sysEnvUrl);
         if (sysEnvUrl != null) {
            try {
               fileUrl = new URL(sysEnvUrl);

               loadFromUrl(fileUrl);
               
               log.debug("Configuration file loaded from '"+fileUrl.toString()+"' (from SYS ENV="+sysEnvKey+")");
               
               return;
            }
            catch (MalformedURLException mue) {
               throw new ConfigException("Configuration file url given in system environment variable '"+
                                            sysEnvKey+"' is malformed",mue);
            }
            catch (IOException ioe) {
               throw new ConfigException(ioe+" loading property file at '"+fileUrl+
                                            "' (returned by system environment variable '"+sysEnvKey+"')", ioe);
            }
         }
         
         //look for file in classpath.
         //see http://www.javaworld.com/javaworld/javaqa/2003-08/01-qa-0808-property.html
         log.debug("Looking for '"+configFilename+"' on classpath");
         URL configUrl = ClassLoader.getSystemResource(configFilename);
         if (configUrl != null) {
            try {
               loadFromUrl(configUrl);
               
               log.info("Configuration file loaded from '"+configUrl+"' (found in classpath)");
               
               return;
               
            } catch (IOException ioe) {
               throw new ConfigException(ioe+" loading property file at '"+configUrl+"' (from classpath)", ioe);
            }
         }
         
         
         //look for it in the working directory
         log.debug("Looking for '"+configFilename+"' in working directory");
         File f = new File(configFilename);
         if (f.exists()) {
            try {
               loadFromUrl(f.toURL());
               log.info("Configuration file loaded from working directory '"+f.getAbsolutePath()+"'");
               return;
            }
            catch (IOException ioe) {
               throw new ConfigException(ioe+" loading property file at '"+f.getAbsolutePath()+"' (ie in working directory)", ioe);
            }
         }
         
         //well we haven't found one anywhere - this may not be an error (as none may be desired) but
         //it should be reported...
         log.warn("No configuration file found; if you need one, "+
                     "make sure "+configFilename+" is in your classpath, "+
                     "or set the JNDI key "+propertyUrlKey+" to it's URL, "+
                     "or set the JNDI key "+propertyKey+" to it's file location");
      }
   }
   
   /**
    * Loads the properties from the given stream.  While this should only be
    * called once during part of the normal initialisation process, we also
    * allow public access so that test harnesses etc can load their own
    * properties differently.
    */
   public synchronized void loadFromUrl(URL url) throws IOException   {
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
         throw new ConfigException(ne+" locating key="+key+" in JNDI", ne);
      }

      //try the properties file
      if (!fileInitialised) { initialiseFile(); }

      if (properties == null) {
         lookedIn = lookedIn +", (no config file)";
      }
      else {
         String value = properties.getProperty(key);
         lookedIn = lookedIn +", config file(s) ("+loadedFrom()+")";
      
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

   /**
    * Dumps config contents
    */
   public void dumpConfig(Writer writer)  {
      
      PrintWriter out = new PrintWriter(writer);
      
      out.println("Configuration loaded from: "+loadedFrom());
      out.println();

      //-- cache --
      if (cache.isEmpty()) {
         out.println("(Cache is empty)");
      }
      else {
         out.println("Cache:");
         Enumeration c = cache.keys();
         while (c.hasMoreElements()) {
            Object key = c.nextElement();
            out.println(formKeyValue(key, cache.get(key)));
         }
      }
      
      //-- JNDI --
      out.println();
      if (jndiContext != null) {
         out.println("JNDI:");
         try {
            out.println("JNDI Environment:");
            Hashtable env = jndiContext.getEnvironment();
            Enumeration j = env.keys();
            while (j.hasMoreElements()) {
               Object key = j.nextElement();
               out.println(formKeyValue(key, env.get(key)));
            }
            out.println("JNDI Names:");
            Enumeration n = jndiContext.list(jndiPrefix);

            while (n.hasMoreElements()) {
               Object key = n.nextElement();
               String value = "??Failed lookup";
               //not sure how all this works, so for now will ignore naming exceptions
               try {
                  value = jndiContext.lookup(key.toString()).toString();
               } catch (NamingException ne) { } //ignore - print fail value
               out.println(formKeyValue(key, value));
            }
            
         }
         catch (NamingException ne) {
            ne.printStackTrace(out);
         }

      }
      else {
         out.println("(No JNDI)");
      }
      out.println();
      
      //--- Property Files ---
      if (properties != null) {
         out.println("Properties from file(s):");
         Enumeration p = properties.keys();
         while (p.hasMoreElements()) {
            Object key = p.nextElement();
            out.println(formKeyValue(key, properties.getProperty(key.toString())));
         }
      }
      else {
         out.println("(No Config File)");
      }
      out.println();

      //-- System environment variables ----
      out.println("System Environment Variables:");
      Enumeration s = System.getProperties().keys();
      while (s.hasMoreElements()) {
         Object key = s.nextElement();
         out.println(formKeyValue(key, System.getProperty(key.toString())));
      }

      out.flush();
   }
   
   /**
    * Formats a key/value pair for printing.  Used by dumpConfig.  Does noddy
    * check for 'password' in the key string and hides value if present
    */
   public String formKeyValue(Object key, Object value) {
      if (key.toString().toLowerCase().indexOf("password") > -1) {
         return "  "+key+" = <hidden>";
      } else {
         return "  "+key+" = "+value;
      }
   }
   
   /**
    * Main method dumps config contents to console - useful for debugging
    */
   public static void main(String[] args) {
      FailbackConfig config = new FailbackConfig(null);

      //force lazy load
      config.getProperty("Nuffink", null);

      config.dumpConfig(new PrintWriter(System.out));
   }
}
/*
$Log: FailbackConfig.java,v $
Revision 1.12  2004/03/05 12:32:29  mch
Hides values for keys that include the word 'password'

Revision 1.11  2004/03/05 00:33:02  mch
Added exception name to errors

Revision 1.10  2004/03/04 20:25:02  mch
Added dumpConfig to Config, and changed argument to general Writer

Revision 1.9  2004/03/03 17:29:00  mch
Added debug statements

Revision 1.8  2004/03/03 17:11:07  mch
Added command line dump and tested

Revision 1.7  2004/03/03 16:56:46  mch
minor comment change

Revision 1.6  2004/03/03 16:51:10  mch
Added dumpConfig

Revision 1.5  2004/03/03 16:21:59  mch
Added better reporting if no config file found

Revision 1.4  2004/03/01 14:06:39  mch
Added filename failback and better error reporting

Revision 1.3  2004/02/27 14:23:12  mch
Changed loadUrl to loadFromUrl

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






