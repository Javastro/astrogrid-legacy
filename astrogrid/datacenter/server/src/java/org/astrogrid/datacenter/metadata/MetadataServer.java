/*
 * $Id: MetadataServer.java,v 1.15 2004/08/30 17:30:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;
import org.astrogrid.datacenter.fits.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Serves the service's metadata file.  See package documentation.
 * <p>
 * @author M Hill
 */

public class MetadataServer {
   protected static Log log = LogFactory.getLog(MetadataServer.class);
   
   public final static String PLUGIN_KEY = "datacenter.metadata.plugin";
   
   private static Document cache = null;
   
   /**
    * Returns the whole metadata file as a DOM document
    */
   public synchronized static Document getMetadata() throws IOException {
      if (cache == null) {
         cache = createPlugin().getMetadata();
      }
      return cache;
   }
   
   /**
    * Returns the metadata; if it can't find the normal stuff, it will return
    * whatever can be generated
    */
   public synchronized static Document getOrGenerateMetadata() throws IOException {
      try {
         return getMetadata();
      }
      catch (FileNotFoundException fnfe) {
         return MetadataInitialiser.generateMetadata();
      }
   }
   
   /** Instantiates the class with the given name.  This is useful for things
    * such as 'plugins', where a class name might be given in a configuration file.
    * Rather messily throws Throwable because anything might have
    * gone wrong in the constructor.
    */
   public static MetadataPlugin createPlugin() {
      
      String pluginClassName = SimpleConfig.getSingleton().getString("datacenter.metadata.plugin",org.astrogrid.datacenter.metadata.FileServer.class.getName());
      Object plugin = null;
      
      try {
         log.debug("Creating MetadataPlugin '"+pluginClassName+"'");
         
         Class qClass = Class.forName(pluginClassName);
         
         /* NWW - interesting bug here.
          original code used class.newInstance(); this method doesn't declare it throws InvocationTargetException,
          however, this exception _is_ thrown if an exception is thrown by the constructor (as is often the case at the moment)
          worse, as InvocatioinTargetException is a checked exception, the compiler rejects code with a catch clause for
          invocationTargetExcetpion - as it thinks it cannot be thrown.
          this means the exception boils out of the code, and is unstoppable - dodgy
          work-around - use the equivalent methods on java.lang.reflect.Constructor - which do throw the correct exceptions */
         
         Constructor constr = qClass.getConstructor(new Class[] { });
         plugin = constr.newInstance(new Object[] { } );
         
      }
      catch (ClassNotFoundException cnfe) {
         throw new RuntimeException("Could not find metadata plugin class "+pluginClassName);
      }
      catch (NoSuchMethodException nsme) {
         throw new RuntimeException("Bad metadata plugin specified ("+pluginClassName+") - has no zero-argument constructor");
      }
      catch (Throwable th) {
         throw new RuntimeException("Bad metadata plugin specified ("+pluginClassName+")",th);
      }
      
      if (!(plugin instanceof MetadataPlugin)) {
         throw new RuntimeException("Bad metadata plugin specified ("+pluginClassName+") - does not implement MetadataPlugin");
      }
      
      return (MetadataPlugin) plugin;
      
   }
   
   /**
    * Clears the cache - useful to call before doing a set of operations, forces
    * metadata to be refreshed from disk
    */
   public synchronized static void clearCache() {
      cache = null;
   }
   
   /**
    * Returns the VODescription element of the metadata
    * If there is more than one, logs an error but does not fail.
    */
   public static Element getVODescription() throws IOException {
      NodeList nodes = getMetadata().getElementsByTagName("VODescription");
      
      if (nodes.getLength()>1) {
         log.error("Server not configured properly: Too many VODescription nodes in metadata - place all VOResource elements in one VODescription");
      }
      
      if (nodes.getLength()==0) {
         throw new DatacenterException("Server not configured completely; no VODescription element in its metadata");
      }
      
      return (Element) nodes.item(0);
   }
   
   
   
   /** Returns the element representing the given table name in the served
    * metadata */
   public static Element getTableElement(String tableName) throws IOException {
      return getTableElement(tableName, getMetadata().getDocumentElement());
   }
   
   /** Returns the element for the given table/column in the served metadata */
   public static Element getColumnElement(String tableName, String columnName) throws IOException {
      return getColumnElement(tableName, columnName, getMetadata().getDocumentElement());
   }
   
   /** Returns a list of the tables of the served metadata */
   public static String[] getTables() throws IOException {
      return getTables(getMetadata().getDocumentElement());
   }
   
   /** Returns a list of the columns for the given table of the served metadata */
   public static String[] getColumns(String tableName) throws IOException {
      return getColumns(tableName, getMetadata().getDocumentElement());
   }
   
   /** Returns a list of the tables of the served metadata */
   public static String[] getTables(Element metatables) throws IOException {
      NodeList tableNodes = metatables.getElementsByTagName("Table");
      String[] tables = new String[tableNodes.getLength()];
      for (int t=0;t<tableNodes.getLength();t++) {
         tables[t] = ((Element) tableNodes.item(t)).getAttribute("name");
      }
      return tables;
   }
   
   /** Returns a list of the columns for the given table of the served metadata */
   public static String[] getColumns(String tableName, Element metatables) throws IOException {
      
      NodeList colNodes = getTableElement(tableName, metatables).getElementsByTagName("Column");
      
      String[] columns = new String[colNodes.getLength()];
      for (int t=0;t<columns.length;t++) {
         columns[t] = ((Element) colNodes.item(t)).getAttribute("name");
      }
      return columns;
   }
   
   /** Returns the element representing the given table name in the given
    * metadata */
   public static Element getTableElement(String tableName, Element metatables) throws IOException {
      NodeList tables = metatables.getElementsByTagName("Table");
      
      for (int t=0;t<tables.getLength();t++) {
         if (((Element) tables.item(t)).getAttribute("name").equals(tableName)) {
            return (Element) tables.item(t);
         }
      }
      
      throw new IllegalArgumentException("No such table '"+tableName+"' in metadata");
   }
   
   /** Returns the element for the given table/column in the served metadata */
   public static Element getColumnElement(String tableName, String columnName, Element metatables) throws IOException {
      NodeList tables = metatables.getElementsByTagName("Table");
      
      Element tableElement = null;
      for (int t=0;t<tables.getLength();t++) {
         if (((Element) tables.item(t)).getAttribute("name").equals(tableName)) {
            tableElement = (Element) tables.item(t);
         }
      }
      
      if (tableElement == null) {
         throw new IllegalArgumentException("No such table '"+tableName+"' in metadata");
      }
      
      NodeList columns = tableElement.getElementsByTagName("Column");
      
      Element colElement = null;
      for (int c=0;c<columns.getLength();c++) {
         if (((Element) columns.item(c)).getAttribute("name").equals(columnName)) {
            colElement = (Element) columns.item(c);
         }
      }
      
      if (colElement == null) {
         throw new IllegalArgumentException("No such column '"+columnName+"' in metadata for table '"+tableName+"'");
      }
      
      return colElement;
   }
   
   
}



