/*
 * $Id: FailbackConfig.java,v 1.3 2005/11/10 17:10:39 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.cfg;

import java.util.*;
import javax.naming.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;

/**
 * All Things To All Men Configuration.
 * <p>
 * A comprehensive facade singleton provding fallback access to Jndi and
 * standard text configuration files.
 * <p>
 * The fallback works like this:
 * <ul>
 * <li> Look in local cache (so calling 'setProperty' will override file properties)
 * <li> if that fails, Look in jndi for the property.
 * <li> If that fails, then look in the configuration file (see below for how this is located)
 * <li> If that fails, then look in the system environments.
 * <li> If that fails, throw an exception unless a default has been supplied.
 * </ul>
 * <p>
 * The configuration file locator works like this:
 * <ul>
 * <li> look in jndi for the key <code>"org.astrogrid.config.url"</code> which gives the url to the file
 * <li> if that fails, look in jndi for the key <code>"org.astrogrid.config.filename"</code> which gives the property filename
 * <li> if that fails, look in the system environment vars for the same key
 * <li> if that fails, look for the file <code>"astrogrid.properties"</code> on the classpath 
 * <li> if that fails, look for the file <code>"astrogrid.properties"</code> in the working directory
 * <li> if that fails, look for the file <code>"default.properties"</code> on the classpath 
 * </ul>
 * <p>
 * The configuration file lookup stops at the first find, so we don't get too confused
 * with lots of configuration files around, and properties being found in one
 * but not others.  If at any point it is referred to (ie JNDI key for the config filename exists) but
 * there is a problem loading the file, it fails.  This means you can be sure that
 * if you've <i>tried</i> to configure it, you will know if it hasn't worked.
 * <p>
 * Initialisation is 'lazy' - particularly as we may not want to go looking for
 * configuration files if everything is in Jndi. However given the dangers of
 * double-checked locking, the initialisation routines are synchronised and
 * checked <i>within</i> for the initialisation flag
 * <p>
 * Failures are all reported as exceptions, unless a default is given.  So
 * if you think a value might be missing and you don't want your app to fallover,
 * supply a default.
 * <p>
 * We could think about adding resource bundles instead of property files, but
 * I think JNDI provides all the complexity we need if we need that much...?
 * <p>
 * @author mch
 */


public class FailbackConfig extends ConfigReader {

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

   /** Usual filename for properties file in classpath, etc */
   private static String configFilename = "astrogrid.properties";
   
   /** filename for default properties file that come with the distribution */
   private static String defaultFilename = "default.properties";

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
               jndiContext.lookup("java:comp/env"); //this should be present in a Servlet Container
               
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
         
         String urlValue = null; //so we can use in reporting exception
         String filenameValue = null;

