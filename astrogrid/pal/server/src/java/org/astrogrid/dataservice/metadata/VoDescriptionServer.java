/*
 * $Id: VoDescriptionServer.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.SimpleDateFormat;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.dataservice.metadata.queryable.ConeConfigQueryableResource;
import org.astrogrid.dataservice.metadata.queryable.QueryableResourceReader;
import org.astrogrid.dataservice.queriers.sql.RdbmsResourceGenerator;
import org.astrogrid.dataservice.queriers.sql.RdbmsResourceInterpreter;
import org.astrogrid.dataservice.queriers.test.SampleStarsPlugin;
import org.astrogrid.dataservice.service.cone.ConeResourceServer;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
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
   
   private static Document cache = null;
   
   public static final String AUTHID_KEY = "datacenter.authorityId";
   public static final String RESKEY_KEY = "datacenter.resourceKey";
   
   public static final String QUERYABLE_PLUGIN = "datacenter.queryable.plugin";
   
   public final static String VODESCRIPTION_ELEMENT =
               "<VODescription  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "+
                               "xmlns:cea='http://www.ivoa.net/xml/CEAService/v0.1' "+
                               "xmlns:ceapd='http://www.astrogrid.org/schema/AGParameterDefinition/v1' "+
                               "xmlns:ceab='http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1' "+
                               "xmlns:vr='http://www.ivoa.net/xml/VOResource/v0.9' "+
                               "xmlns='http://www.ivoa.net/xml/VOResource/v0.9' "+  //default namespace
                    ">";
   public final static String VODESCRIPTION_ELEMENT_END ="</VODescription>";

   /** used to format dates so that the registry can process them. eg 2005-11-04T15:34:22Z -
    * the date must be GMT */
   public final static SimpleDateFormat REGISTRY_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

   /**
    * Returns the whole metadata file as a DOM document
    */
   public synchronized static Document getVoDescription() throws IOException {
      if (cache == null) {
         try {
            cache = DomHelper.newDocument(makeVoDescription().toString());
            
            //check it's OK
            validate(cache);
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
   
   /** Checks that the given document is a valid vodescription, throwing an
    * exception if not */
   public static void validate(Document vod) throws MetadataException {
      Element root = vod.getDocumentElement();

      NodeList children = root.getChildNodes();
      
      for (int i = 0; i < children.getLength(); i++) {
         if (children.item(i) instanceof Element) {
            Element resource = (Element) children.item(i);
            
            if (!resource.getNodeName().equals("Resource")) {
               throw new MetadataException("VODescription Child "+i+" ("+resource.getNodeName()+") is not a Resource element");
            }
            
            Element id = DomHelper.getSingleChildByTagName(resource, "Identifier");
            if (id == null) {
               //no identifier - could add one but we don't know what resource key to give it
               throw new MetadataException("Resource "+i+" (xsi:type="+resource.getAttribute("xsi:type")+") has no Identifier");
            }
            
            DomHelper.setElementValue(DomHelper.ensuredGetSingleChild(id, "AuthorityID"), SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY));
            Element resKey = DomHelper.getSingleChildByTagName(id, "ResourceKey");
            if ((resKey == null) || (DomHelper.getValueOf(resKey).trim().length()==0)) {
               //no resource key
               throw new MetadataException("Identifier in Resource "+i+" (xsi:type="+resource.getAttribute("xsi:type")+") has no ResourceKey");
            }
         }
      }
      
   }

   /** Instantiates the class with the given name.  This is useful for things
    * such as 'plugins', where a class name might be given in a configuration file.
    * Rather messily throws Throwable because anything might have
    * gone wrong in the constructor.
    */
   public static VoResourcePlugin createVoResourcePlugin(String pluginClassName) {
      
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
         throw new RuntimeException("Bad metadata plugin specified ("+pluginClassName+") - does not implement VoResourcePlugin");
      }
      
      return (VoResourcePlugin) plugin;
      
   }
   
   /** Instantiates the class with the given name.  This is useful for things
    * such as 'plugins', where a class name might be given in a configuration file.
    * Rather messily throws Throwable because anything might have
    * gone wrong in the constructor.
    */
   public static QueryableResourceReader createQueryablePlugin(String pluginClassName) {
      
      Object plugin = null;
      
      try {
         log.debug("Creating Queryable Plugin '"+pluginClassName+"'");
         
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
         throw new RuntimeException("Bad metadata plugin specified ("+pluginClassName+") - does not implement VoResourcePlugin");
      }
      
      return (QueryableResourceReader) plugin;
      
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

      //get plugin list from config - need to add a default to the common method...
      Object[] plugins  = null;
      try {
         plugins = SimpleConfig.getSingleton().getProperties(VoResourcePlugin.RESOURCE_PLUGIN_KEY);
      } catch (PropertyNotFoundException pnfe)
      {
         log.warn("No config found for resource plugins, key="+VoResourcePlugin.RESOURCE_PLUGIN_KEY);
         
         //for backwards compatibility, look for old datacenter.metadata.plugin
         String s = SimpleConfig.getSingleton().getString("datacenter.metadata.plugin",null);
         if (s != null) {
            plugins = new String[] { s };
         }
      }

      //start the vodescription document
      StringBuffer vod = new StringBuffer();
      vod.append(VODESCRIPTION_ELEMENT+"\n");
      boolean ceaDone = false; //backwards compatiblity marker to see if CeaResource has been done
      
      //loop through plugins adding each one's list of resources
      if (plugins != null) {
         for (int p = 0; p < plugins.length; p++) {
            log.debug("Including Resource plugin "+plugins[p].toString());
            //make plugin
            VoResourcePlugin plugin = createVoResourcePlugin(plugins[p].toString());
            //get resources from plugin
            addResources(vod, plugin);
            
            if (plugin instanceof CeaResourceServer) { ceaDone = true; }
         }
      }

      //add the standard ones - cea, cone etc
      if (!ceaDone) { addResources(vod, new CeaResourceServer()); }
      addResources(vod, new ConeResourceServer());
//      addResources(vod, new SkyNodeResourceServer());
      
      //finish vod element
      vod.append(VODESCRIPTION_ELEMENT_END);

      return vod.toString();
   }

   /** Convenience routine for the above to load the resources from the given plugin
    * to the given StringBuffer */
   private static void addResources(StringBuffer b, VoResourcePlugin plugin) throws IOException {
      String[] voResources = plugin.getVoResources();
      //add to document
      for (int r = 0; r < voResources.length; r++) {
         b.append(voResources[r]);
      }
      return;
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
    * Sends the voDescription to the registry, returning list of Registries that
    * it was sent to
    */
   public static String[] pushToRegistry() throws IOException, RegistryException {
      RegistryAdminService service = RegistryDelegateFactory.createAdmin();
      service.update(getVoDescription());
      return new String[] { SimpleConfig.getSingleton().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY) };
   }

   /**
    * Sends the voDescription to the given registry URL, returning list of Registries that
    * it was sent to
    */
   public static void pushToRegistry(URL targetRegistry) throws IOException, RegistryException {
      RegistryAdminService service = RegistryDelegateFactory.createAdmin(targetRegistry);
      service.update(getVoDescription());
   }

   /**
    * Special case for common stuff - returns 'Queryable' implementation */
   public static QueryableResourceReader getQueryable() throws IOException {
      if (SimpleConfig.getProperty(QUERYABLE_PLUGIN, null) != null) {
         return createQueryablePlugin(SimpleConfig.getProperty(QUERYABLE_PLUGIN));
      }
      //if none given, see if we can use the standard RDBMS implementation
      if (getResource(RdbmsResourceGenerator.XSI_TYPE) != null) {
         return new RdbmsResourceInterpreter();
      }
      throw new UnsupportedOperationException("No 'Queryable' definition found on this service; "+QUERYABLE_PLUGIN+" not set in config and no RDBMS Resource found");
   }
   
   /**
    * for quick tests etc
    */
   public static void main(String[] args) throws RegistryException, IOException
   {
      SampleStarsPlugin.initConfig();
//    SimpleConfig.setProperty("datacenter.url","http://localhost:8080");
      VoDescriptionServer.pushToRegistry(new URL("http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/AdminService"));
   }
}








