/*
 * $Id: VoDescriptionServer.java,v 1.11 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.io.xml.XmlTagPrinter;
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
   
   public static final String AUTHID_KEY = "datacenter.authorityId";
   public static final String RESKEY_KEY = "datacenter.resourceKey";
   
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
            
            Element id = DsaDomHelper.getSingleChildByTagName(resource, "Identifier");
            if (id == null) {
               //no identifier - could add one but we don't know what resource key to give it
               throw new MetadataException("Resource "+i+" (xsi:type="+resource.getAttribute("xsi:type")+") has no Identifier");
            }
            
            DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(id, "AuthorityID"), SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY));
            Element resKey = DsaDomHelper.getSingleChildByTagName(id, "ResourceKey");
            if ((resKey == null) || (DomHelper.getValue(resKey).trim().length()==0)) {
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
         throw new RuntimeException("Bad metadata plugin specified ("+pluginClassName+") - does not implement VoResourcePlugin");
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

      //get plugin list from config - need to add a default to the common method...
      Object[] plugins  = null;
      try {
         plugins = SimpleConfig.getSingleton().getProperties(VoResourcePlugin.RESOURCE_PLUGIN_KEY);
      } catch (PropertyNotFoundException pnfe)
      {
         //for backwards compatibility, look for old datacenter.metadata.plugin
         String s = SimpleConfig.getSingleton().getString("datacenter.metadata.plugin",null);
         if (s != null) {
            plugins = new String[] { s };
         }
      }
      //if they are not specified, assume one AuthorityConfigPlugin and a FileResourcePlugin
      if ((plugins == null) || (plugins.length==0)) {
         plugins = new String[] {
            AuthorityConfigPlugin.class.getName(),
            FileResourcePlugin.class.getName(),
            CeaResourceServer.class.getName(),
         };
      }

      //start the vodescription document
      StringBuffer vod = new StringBuffer();
      vod.append(VODESCRIPTION_ELEMENT+"\n");

      //loop through plugins adding each one's list of resources
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
   
   /** Adds an Identifier tag to the given XmlPrinter */
   public static void addIdentifier(XmlTagPrinter parent, String resourceKeyEnd) throws IOException {
      XmlTagPrinter identifier = parent.newTag("Identifier");
      identifier.writeTag("AuthorityID", SimpleConfig.getSingleton().getString(AUTHID_KEY));
      identifier.writeTag("ResourceKey", SimpleConfig.getSingleton().getString(RESKEY_KEY)+resourceKeyEnd);
   }

   /** Writes out standard summary stuff to the given XmlPrinter */
   public static void writeSummary(XmlTagPrinter parent) throws IOException {
      parent.writeTag("Title", DataServer.getDatacenterName());
      parent.writeTag("ShortName", SimpleConfig.getSingleton().getString("datacenter.shortname", ""));
      
      XmlTagPrinter summary = parent.newTag("Summary");
      summary.writeTag("Description", SimpleConfig.getSingleton().getString("datacenter.description", ""));
      summary.writeTag("ReferenceURL", SimpleConfig.getSingleton().getString("datacenter.url", ""));
   }
   
   /**  Writes out standard curation stuff to the given XmlPrinter  */
   public static void writeCuration(XmlTagPrinter parent) throws IOException {
      XmlTagPrinter curation = parent.newTag("Curation");
      String publisher = SimpleConfig.getSingleton().getString("datacenter.publisher",null);
      if (publisher != null) {
         XmlTagPrinter publisherTag = curation.newTag("Publisher");
         publisherTag.writeTag("Title", publisher);
      }

      XmlTagPrinter contact = curation.newTag("Contact");
      contact.writeTag("Name", SimpleConfig.getSingleton().getString("datacenter.contact.name",""));
      contact.writeTag("Email", SimpleConfig.getSingleton().getString("datacenter.contact.email",""));
      contact.writeTag("Date", SimpleConfig.getSingleton().getString("datacenter.contact.date",""));
   }

   /** Checks that the identifier elements are there and set to the local values,
    * creating and setting if not.  The given resourceKeyEnd is appended to the
    datacenter's resourceKey to give the appropriate full resource key */
   public static void ensureIdentifier(Element resource, String resourceKeyEnd) {

      //check that each 'returns' resource has a child <Identifier>, as some early ones didn't and we want to catch them
      Element voIdTag = DsaDomHelper.ensuredGetSingleChild(resource, "Identifier");
      
      String dsaResKey = SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY);
   
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(voIdTag, "AuthorityID"), SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY));
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(voIdTag, "ResourceKey"), SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY)+resourceKeyEnd);
   }

   /** Checks the summary elements are there and set to the local values (creating it if not) */
   public static void ensureSummary(Element resource) {
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(resource, "Title"), DataServer.getDatacenterName());
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(resource, "ShortName"), SimpleConfig.getSingleton().getString("datacenter.shortname", ""));

      Element summary = DsaDomHelper.ensuredGetSingleChild(resource, "Summary");
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(summary, "Description"), SimpleConfig.getSingleton().getString("datacenter.description", ""));
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(summary, "ReferenceURL"), SimpleConfig.getSingleton().getString("datacenter.url", ""));
   }

   /**  Checks the curation stuff is present and set to the local values (creating it if not) */
   public static void ensureCuration(Element resource)  {
      Element curation = DsaDomHelper.ensuredGetSingleChild(resource, "Curation");
      String publisher = SimpleConfig.getSingleton().getString("datacenter.publisher",null);
      if (publisher != null) {
         Element title = DsaDomHelper.ensuredGetSingleChild(curation, "Title");
         DsaDomHelper.setElementValue(title, publisher);
      }

      Element contact = DsaDomHelper.ensuredGetSingleChild(curation, "Contact");
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(contact, "Name"), SimpleConfig.getSingleton().getString("datacenter.contact.name", ""));
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(contact, "Email"), SimpleConfig.getSingleton().getString("datacenter.contact.email", ""));
      DsaDomHelper.setElementValue(DsaDomHelper.ensuredGetSingleChild(contact, "Date"), SimpleConfig.getSingleton().getString("datacenter.contact.date", ""));
   }
   
   /** Converts date to string suitable for registry */
   public String toRegistryForm(Date givenDate) {
      //deprecated methods
      //long minsOffset = aDate.getTimezoneOffset();
      //Date gmtDate = new Date( aDate.getTime() + aDate.getTimezoneOffset());
      //this also leaves us with a date that has it's original timezone, but a new value.  It's really a different time...
      
      //cludge to get timezone of given date; write it out and then parse it...
      SimpleDateFormat offsetGetter = new SimpleDateFormat("Z");
      String offsetName = offsetGetter.format(givenDate);
      TimeZone givenZone = TimeZone.getTimeZone(offsetName);
      int offset = givenZone.getOffset(givenDate.getTime());
//      Date gmtDate = new Date(givenZone.getOffset();
      
      
      //convert to GMT
      Calendar localCalender = Calendar.getInstance();
      TimeZone localZone = localCalender.getTimeZone();
      Calendar ukcalender = new GregorianCalendar(Locale.ENGLISH);
      TimeZone gmtZone = TimeZone.getTimeZone("GMT");

      return "";
   }
   
}