         //if jndi server is running, look up url in that
         if (jndiContext != null) {
            String jndiUrlKey = jndiPrefix+propertyUrlKey;
            String jndiFileKey = jndiPrefix+propertyKey;
            String keyUsed = jndiUrlKey;
            
            try {
               try {
                  urlValue = jndiContext.lookup(jndiUrlKey).toString().trim();//so we can report in exception
                  fileUrl = new URL(urlValue);
               } catch (NameNotFoundException nnfe) { } //ignore carry on
               log.debug("Config: JNDI key "+jndiUrlKey+" => "+fileUrl);
               try {
                  filenameValue = jndiContext.lookup(jndiFileKey).toString().trim();
               } catch (NameNotFoundException nnfe) { } //ignore carry on
               log.debug("Config: JNDI key "+jndiFileKey+" => "+filenameValue);
               
               //if they are both defined, throw an exception - sysadmin should only
               //define one, then we know where we are
               if ((fileUrl != null) && (filenameValue != null)) {
                  throw new ConfigException("Both "+jndiUrlKey+" and "+jndiFileKey+" defined in JNDI; specify only one");
               }

               //if filename given, locate
               if (filenameValue != null) {
                  File propertyFile = new File(filenameValue);
                  //if it's relative, locate from the classpath/working directory
                  if (!propertyFile.isAbsolute()) {
                     if (lookForConfigFile(filenameValue) == true)
                     {
                        //success!
                        return;
                     }
                     throw new FileNotFoundException(filenameValue);
                  }

                  //otherwise turn it into a url and carry on
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
               throw new ConfigException("Configuration file ("+filenameValue+") given in JNDI (key="+jndiFileKey+") cannot be found",fnfe);
            }
            catch (NamingException ne) {
               throw new ConfigException("Using key '"+jndiUrlKey+"' or '"+jndiFileKey+"' in JNDI gave: ", ne);
            }
            catch (IOException ioe) {
               throw new ConfigException(ioe+" loading property file at '"+fileUrl+
                                            "' (returned by JNDI key "+keyUsed+")", ioe);
            }
         }

         //look up url in system environment if nothing given in JNDI
         if ((filenameValue == null) && (urlValue == null)) {
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
         }

         //Nothing in JNDI, nothing in sys env, so look in class path for general properties file
         log.info("Config: No key to config file found in JNDI/SysEnv, so falling back to "+configFilename);

         try {
            if (lookForConfigFile(configFilename)) {
               return;
            }
         }
         catch (AccessControlException ace) {
            //this exception is thrown when a file is looked for, even if it doesn't exist,
            //if there is no FilePermission set for it.  This can be a pain for the fallback
            //config as it looks in many places for the configuration.  So we just log it - as an
            //error - but we don't crash
            log.error("No Permission to access "+configFilename, ace);
         }
            

         //last resort - look for default.properties that might be part of the distribution
         try {
            if (lookForConfigFile(defaultFilename)) {
               return;
            }
         }
         catch (AccessControlException ace) {
            //this exception is thrown when a file is looked for, even if it doesn't exist,
            //if there is no FilePermission set for it.  This can be a pain for the fallback
            //config as it looks in many places for the configuration.  So we just log it - as an
            //error - but we don't crash
            log.error("No Permission to access "+defaultFilename, ace);
         }
         
         
         //well we haven't found one anywhere - this may not be an error (as none may be desired) but
         //it should be reported...
         log.warn("No configuration file found; if you need one, "+
                     "make sure "+configFilename+" or "+defaultFilename+" is in your classpath, "+
                     "or set the JNDI key "+propertyUrlKey+" to its URL, "+
                     "or set the JNDI key "+propertyKey+" to its file location");
      }
   }

   /**
    * Looks for given config filename absolutely or in classpath and working directory, and loads
    * it if found.  Returns false if not found.
    * This could probably make use of Config.resolveFile()
    */
   private boolean lookForConfigFile(String givenFilename)  {

      //replace ${stuff} with sys.env values for stuff
      String filename = resolveEnvironmentVariables(givenFilename);
      
      //for debugging
      if (!filename.equals(givenFilename)) {
         givenFilename = givenFilename + " => "+filename;
      }

      log.debug("Looking for "+givenFilename);
     
         //if it's absolute, look absolutely
         File f = new File(filename);
         if (f.isAbsolute()) {
            if (f.exists()) {
               loadFromFile(f);
               return true;
            }
            return false;
         }
         
         //look for file in classpath.
         //see http://www.javaworld.com/javaworld/javaqa/2003-08/01-qa-0808-property.html
         //NB this works via URL as we don't expect to get config files from inside jars
         log.debug("Looking for "+givenFilename+" on classpath");
   //      URL configUrl = ClassLoader.getSystemResource(filename);
         URL configUrl = this.getClass().getClassLoader().getResource(filename);
         if (configUrl != null) {
            try {
               loadFromUrl(configUrl);
               
               log.info("Configuration file loaded from '"+configUrl+"' (found in classpath)");
               
               return true;
               
            } catch (IOException ioe) {
               throw new ConfigException(ioe+" loading property file at '"+configUrl+"' (from classpath)", ioe);
            }
         }
         
         
         //look for it in the working directory
         log.debug("Looking for "+givenFilename+" in working directory");
         if (f.exists()) {
            loadFromFile(f);
            return true;
         }
         
      return false;
   }
   
   /** Convenience routine - loads properties from given file */
   private void loadFromFile(File f) {
      try {
         loadFromUrl(f.toURL());
         log.info("Configuration file loaded from '"+f.getAbsoluteFile()+"'");
         return;
      }
      catch (IOException ioe) {
         throw new ConfigException(ioe+" loading property file at '"+f.getAbsoluteFile(), ioe);
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

      //we load them into a local variable properties instance first, so that
      //we can override the included config settings with these ones.
      Properties localProperties = new Properties();
      localProperties.load(url.openStream());

      addLoadedFrom(url.toString()); //add to string indicating what's happening
      
      //look for chain; if this file contains the property 'include.config.filename'
      //then load that into the global properties
      String includeFile = localProperties.getProperty("include.config.filename") ;
      if (includeFile != null) {
         boolean found = lookForConfigFile(includeFile);
         if (!found) {
            throw new ConfigException("include config file '"+includeFile+"' not found");
         }
      }
      
      //override any existing set propertis with the local ones loaded above
      properties.putAll(localProperties);
      
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
      if (value == null) {
         if (cache.containsKey(key)) {
            cache.remove(key);
         }
      } else {
         cache.put(key, value);
      }
   }

   /**
    * Returns array of values for the given key. Throws exception if property not found in Jndi,
    * then configuration file, then system environment.
    */
   public Object[] getProperties(String key)  {

      assertKeyValid(key);

      String lookedIn = "Cache";
      
      //first look in cache
      if (cache.containsKey(key)) {
         Object o = cache.get(key);
         if (o instanceof Object[]) {
            return (Object[]) o;
         }
         else {
            return new Object[] { o };
         }
      }
         
      //first look in jndi
      try {
         if (!jndiInitialised) { initialiseJndi(); }

         if (jndiContext != null) {
            lookedIn = lookedIn + ", JNDI";
            Context javacontext = (Context)jndiContext.lookup(jndiPrefix);
            NamingEnumeration en = jndiContext.list(jndiPrefix+key);
            Vector values = new Vector();
            while (en.hasMoreElements()) {
               NameClassPair pair = (NameClassPair) en.next();
               String value = null;
               //not sure how all this works, so for now will ignore naming exceptions
               try {
                  value = javacontext.lookup(pair.getName()).toString();
               } catch (NamingException ne) {
                  value="??Failed lookup: "+ne;
               } //ignore - value=??failed
               values.add(value);
            }
            return values.toArray();
         }
         else {
            lookedIn = lookedIn + ", (No JNDI)";
         }
      }
      catch (NameNotFoundException nnfe) { } //ignore, we'll look somewhere else
      catch (NamingException ne) {
         throw new ConfigException(ne+" locating key="+key+" in JNDI", ne);
      }

      //try the properties file. It can only hold one value per key, so we
      //look for the key (and return a single element array if found) and/or
      //key.1, key.2, key.3 until a key returns null.
      if (!fileInitialised) { initialiseFile(); }

      if (properties == null) {
         lookedIn = lookedIn +", (no config file)";
      }
      else {
         lookedIn = lookedIn +", config file(s) ("+loadedFrom()+")";

         String value = properties.getProperty(key);
         String value1 = properties.getProperty(key+".1");
      
         //check that there aren't both settings without number and settings with
         if ((value != null) && (value1 != null)) {
            throw new ConfigException("Both single value and sets of values defined for key "+key+" in property file");
         }

         //only one value set
         if (value != null) {
            return new Object[] { value };
         }
         
         if (value1 != null) {
            Vector values = new Vector();
            int v = 2;
            while (value1 != null) {
               values.add(value1);
               value1 = properties.getProperty(key+"."+v);
               v++;
            }
            return values.toArray();
         }
         
      }
      
      //try the system environment
      lookedIn = lookedIn +", sysenv";
      String value = System.getProperty(key);
      if (value != null) {
         return new Object[] { value };
      }
      
      throw new PropertyNotFoundException("Could not find '"+key+"' in: "+lookedIn);
   }

   /**
    * Set property to array.  Stores in cache so it overrides all other properties
    * with the same key.
    */
   public void setProperties(String key, Object[] values) {
      if (values == null) {
         if (cache.containsKey(key)) {
            cache.remove(key);
         }
      } else {
         cache.put(key, values);
      }
   }

   /**
    * Returns a list of keys.  This list is made up of the values in the
    * cache, JNDI, properties file and system environment keys; note that duplicate
    * keys will be hidden.
    */
   public Set keySet() {

      if (!jndiInitialised) { initialiseJndi(); }
      if (!fileInitialised) { initialiseFile(); }
      
      Set allKeys = new HashSet();
      
      //cache
      allKeys.addAll(cache.keySet());

      //jndi
      if (jndiContext != null) {
         try {
            Hashtable jndi = jndiContext.getEnvironment();
            allKeys.addAll(jndi.keySet());
         }
         catch (NamingException ne) {
            throw new ConfigException("Getting Environment from "+jndiContext,ne);
         }
      }
      
      //property files
      if (properties != null) {
         allKeys.addAll(properties.keySet());
      }

      //sys env
      allKeys.addAll(System.getProperties().keySet());

      return allKeys;
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
            Context javacontext = (Context)jndiContext.lookup(jndiPrefix);
            NamingEnumeration n = jndiContext.list(jndiPrefix);

            while (n.hasMoreElements()) {
               NameClassPair key = (NameClassPair)n.next();
               String value = "??Failed lookup";
               //not sure how all this works, so for now will ignore naming exceptions
               try {
                  value = javacontext.lookup(key.getName()).toString();
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
      try {
         Enumeration s = System.getProperties().keys();
         while (s.hasMoreElements()) {
            Object key = s.nextElement();
            out.println(formKeyValue(key, System.getProperty(key.toString())));
         }
      }
      catch (AccessControlException ace) {
         //might not be allowed blanket access to system enviornment variables...
         out.println("No blanket access permitted: "+ace);
      }

      out.flush();
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
Revision 1.3  2005/11/10 17:10:39  mch
Fixed javadoc

Revision 1.2  2005/03/28 01:24:42  mch
Added UserOptions

Revision 1.1  2005/03/21 14:05:47  mch
Initial checkin

Revision 1.32  2004/11/08 10:04:10  mch
Throw error if include file not found

Revision 1.31  2004/11/07 16:48:30  mch
Added include so we can maintain all the ivorn shortcuts

Revision 1.30  2004/11/07 16:44:25  mch
fix to multi-value getProperties for JNDI

Revision 1.29  2004/10/08 17:12:26  mch
Fix to getting sets of property when that property has been set to a set thpththh

Revision 1.28  2004/10/08 16:33:57  mch
Added setProperties() and attempt to make getProperties() work with property files

Revision 1.27  2004/10/05 19:32:43  mch
Added getProperties

Revision 1.26  2004/08/25 00:34:15  mch
Updated documentaiton

Revision 1.25  2004/08/04 12:11:59  mch
Fixed access control exception so it gets thrown when a config file is given

Revision 1.24  2004/08/03 11:13:29  mch
Added trap for AccessControlException

Revision 1.23  2004/07/16 16:03:04  mch
Added trim for config filename from JNDI, better error reporting and checking

Revision 1.22  2004/07/14 14:43:00  pah
get the jndi values to print out properly in the dump

Revision 1.21  2004/04/23 19:01:22  pah
changed the test for jndi presence slightly

Revision 1.20  2004/04/07 11:41:04  jdt
Modified by the A.A.A.A.

Revision 1.19  2004/03/31 11:00:14  mch
Added keySet()

Revision 1.18  2004/03/12 13:22:34  mch
Fix for null setProperty value

Revision 1.17  2004/03/09 16:34:51  mch
Added sysenv resolver & better error reporting

Revision 1.16  2004/03/09 16:32:27  mch
Added sysenv resolver

Revision 1.15  2004/03/06 22:22:08  mch
Added resolveFile

Revision 1.14  2004/03/06 14:58:17  mch
Added more failback config files, incl default.properties

Revision 1.13  2004/03/06 14:54:26  mch
Better file lookup

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









