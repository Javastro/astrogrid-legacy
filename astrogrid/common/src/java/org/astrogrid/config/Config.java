/*
 * $Id: Config.java,v 1.7 2003/12/16 13:13:12 mch Exp $
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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A superclass for configuration implementations that provides loading methods
 * <p>
 */

public abstract class Config {
   /** For debug purposes - a list of locations that the configuration file
    * has been loaded from @see getLocations() */
   private static String locations = null;
   
   /** The logging instance */
   protected static Log log = LogFactory.getLog(SimpleConfig.class);
   
   /**
    * Indirect file lookup; pass in the fully pathed jndi key (eg java:comp/env/wibble/config.url)
    * and this routine will find the filename given by that key, and load the properties from there
    *
    * @return true if load succeeded
    */
   public boolean loadJndiUrl(String jndiKey) throws IOException {
      
      URL configUrl = null;
      Object jndiValue = null;
      
      try {
         jndiValue = new InitialContext().lookup(jndiKey);
         
         if (jndiValue == null) {
            return false;
         }
         
         if (jndiValue instanceof URL) {
            configUrl = (URL) jndiValue;
         } else if (jndiValue instanceof String) {
            configUrl = new URL((String) jndiValue);
         } else {
            throw new IOException("Unknown JndiValue type:" + jndiValue.getClass().getName());
         }
         
         addLocation("JNDI ("+jndiKey+"->"+configUrl+")");
         loadUrl(configUrl);
         return true;
      }
      catch (NamingException e) {
         //ignore - just means it wasn't found
         log.debug(e);
         addLocation("(No JNDI key "+jndiKey+")");
         return false;
      }
      catch (IOException ioe) {
         //rethrow with more info
         IOException nioe = new IOException(ioe+": Load failed from jndi '"+jndiKey+"' -> '"+jndiValue+"' -> '"+configUrl+"'");
         nioe.setStackTrace(ioe.getStackTrace());
         throw nioe;
      }
      
   }
   
   /**
    * loads the properties from the url specified in the system environment
    * variable of the given name
    * @return true if load succeeded
    */
   public boolean loadSysEnvUrl(String sysEnv) throws IOException {
      
      URL configUrl = null;
      
      String sysVar = System.getProperty(sysEnv);
      if ((sysVar != null) && (sysVar.length() > 0)) {
         try {
            configUrl = new URL(sysVar);
            addLocation("System Environment ("+sysEnv+"->"+configUrl+")");
            loadUrl(configUrl);
            return true;
         }
         catch (MalformedURLException e) {
            //rethrow as a fatal runtime exception - it was specified but wrong
            throw new RuntimeException("System Variable '"+sysEnv+"' returned '"+sysVar+"', not a valid URL");
         }
         catch (IOException ioe) {
            //rethrow with more info
            IOException nioe = new IOException(ioe+": Load failed from system environment variable '"+sysEnv+"' -> '"+sysVar+"' -> '"+configUrl+"'");
            nioe.setStackTrace(ioe.getStackTrace());
            throw nioe;
         }
      }
      return false;
   }
   
   /**
    * Loads the file at the given filepath as a properties file
    * @return true if load succeeded
    */
   public boolean loadFile(String filepath) throws IOException {

      try {
         loadStream(new FileInputStream(filepath));
         
         addLocation(filepath);
         
         return true;
      } catch (IOException ioe) {
         addLocation("(Failed "+filepath+")");
         throw ioe;
      }
   }
   
   
   /**
    * Loads the properties from the given url
    */
   public void loadUrl(URL url) throws IOException {
      try {
         loadStream(url.openStream());

         addLocation(url.toString());
      
      } catch (IOException ioe) {
         addLocation("(Failed "+url+")");
         throw ioe;
      }

   }
   
   /**
    * Autoload looks for the properties file in jndi, then the environment variables,
    * then the classpath, then the local (working) directory.
    * @return true if load succeeded
    */
   public void autoLoad() throws IOException {
      
      boolean loaded = loadJndiUrl(getJndiKey());
      
      if (!loaded) {
         loaded = loadSysEnvUrl(getSysEnvKey());
      }
      
      if (!loaded) {
         try {
            loaded = loadFile(getDefaultFilename());
         }
         catch (FileNotFoundException fnfe) {
            //not a problem if the default file is not found
            log.debug(fnfe);
         }
      }
   }

   /**
    * Loads the properties at the given inputstream.
    */
   protected abstract void loadStream(InputStream in) throws IOException;
   
   /** Subclasses should implement this to return the jndi key used to find the url of the configuration file */
   protected abstract String getJndiKey();
   
   /** Subclasses should implement this to return the System Environment variable used to find the url of the configuration file */
   protected abstract String getSysEnvKey();
   
   /** Subclasses should implement this Returns the default configuration filename  */
   protected abstract String getDefaultFilename();

   /**
    * Returns the location of the configuration file - useful when reporting
    * errors that things can't be found or are incorrect, so the user really
    * knows they're looking in the right place
    */
   public String getLocations() {
      if (locations == null) {
         return "(no file loaded)";
      }
      else {
         return locations;
      }
   }
   
   /**
    * convenient way of adding locations to the string
    */
   private void addLocation(String newLocation) {
      if (locations == null) {
         locations = newLocation;
      }
      else {
         locations = locations+", "+newLocation;
      }
   }
   
   
   
}


