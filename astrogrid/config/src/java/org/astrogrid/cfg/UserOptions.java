/*
 * $Id: UserOptions.java,v 1.1 2005/03/28 01:24:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.cfg;

import java.io.*;

import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

/**
 * A class that provides user options - Essentially a ConfigReader with 'save'
 * options.  At the moment this just wraps a property file with the same name
 * as the constructed context.  However there ought to be some better way of
 * of organising shared properties (eg look and feel) in a single config file.
 *
 */

public class UserOptions extends ConfigReader {

   Properties properties = new Properties();

   File file = null;
   
   String header = null;
   
   /** Construct, loading properties from file with the given filename.properties */
   public UserOptions(String filename) throws IOException {
      file = new File(filename+".options");
      try {
         properties.load(new FileInputStream(file));
      }
      catch (FileNotFoundException e) {
         //log but ignore
         log.warn(e+" loading user options from "+file);
      }
   }
   
   /** Returns the property identified by the given key.  If the property
    * is not found, throws a PropertyNotFoundException */
   public Object getProperty(String key) throws PropertyNotFoundException {
      Object value = properties.get(key);
      if (value == null) {
         throw new PropertyNotFoundException(key+" not found in "+file);
      }
      return value;
   }

   /** Set property. NB stores value as string, so make sure it can be represented as such!   */
   public void setProperty(String key, Object value) {
      properties.setProperty(key, value.toString());
   }
   
   /** Returns the list of properties identified by the given key.  If no matching properties
    * are found, throws a PropertyNotFoundException */
   public Object[] getProperties(String key) throws PropertyNotFoundException {
         String value = properties.getProperty(key);
         String value1 = properties.getProperty(key+".1");
      
         //check that there aren't both settings without number and settings with
         if ((value != null) && (value1 != null)) {
            throw new ConfigException("Both single value and sets of values defined for key "+key+" in property file "+file);
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
         
         throw new PropertyNotFoundException(key+" not found in "+file);
      }

   /** Sets the property to the given list of values */
   public void setProperties(String key, Object[] values) {
      for (int i = 0; i < values.length; i++) {
         setProperty(key+"."+i, values[i]);
      }
   }
   
   /** Loads the properties from the file at the given URL.  Not sure if this should
    * really be available for user options, but ho hum */
   public void loadFromUrl(URL url) throws IOException {
      properties.load(url.openStream());
   };

   /** Writes out the configuration keys and values to the given Writer. Used
    * for site debugging.  Remember that passwords should be hidden... */
   public void dumpConfig(Writer writer) {
      PrintWriter out = new PrintWriter(writer);
      Enumeration p = properties.keys();
      while (p.hasMoreElements()) {
         Object key = p.nextElement();
         out.println(formKeyValue(key, properties.getProperty(key.toString())));
      }
   }
   
   /** Returns a list of the keys */
   public Set keySet() {
      return properties.keySet();
   }
   
   /** Stores the options to the file they were loaded from */
   public void store() throws IOException {
      properties.store(new FileOutputStream(file), header);
   }
   
}


