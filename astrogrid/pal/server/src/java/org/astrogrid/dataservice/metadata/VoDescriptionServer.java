/*
 * $Id: VoDescriptionServer.java,v 1.15 2006/08/21 15:39:30 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.dataservice.metadata.queryable.QueryableResourceReader;
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.dataservice.service.cea.CeaResources;
import org.astrogrid.dataservice.service.cone.ConeResources;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.astrogrid.slinger.ivo.IVORN;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;


/**
 * Assembles the various VoResource elements provided by the plugins, and
 * serves them all up wrapped in a VoDescription element for submitting to registries
 * @see VoResourceSupport for how resource elements are generated
 * @see package documentation
 * <p>
 * @author M Hill
 */

public class VoDescriptionServer {
   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   private static Document cache = null;
   
   public static final String QUERYABLE_PLUGIN = "datacenter.queryable.plugin";
   public final static String RESOURCE_PLUGIN_KEY = "datacenter.resource.plugin";

/*   
   public final static String VODESCRIPTION_ELEMENT =
               "<VOResources " +
                   "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                   "xmlns:vor='http://www.ivoa.net/xml/VOResource/v0.10' " +
                   "xmlns='http://www.ivoa.net/xml/VOResource/v0.10' " + //default namespace
                   ">";
 */
   public final static String VODESCRIPTION_ELEMENT =
         "<vor:VOResources xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xmlns:vor=\"http://www.ivoa.net/xml/RegistryInterface/v0.1\" xsi:schemaLocation=\"http://www.ivoa.net/xml/RegistryInterface/v0.1 http://www.ivoa.net/xml/RegistryInterface/v0.1\">\n";

   public final static String VODESCRIPTION_ELEMENT_END = "</vor:VOResources>";

   /**
    * Returns the whole metadata file as a DOM document
    */
   public synchronized static Document getVoDescription() throws IOException {
      if (cache == null) {
         try {
            cache = DomHelper.newDocument(makeVoDescription());
            
         }
         catch (SAXException e) {
            throw new MetadataException("XML error with Metadata: "+e,e);
         }
      }
      return cache;
   }
   
   /** Checks that the given document is a valid vodescription, throwing an
    * exception if not */
   public static void validateDescription(String vod) throws SAXException, MetadataException {
      Element root = null;
      try {
         root = DomHelper.newDocument(vod).getDocumentElement();
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
      
      // Added by KEA: validate the VODescription against schema
      String rootElement = root.getLocalName();
      if(rootElement == null) {
         rootElement = root.getNodeName();
      }
      try {
         AstrogridAssert.assertSchemaValid(root,rootElement,SchemaMap.ALL);
      }
      catch (Throwable th) {
         throw new MetadataException("Resource VODescription does not validate against its schema: "+th.getMessage(), th);
      }


      // Perform some manual checks (KEA: remove these?)
      NodeList children = root.getChildNodes();
      
      for (int i = 0; i < children.getLength(); i++) {
         if (children.item(i) instanceof Element) {
            Element resource = (Element) children.item(i);
            
            if (!resource.getLocalName().equals("Resource")) {
               throw new MetadataException("VODescription Child "+i+" ("+resource.getNodeName()+") is not a Resource element");
            }
            
            Element idNode = DomHelper.getSingleChildByTagName(resource, "identifier");
            if (idNode == null) {
               //no identifier - could add one but we don't know what resource key to give it
               throw new MetadataException("Resource "+i+" (xsi:type="+resource.getAttribute("xsi:type")+") has no <identifier>");
            }
            
            String configAuth = ConfigFactory.getCommonConfig().getString(VoResourceSupport.AUTHID_KEY);
            String rawId = DomHelper.getValueOf(idNode).trim();
            IVORN id = null;
            try {
               id = new IVORN(rawId);
            }
            catch (URISyntaxException e) {
               throw new MetadataException("<identifier> '"+rawId+"' is not a valid IVORN: "+e);
            }
            
            if (!id.getAuthority().startsWith(configAuth)) {
               throw new MetadataException("<identifier> '"+id+"' does not start with configured authority "+configAuth);
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
    * metadata to be refreshed from disk.  Not threadsafe...
    */
   public static void clearCache() {
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
         plugins = ConfigFactory.getCommonConfig().getProperties(RESOURCE_PLUGIN_KEY);
      } catch (PropertyNotFoundException pnfe)
      {
         log.warn("No config found for resource plugins, key="+RESOURCE_PLUGIN_KEY);
         
         //for backwards compatibility, look for old datacenter.metadata.plugin
         String s = ConfigFactory.getCommonConfig().getString("datacenter.metadata.plugin",null);
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
            
            checkAndAppendResource(vod, plugin);
            
            if (plugin instanceof CeaResources) { ceaDone = true; }
         }
      }

      //add the standard ones - cea, cone etc
      if (!ceaDone)
          {
          checkAndAppendResource(vod, new CeaResources());
          }

      // Only add conesearch if the configuration file says to do so.
      //
      String s = ConfigFactory.getCommonConfig().getString(
          "datacenter.implements.conesearch",null);
      if ( (s != null) && ( s.equals("true") || s.equals("TRUE") ) ) {
         checkAndAppendResource(vod, new ConeResources());
      }

//    addResources(vod, new SkyNodeResourceServer());
      
      //finish vod element
      vod.append(VODESCRIPTION_ELEMENT_END);

      return vod.toString();
   }

   public static void checkAndAppendResource(StringBuffer vod, VoResourcePlugin plugin) throws MetadataException, IOException {

      //get resources from plugin
      String resources = plugin.getVoResource();

      try {
         validateDescription(VODESCRIPTION_ELEMENT+resources+VODESCRIPTION_ELEMENT_END);
      
         vod.append(resources+"\n\n");
      }
      catch (SAXException e) {
  //       throw new MetadataException("Plugin "+plugin.getClass()+" generated invalid XML ",e);
    
         log.error("Plugin "+plugin.getClass()+" generated invalid XML ",e);
         vod.append(resources+"\n\n");
      }
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
      return new String[] { ConfigFactory.getCommonConfig().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY) };
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
    * for quick tests etc
    */
   public static void main(String[] args) throws RegistryException, IOException
   {
      SampleStarsPlugin.initConfig();
//    ConfigFactory.getCommonConfig().setProperty("datacenter.url","http://localhost:8080");
      VoDescriptionServer.pushToRegistry(new URL("http://galahad.star.le.ac.uk:8080/galahad-registry/services/AdminService"));
   }
}

