/*
 * $Id: Config.java,v 1.13 2004/03/01 14:07:01 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
   public abstract void loadFromUrl(URL url) throws IOException;

   
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
      
      try {
         return new URL(getProperty(key).toString());
      }
      catch (MalformedURLException mue) {
         throw new ConfigException("Key '"+key+"' returns invalid URL '"+getProperty(key)+"'", mue);
      }
   }
   
   /**
    * Typed getProperty - returns URL.  If property is not a valid url, throws
    * a wrapping ConfigException as a runtime error
    */
   public URL getUrl(String key, URL defaultValue) {
      
      try {
         return getUrl(key);
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
      
      try {
         return Integer.parseInt(getProperty(key).toString().trim());
      }
      catch (NumberFormatException nfe) {
         throw new ConfigException("Key '"+key+"' returns invalid integer '"+getProperty(key)+"'", nfe);
      }
   }
   
   /**
    * Typed getProperty - returns integer.  If property is not a valid int, throws
    * a wrapping ConfigException as a runtime error
    */
   public int getInt(String key, int defaultValue) {
      
      try {
         return getInt(key);
      }
      catch (PropertyNotFoundException nfe) {
         return defaultValue;
      }
   }
   
   /**
    * Indirect typed getProperty - returns a DOM loaded from a file at the url
    * speficied.  ie, looks up the given key to get a url, then attempts to
    * read a DOM from the file at that URL.
    * @todo - This is a temporary holding point - Kevin will implement
    */
   public Document getDom(String key) {
      return readDom(key, getUrl(key));
   }
   
   /**
    * Indirect typed getProperty - returns a DOM loaded from a file at the url
    * speficied.  ie, looks up the given key to get a url, then attempts to
    * read a DOM from the file at that URL.
    */
   public Document getDom(String key, Document defaultDom) {
      try {
         return getDom(key);
      }
      catch (PropertyNotFoundException nfe) {
         return defaultDom;
      }
      
   }

   /**
    * Indirect typed getProperty - returns a DOM loaded from a file at the url
    * speficied.  ie, looks up the given key to get a url, then attempts to
    * read a DOM from the file at that URL.
    */
   public Document getDom(String key, URL defaultUrl) {
      try {
         return getDom(key);
      }
      catch (PropertyNotFoundException nfe) {
         return readDom("(default, key '"+key+"' not found)", defaultUrl);
      }
      
   }

   /** For DOM loading methods above.  The key is given so that expcetions
    * can report which key was being used */
   private Document readDom(String key, URL source) {
      try {
         return XMLUtils.newDocument(source.openStream());
      }
      catch (IOException ioe) {
         throw new ConfigException("Could not read from '"+source+"' given by Config Key '"+key+"'" , ioe);
      }
      catch (ParserConfigurationException pce) {
         throw new ConfigException("Application not configured correctly: ", pce);
      }
      catch (SAXException se) {
         throw new ConfigException("Invalid XML in '"+source+"' from Config Key '"+key+"'");
      }
   }
   
}


