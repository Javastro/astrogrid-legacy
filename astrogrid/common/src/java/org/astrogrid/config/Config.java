/*
 * $Id: Config.java,v 1.1 2003/10/07 16:42:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A superclass for configuration implementations that provides loading methods
 * <p>
 */

public abstract class Config
{
   /** For debug purposes - a list of locations that the configuration file
    * has been loaded from @see getLocations() */
   private static String locations = null;

   /** The logging instance */
   protected static Log log = LogFactory.getLog(SimpleConfig.class);
   
   /**
    * loads the properties from the url specified in Jndi by the given Jndi name
    */
   public void loadJndiUrl(String jndiName) {
      
      URL configUrl = null;
      
      try {
         configUrl = (URL) new InitialContext().lookup(jndiName);
         addLocation("JNDI ("+jndiName+"->"+configUrl+")");
         loadUrl(configUrl);
      }
      catch (NamingException e) {
         //ignore - just means it wasn't found
      }
      catch (IOException ioe) {
         log.error("Load failed from jndi '"+jndiName+"' -> '"+configUrl+"'",ioe);
      }
   }

   /**
    * loads the properties from the url specified in the system environment
    * variable of the given name
    */
   public void loadSysEnvUrl(String sysEnv) {
      
      URL configUrl = null;

      String sysVar = System.getProperty(sysEnv);
      if ((sysVar != null) && (sysVar.length() > 0)) {
         try
         {
            configUrl = new URL(sysVar);
            addLocation("System Environment ("+sysEnv+"->"+configUrl+")");
            loadUrl(configUrl);
         }
         catch (MalformedURLException e) {
            //rethrow as a fatal runtime exception
            throw new RuntimeException("System Variable '"+sysEnv+"' returned '"+sysVar+"', not a valid URL");
         }
         catch (IOException ioe) {
            log.error("Load failed from system environment variable '"+sysEnv+"' -> '"+sysVar+"' -> '"+configUrl+"'",ioe);
         }
      }
   }
      
 
   /**
    * Loads the file at the given filepath as a properties file
    */
   public void loadFile(String filepath) throws IOException
   {
      addLocation(filepath);

      loadStream(new FileInputStream(filepath));
   }

   /**
    * Loads the properties from the given url
    */
   public void loadUrl(URL url) throws IOException
   {
      addLocation(url.toString());

      loadStream(url.openStream());
   }

   /**
    * Loads the properties at the given inputstream.
    */
   protected abstract void loadStream(InputStream in) throws IOException;

   /**
    * Returns the location of the configuration file - useful when reporting
    * errors that things can't be found or are incorrect, so the user really
    * knows they're looking in the right place
    */
   public String getLocations()
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
   private void addLocation(String newLocation)
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

}

