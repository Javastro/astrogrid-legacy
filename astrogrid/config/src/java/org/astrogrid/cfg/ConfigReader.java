/*
 * $Id: ConfigReader.java,v 1.3 2005/03/28 01:24:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.cfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.astrogrid.xml.DomHelper;

/**
 * Defines the methods that a class that provides properties must implement.
 * <p>
 * NB - Have deliberately NOT included a loadStream(Stream) method - although
 * this might be generically useful, it makes it difficult to track through
 * the Config package where properties have come from. If
 * you need to do this, write your stream of properties to a file and then load
 * from File.toURL()
 *
 */

public abstract class ConfigReader {

   /** The logging instance */
   protected Log log = LogFactory.getLog(ConfigReader.class);
//   protected static Logger log = LogManager.getLogger(Config.class);

   /** String used to identify locations loaded from */
   private String loadedFrom = null;

   /** Returns the property identified by the given key.  If the property
    * is not found, throws a PropertyNotFoundException */
   public abstract Object getProperty(String key) throws PropertyNotFoundException;

   /** Set property.    */
   public abstract void setProperty(String key, Object value);
   
   /** Returns the list of properties identified by the given key.  If no matching properties
    * are found, throws a PropertyNotFoundException */
   public abstract Object[] getProperties(String key) throws PropertyNotFoundException;

   /** Sets the property to the given list of values */
   public abstract void setProperties(String key, Object[] values);
   
   /** Loads the properties from the file at the given URL  */
   public abstract void loadFromUrl(URL url) throws IOException;

   /** Writes out the configuration keys and values to the given Writer. Used
    * for site debugging.  Remember that passwords should be hidden... */
   public abstract void dumpConfig(Writer out);
   
   /** Returns a list of the keys */
   public abstract Set keySet();
   
