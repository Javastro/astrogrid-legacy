/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
import java.io.IOException;
import java.util.Vector;
import java.lang.reflect.Constructor;
import java.net.URISyntaxException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.queryable.QueryableResourceReader;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.admin.RegistryAdminService;
//import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.astrogrid.slinger.ivo.IVORN;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;

/**
 * Utility functions for versioned VoDescriptionGenerator classes.
 *
 * @see VoResourceSupport for how resource elements are generated
 * @see package documentation
 * <p>
 * @author M Hill, K Andrews
 * @deprecated  Used with old-style push registration. Not relevant now
 * that we are using new IVOA registration conventions, and NO LONGER
 * SUPPORTED.
 *
 */

public class VoDescriptionGeneratorBase {

   protected static Log log = LogFactory.getLog(VoDescriptionGeneratorBase.class);
   
   /** Checks that the given document is a valid vodescription, throwing an
    * exception if not */
   public void validateDescription(String vod) throws SAXException, MetadataException {
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
            
            String configAuth = ConfigFactory.getCommonConfig().getString(VoResourceSupportBase.AUTHID_KEY);
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
   public VoResourcePlugin createVoResourcePlugin(String pluginClassName) {
      
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
   public QueryableResourceReader createQueryablePlugin(String pluginClassName) {
      
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
    * Make a VODescription document out of all the voResourcePlugins, returning an
    * unvalidated string.  This means we can view the made (finsihed) docuemnt
    * separate from the validating process. This needs to be instantiated
    * by version-specific descendent classes. */
   public String makeVoDescription() throws IOException, MetadataException {
      // Throw an exception because we can't have an abstract static method,
      // which is what we'd really like here
      throw new MetadataException("Programming Error: Attempt to instantiate VoDescriptionGeneratorBase instead of a versioned VoDescriptionGenerator.");
   }
   public Document getVoDescription() throws IOException, MetadataException {
      // Throw an exception because we can't have an abstract static method,
      // which is what we'd really like here
      throw new MetadataException("Programming Error: Attempt to instantiate VoDescriptionGeneratorBase instead of a versioned VoDescriptionGenerator.");
   }

   /**
    */
   public void checkAndAppendResource(StringBuffer vod, VoResourcePlugin plugin, String vodescElementStart, String vodescElementEnd) throws MetadataException, IOException {

      //get resources from plugin
      String resources = plugin.getVoResource();
      if ((resources != null) && !("".equals(resources)) ){
         // Don't try validating empty resources (which might be returned by
         // e.g. conesearch plugin if conesearch is enabled but no tables
         // are configured as conesearchable)
         try {
            validateDescription(vodescElementStart+resources+vodescElementEnd);
         
            vod.append(resources+"\n");
         }
         catch (SAXException e) {
     //       throw new MetadataException("Plugin "+plugin.getClass()+" generated invalid XML ",e);
       
            log.error("Plugin "+plugin.getClass()+" generated invalid XML ",e);
            vod.append(resources+"\n");
         }
      }
   }


   /* Used with v1.0 resource generation */
   public void checkAndAppendSubelements(StringBuffer vod, String subelements, String vodescElementStart, String vodescElementEnd, String pluginName) throws MetadataException, IOException {

      if ((subelements != null) && !("".equals(subelements)) ){
         try {
            validateDescription(
                  vodescElementStart + subelements + vodescElementEnd);
            vod.append(subelements);
         }
         catch (SAXException e) {
            log.error("Resource generator "+pluginName+" generated invalid XML ",e);
            throw new MetadataException("Plugin "+pluginName+" generated invalid XML ",e);
       
            //vod.append(subelements);
         }
      }
   }

   
   /**
    * Returns the resource element of the given type eg 'AuthorityID'.
    * Matches the given string against the attribute 'xsi:type' of the elements
    * named 'Resource'
    */
   public Element getResource(String type) throws IOException {
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
    * Sends the voDescription to the given registry URL.
    */
   public void pushToRegistry(URL targetRegistry, String versionParam) throws IOException, RegistryException {
      RegistryAdminService service = RegistryDelegateFactory.createAdmin(
          targetRegistry, versionParam);
      service.update(getVoDescription());
   }


   /**
    * for quick tests etc
    */
   /*
   public void main(String[] args) throws RegistryException, IOException
   {
      SampleStarsPlugin.initConfig();
//    ConfigFactory.getCommonConfig().setProperty("datacenter.url","http://localhost:8080");
      VoDescriptionServer.pushToRegistry(new URL("http://galahad.star.le.ac.uk:8080/galahad-registry/services/AdminService"));
   }
*/
}

