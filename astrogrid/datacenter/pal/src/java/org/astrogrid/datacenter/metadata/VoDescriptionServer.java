/*
 * $Id: VoDescriptionServer.java,v 1.4 2004/10/12 23:09:53 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
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
   
   private static Document cache = null;
   
   public final static String VODESCRIPTION_ELEMENT =
               "<VODescription  xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "+
                               "xmlns:cea='http://www.ivoa.net/xml/CEAService/v0.1' "+
                               "xmlns:ceapd='http://www.astrogrid.org/schema/AGParameterDefinition/v1' "+
                               "xmlns:ceab='http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1' "+
                               "xmlns:vr='http://www.ivoa.net/xml/VOResource/v0.9' "+
                               "xmlns='http://www.ivoa.net/xml/VOResource/v0.9' "+  //default namespace
                    ">";
   public final static String VODESCRIPTION_ELEMENT_END ="</VODescription>";

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

      //get plugin list from config
      Object[] plugins = SimpleConfig.getSingleton().getProperties(VoResourcePlugin.RESOURCE_PLUGIN_KEY);
      
      //if they are not specified, assume one AuthorityConfigPlugin and a FileResourcePlugin
      if ((plugins == null) || (plugins.length==0)) {
         plugins = new String[] {
            AuthorityConfigPlugin.class.getName(),
            FileResourcePlugin.class.getName(),
         };
      }

      //start the vodescription document
      StringBuffer vod = new StringBuffer();
      vod.append(VODESCRIPTION_ELEMENT+"\n");

      //loop through plugins adding each ones list of resources
      for (int p = 0; p < plugins.length; p++) {
         //make plugin
         VoResourcePlugin plugin = createPlugin(plugins[p].toString());
         //get resources from plugin
         String[] voResources = plugin.getVoResources();
         //add to document
         for (int r = 0; r < voResources.length; r++) {
            vod.append(voResources[r]);
         }
      }

      //add the voresource for the CEA access to the datacenter
      try {
         //I've wrapped this in a separate try/catch so that problems with CEA
         //don't stop the initialiser from working.. which is naughty
         String ceaVoDescription = CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry();
         //extract the resource elements
         Document ceaDoc = DomHelper.newDocument(ceaVoDescription);
         NodeList ceaResources = ceaDoc.getElementsByTagName("Resource");
         if (ceaResources.getLength() ==0) {
            ceaResources = ceaDoc.getElementsByTagName("vr:Resource");
         }
            
         for (int i = 0; i < ceaResources.getLength(); i++) {
            vod.append(DomHelper.ElementToString((Element) ceaResources.item(i)));
         }
      } catch (Throwable th) {
        log.error(th);
      }

      //finish vod element
      vod.append(VODESCRIPTION_ELEMENT_END);

      return vod.toString();
   }

   /**
    * Returns the Authority resource element of the description */
   public static Element getAuthorityResource() throws IOException {
      return getResource("AuthorityType");
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
   
   
}