   /**
    * Adds the given string to the list of places the values are being found
    */
   protected void addLoadedFrom(String s) {
      if (loadedFrom == null) {
         loadedFrom = s;
      } else {
         loadedFrom = loadedFrom + ", "+s;
      }
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
    * Convenience routine for dumpConfig. Formats a key/value pair for printing.
    * Does noddy
    * check for 'password' in the key string and hides value if present
    */
   protected String formKeyValue(Object key, Object value) {
      if (key.toString().toLowerCase().indexOf("password") > -1) {
         return "  "+key+" = <hidden>";
      } else {
         return "  "+key+" = "+value;
      }
   }
   

   /**
    * Convenience string of getProperty
    */
   public String getString(String key, String defaultValue) {
      Object o = getProperty(key, defaultValue);  //doing toString() can cause problems if null is the default...
      if (o == null) {
         return null;
      } else {
         return o.toString();
      }
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
    * Typed getProperty - returns boolean.
    */
   public boolean getBoolean(String key, boolean defaultValue) {
      
      try {
         return getBoolean(key);
      }
      catch (PropertyNotFoundException nfe) {
         return defaultValue;
      }
   }
   
   /**
    * Typed getProperty - returns boolean.
    */
   public boolean getBoolean(String key) {
      return Boolean.valueOf(getProperty(key).toString()).booleanValue();
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
         return DomHelper.newDocument(source.openStream());
      }
      catch (IOException ioe) {
         throw new ConfigException("Could not read from '"+source+"' given by Config Key '"+key+"'" , ioe);
      }
      catch (SAXException se) {
         throw new ConfigException("Invalid XML in '"+source+"' from Config Key '"+key+"'");
      }
   }

   /** There are several occasions when an application needs a complete file.  For
    * example, a metadata file.  This method provides a way of locating that file
    * in different environments.
    * If the filename includes a ${..} then the contents of the
    * brackets are resolved using the system properties.  This allows us to make
    * use of Tomcat's locations for example.
    * If the filename is then absolute, the file is resolved normally.
    * If the filename is relative, the file is searched for first in the classpath
    * then in the working directory.
    * If the path is not found, a FileNotFoundException is thrown listing
    * the places looked.  If the path is found, a url to it is returned.
    *
    * Hmm not sure if this is really a Configuration thing....
    *
    * NB this resolves to a URL so it won't find files in jar files
    */
   public static URL resolveFilename(String givenFilename) throws IOException {
      
      String filename = givenFilename; //so we preserve the original

      //resolve included environment variables
      filename = resolveEnvironmentVariables(filename);

      //for error messages
      String msg = givenFilename;
      if (!givenFilename.equals(filename)) {
         msg = msg + " (resolves to "+filename+")";
      }
      
      File f = new File(filename);
      if (f.isAbsolute()) {
         if (f.exists() && f.isFile()) {
            return f.toURL();
         }
         throw new FileNotFoundException(msg);
      }
      else
      {
         //not absolute so look in classpath
         URL url = ConfigReader.class.getClassLoader().getResource(filename);
   
         if (url != null) {
            return url;
         }
         else {
            //not found so look in working directory
            if (f.exists() && f.isFile()) {
               return f.toURL();
            }
         }
         throw new FileNotFoundException(msg+" not found in classpath or working directory");
      }
   }


   /**
    * Resolves environment variables.  Looks for ${xxxx} strings and replace
    * with whatever xxxx is set to in the system environment properties
    */
   public static String resolveEnvironmentVariables(String givenSource) {
      
      String source = givenSource; //preserve given for error messages
      
      while (source.indexOf("${")>-1) {
         int s = source.indexOf("${");
         int e = source.indexOf("}");
         if (e==-1) throw new IllegalArgumentException("String "+givenSource+" has mismatched brackets");
         
         String sysEnvKey = source.substring(s+2,e);
         String sysEnvValue = System.getProperty(sysEnvKey);
         
         if (sysEnvValue == null) throw new ConfigException("Sys Env '"+sysEnvKey+"' not found for filename "+givenSource);
         
         source = source.substring(0, s)+ sysEnvValue + source.substring(e+1);
      }
      
      return source;
   }

   
   /** Returns the class specified by the given key */
   public  Class getClass(String key) {
      String className = getString(key);
      
      try {
         return Class.forName(className);
      }
      catch (ClassNotFoundException e) {
         throw new ConfigException("Class "+className+" cannot be found, given by Key "+key+" in config loaded from "+loadedFrom());
      }
   }

   public Class getClass(String key, Class defaultClass) {
      try {
         return getClass(key);
      }
      catch (PropertyNotFoundException nfe) {
         return defaultClass;
      }
      
   }
   
   /** Returns an instance of the class specified by the given key - useful for plugins */
   public Object getInstance(String key) throws Throwable {
      Class c = getClass(key);
      try {
         return instantiate(c);
      }
      catch (InvocationTargetException ite) {
         throw new ConfigException("Could not create class "+c+" given by key "+key, ite.getCause());
      }
   }
   
   /** Instantiates the class with the given name.  This is useful for things
    * such as 'plugins', where a class name might be given in a configuration file.
    * Rather messily throws Throwable because anything might have
    * gone wrong in the constructor.  Here in the Config because it gets used here
    * so might as well.
    */
   public static Object instantiate(Class classToMake) throws InvocationTargetException, InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, NoSuchMethodException  {
      
      /* NWW - interesting bug here.
       original code used class.newInstance(); this method doesn't declare it throws InvocationTargetException,
       however, this exception _is_ thrown if an exception is thrown by the constructor (as is often the case at the moment)
       worse, as InvocatioinTargetException is a checked exception, the compiler rejects code with a catch clause for
       invocationTargetExcetpion - as it thinks it cannot be thrown.
       this means the exception boils out of the code, and is unstoppable - dodgy
       work-around - use the equivalent methods on java.lang.reflect.Constructor - which do throw the correct exceptions */

      Constructor constr = classToMake.getConstructor(new Class[] { });
      return constr.newInstance(new Object[] { } );
      
   }
   
}


