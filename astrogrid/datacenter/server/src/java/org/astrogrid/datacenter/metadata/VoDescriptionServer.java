/*
 * $Id: VoDescriptionServer.java,v 1.5 2004/09/07 11:20:49 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;
import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Serves the service's VoDescrption.
 * <p>
 * This file includes a VODescrption element, but at the moment I am assuming that
 * the VODescription might be wrapped in other by combining the various Resources returned
 * by the configured MetataPlugins into a VODescription.
 *
 * <p>See package documentation.
 * <p>
 * @author M Hill
 */

public class VoDescriptionServer {
   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   public final static String PLUGIN_KEY = "datacenter.metadata.plugin";
   
   private static Document cache = null;
   
   /**
    * Returns the whole metadata file as a DOM document
    */
   public synchronized static Document getVoDescription() throws IOException {
      if (cache == null) {
         try {
            cache = DomHelper.newDocument(makeVoDescription().toString());
         }
         catch (ParserConfigurationException e) {
            throw new RuntimeException("Server not setup properly: "+e,e);
         }
         catch (SAXException e) {
            throw new MetadataException("XML error with Metadata: "+e,e);
         }
      }
      return cache;
   }
   
   /** Instantiates the class with the given name.  This is useful for things
    * such as 'plugins', where a class name might be given in a configuration file.
    * Rather messily throws Throwable because anything might have
    * gone wrong in the constructor.
    */
   public static VoResourcePlugin createPlugin(String pluginClassName) {
      
      Object plugin = null;
      
      try {
         log.debug("Creating VoResourcePlugin '"+pluginClassName+"'");
         
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
      
      if (!(plugin instanceof VoResourcePlugin)) {
         throw new RuntimeException("Bad metadata plugin specified ("+pluginClassName+") - does not implement MetadataPlugin");
      }
      
      return (VoResourcePlugin) plugin;
      
   }
   
   /**
    * Clears the cache - useful to call before doing a set of operations, forces
    * metadata to be refreshed from disk
    */
   public synchronized static void clearCache() {
      cache = null;
   }
   
   /**
    * Make a VODescription document out of all the voResourcePlugins, returning an
    * unvalidated string.  This means we can view the made (finsihed) docuemnt
    * separate from the validating process. */
   public static String makeVoDescription() throws IOException, MetadataException {

      StringBuffer vod = new StringBuffer();
      vod.append("<VODescription  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>\n");
      
      //lookup authority plugin
      String pluginClassName = SimpleConfig.getSingleton().getString("datacenter.authority.metadata.plugin",null);
      if (pluginClassName != null) {
         VoResourcePlugin authorityPlugin = createPlugin(pluginClassName);
         vod.append(authorityPlugin.getVoResource());
      }
      
      //lookup secondary/main plugin - default to FileResourcePlugin
      pluginClassName = SimpleConfig.getSingleton().getString("datacenter.metadata.plugin", FileResourcePlugin.class.getName());
      VoResourcePlugin plugin = createPlugin(pluginClassName);
      vod.append(plugin.getVoResource());

      //this gives metadata about the CEA access to the datacenter
      try {
         //I've wrapped this in a separate try/catch so that problems with CEA
         //don't stop the initialiser from working.. which is naughty
         String ceaResource = CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry();
         //we convert to Document and back again to Element so that we remove any processing instructions, etc
         Document ceaDoc = DomHelper.newDocument(ceaResource);
         vod.append(DomHelper.ElementToString(ceaDoc.getDocumentElement()));
      } catch (Throwable th) {
        log.error(th);
      }

      vod.append("</VODescription>");

      return vod.toString();
   }

   /**
    * Returns the Authority resource element of the description */
   public static Element getAuthorityResource() throws IOException {
      return getResource("AuthorityID");
   }
   
   /**
    * Returns the resource element of the given type eg 'AuthorityID'.
    * Matches the given string against the attribute 'xsi:type' of the elements
    * named 'Resource'
    */
   public static Element getResource(String type) throws IOException {
      NodeList resources = getVoDescription().getElementsByTagName("Resource");
      
      for (int i = 0; i < resources.getLength(); i++) {
         Element resource = (Element) resources.item(i);
         if (resource.getAttribute("xsi:type").equals(type)) {
            return resource;
         }
      }
      return null; //not found
   }

   /**
    * Sends the voDescription to the registry
    */
   public void pushToRegistry() throws IOException, RegistryException {
      RegistryAdminService service = RegistryDelegateFactory.createAdmin();
      service.update(getVoDescription());
   }
   
   
   /** Returns the element representing the given table name in the served
    * metadata *
   public static Element getTableElement(String tableName) throws IOException {
      return getTableElement(tableName, getMetadata().getDocumentElement());
   }
   
   /** Returns the element for the given table/column in the served metadata
   public static Element getColumnElement(String tableName, String columnName) throws IOException {
      return getColumnElement(tableName, columnName, getMetadata().getDocumentElement());
   }
   
   /** Returns a list of the tables of the served metadata
   public static String[] getTables() throws IOException {
      return getTables(getMetadata().getDocumentElement());
   }
   
   /** Returns a list of the columns for the given table of the served metadata
   public static String[] getColumns(String tableName) throws IOException {
      return getColumns(tableName, getMetadata().getDocumentElement());
   }
   
   /** Returns a list of the tables of the served metadata
   public static String[] getTables(Element metatables) throws IOException {
      NodeList tableNodes = metatables.getElementsByTagName("Table");
      String[] tables = new String[tableNodes.getLength()];
      for (int t=0;t<tableNodes.getLength();t++) {
         tables[t] = ((Element) tableNodes.item(t)).getAttribute("name");
      }
      return tables;
   }
   
   /** Returns a list of the columns for the given table of the served metadata
   public static String[] getColumns(String tableName, Element metatables) throws IOException {
      
      NodeList colNodes = getTableElement(tableName, metatables).getElementsByTagName("Column");
      
      String[] columns = new String[colNodes.getLength()];
      for (int t=0;t<columns.length;t++) {
         columns[t] = ((Element) colNodes.item(t)).getAttribute("name");
      }
      return columns;
   }
   
   /** Returns the element representing the given table name in the given
    * metadata
   public static Element getTableElement(String tableName, Element metatables) throws IOException {
      NodeList tables = metatables.getElementsByTagName("Table");
      
      for (int t=0;t<tables.getLength();t++) {
         if (((Element) tables.item(t)).getAttribute("name").equals(tableName)) {
            return (Element) tables.item(t);
         }
      }
      
      throw new IllegalArgumentException("No such table '"+tableName+"' in metadata");
   }
   
   /** Returns the element for the given table/column in the served metadata
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
    /**/
   
}





