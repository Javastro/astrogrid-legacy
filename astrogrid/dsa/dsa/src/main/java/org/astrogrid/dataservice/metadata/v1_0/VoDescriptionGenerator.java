/*
 * (C) Copyright Astrogrid...
 */
package org.astrogrid.dataservice.metadata.v1_0;

import java.io.IOException;
import java.util.Vector;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionGeneratorBase;
import org.astrogrid.dataservice.metadata.MetadataHelper;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoResourceSupportBase;

//import org.astrogrid.dataservice.metadata.VoResourcePlugin;

// v1.0 resources
//import org.astrogrid.dataservice.metadata.v1_0.VoResourceSupport;
import org.astrogrid.dataservice.service.cea.v1_0.CeaResources;
import astrogrid.dataservice.service.cone.ConeResources;
import org.astrogrid.tableserver.metadata.v1_0.TableResources;

// AG stuff
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

// XML parsing stuff
import org.astrogrid.xml.DomHelper;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Assembles the v1.0 VOResource registrations for the DSA.
 * Serves the individual resources wrapped in a parent VOResources 
 * element for submitting to registries.  Note that this is still
 * "old-style" registration where the DSA self-registers all its 
 * elements.
 *
 * Note that in the new v1.0 registry, we are trying to get as many
 * metadata elements as possible into a single VOResource (unlike the 
 * v0.10 registrations, where the different interfaces each provided 
 * their own separate VOResources).
 *
 * This change makes the generic VOResource plugin model not so workable 
 * (because different interfaces now need to slot *different* kinds of 
 * metadata into the same parent VOResource (rather than each independently 
 * producing their own, complete VOResource).
 *
 * After a bit of experimenting I have changed the architecture somewhat,
 * so that we now explicitly assemble the required VOResources with
 * calls direct to the relevant v1.0 metadata-generation classes
 * (rather than accessing these classes "generically" via a common 
 * VoResources interface, which was becoming messy - the "generic" interface 
 * was becoming a kitchen sink of context-specific methods that did nothing
 * in most instantiating classes.))
 *
 * @see package documentation
 * <p>
 * @author M Hill, K Andrews
 */

public class VoDescriptionGenerator extends VoDescriptionGeneratorBase {

   protected static Log log = LogFactory.getLog(VoDescriptionGenerator.class);
   
   public final static String VORESOURCES_NAMESPACES = 
     "xmlns:ri=\"http://www.ivoa.net/xml/RegistryInterface/v1.0\"\n" +
     "xmlns:vs=\"http://www.ivoa.net/xml/VODataService/v1.0\"\n" +
     "xmlns:cs=\"http://www.ivoa.net/xml/ConeSearch/v1.0\"\n" +
     "xmlns:cea=\"http://www.ivoa.net/xml/CEA/v1.0rc1\"\n" +
     "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n";

   public final static String VORESOURCES_SCHEMALOCATIONS = 
     "http://www.ivoa.net/xml/RegistryInterface/v1.0 " +
     "http://software.astrogrid.org/schema/registry/RegistryInterface/v1.0/RegistryInterface.xsd \n" +
     "http://www.ivoa.net/xml/RegistryInterface/v1.0 " +
     "http://software.astrogrid.org/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd \n" +
     "http://www.ivoa.net/xml/ConeSearch/v1.0 " +
     "http://software.astrogrid.org/schema/vo-resource-types/ConeSearch/v1.0/ConeSearch.xsd \n" +
     "http://www.ivoa.net/xml/CEA/v1.0rc1 " +
     "http://software.astrogrid.org/schema/vo-resource-types/CEAService/v1.0rc1/CEAService.xsd";

   public final static String VORESOURCES_START =
      "<ri:VOResources \n" +
         VORESOURCES_NAMESPACES + 
         "  xsi:schemaLocation=\"" +
         VORESOURCES_SCHEMALOCATIONS +"\"" +
         " from=\"1\" numberReturned=\"1\" more=\"false\">\n";

   public final static String VORESOURCES_END = "</ri:VOResources>";

   //public static final String VORESOURCE_START = "ri:Resource";
      //"<ri:Resource \n" + "status='active' updated='"+toRegistryForm(new Date())+"' xsi:type='"+vorType+"'"+
    //     ">";
   //public static final String VORESOURCE_END = "</ri:Resource>";

   /**
    * Returns the whole metadata file as a DOM document 
    * @deprecated  Used with old-style push registration. Not relevant now
    * that we are using new IVOA registration conventions, and NO LONGER
    * SUPPORTED.
    */
   public Document getVoDescription() throws IOException {
      Document doc;
      try {
         doc = DomHelper.newDocument(makeVoDescription());
      }
      catch (SAXException e) {
         throw new MetadataException("XML error with Metadata: "+e,e);
      }
      return doc;
   }

   /**
    * Produce the DSA v1.0 VOResources, returning an unvalidated string.  
    * (This means we can view the complete VOResources separate from the 
    * validating process, useful for debugging). 
    * @deprecated  Used with old-style push registration. Not relevant now
    * that we are using new IVOA registration conventions, and NO LONGER
    * SUPPORTED.
    */
   public String makeVoDescription() throws IOException, MetadataException {

      // Check whether v1.0 resources are required
      if (VoDescriptionServer.isEnabled(VoDescriptionServer.V1_0) == false) {
        throw new MetadataException("V1.0 resources are disabled in DSA configuration file");
      }
      // Should we publish conesearch resources?  
      boolean doCone = false; 
      String s = ConfigFactory.getCommonConfig().getString(
          "datacenter.implements.conesearch",null);
      if ( (s != null) && ( s.equals("true") || s.equals("TRUE") ) ) {
         doCone = true;
      }

      /*
      Vector pluginList = new Vector();
      pluginList.add((Object)"org.astrogrid.tableserver.metadata.v1_0.TableResources");
      pluginList.add((Object)"org.astrogrid.dataservice.service.cea.v1_0.CeaResources");
      if (doCone) {
         pluginList.add((Object)"org.astrogrid.dataservice.service.cone.v1_0.ConeResources");
      }
      */

      // We need to produce one set of resources for every catalog (database)
      // wrapped by the DSA - because (a) there is no parent <catalog> tag
      // in a CatalogService, so we cannot hierarchically organise the
      // tables and (b) different catalogs may well need very different 
      // Dublin core metadata, coverage etc.
      // First of all, find out what catalogs we have 
      String[] catNames = TableMetaDocInterpreter.getCatalogNames();
      if (catNames.length == 0) {
         // Shouldn't get here
         throw new MetadataException("DSA is misconfigured - no catalogs have been defined in the DSA metadoc");
      }
      String closeResource = VoResourceSupportBase.closeVoResourceElement_1_0();

      // Create a parent VOResources wrapper tag, *including all 
      // required schemata here* 
      StringBuffer vod = new StringBuffer();
      vod.append("\n<ri:VOResources \n");
      vod.append(VORESOURCES_NAMESPACES);
      vod.append(" xsi:schemaLocation=\""+VORESOURCES_SCHEMALOCATIONS+"\" ");
      vod.append("from=\"1\" numberReturned=\""+
            Integer.toString(catNames.length*2) + //1 core & 1 CEA app *per cat*
            "\" more=\"false\">\n");

      for (int i = 0; i < catNames.length; i++) {

         // Build the parent tag and Dublin Core metadata
         // KONA TOFIX : At the moment this is using "one size fits all"
         // settings for dublin core metadata, taken from properties file
         // Maybe need separate metadata for separate catalogs?
         
         // Generate service-for element to link CEA service to CEA application
         String[] end = new String[1];
         end[0] = CeaResources.getAppID(catNames[i]);
         String relTag = VoResourceSupportBase.makeRelationshipTag(
               "service-for", end);
         // Generate relatedTo elements if we have multiple catalogs 
         // This is a bit of a kludge, but it provides a means of detecting
         // which catalogs are served by the same physical service (for when
         // cross-DB querying is available)
         if (catNames.length > 1) {
            String endpoints[] = new String[catNames.length-1];
            int index = 0;
            for (int j = 0; j < catNames.length; j++) {
               if (j != i) {  //Don't refer to self in related resources
                  endpoints[index] = VoResourceSupportBase.makeIvorn(catNames[j]);
                  index = index + 1;
               }
            }
            relTag = relTag + VoResourceSupportBase.makeRelationshipTag(
                  "related-to",endpoints); 
         }
         StringBuffer coreStuff = new StringBuffer(
            VoResourceSupportBase.openVoResourceElement_1_0(
               "vs:CatalogService") + // Opening tag
            // Dublin core metadata
            VoResourceSupportBase.makeDublinCore(catNames[i],"",relTag) 
         );      


         // Start the main registration (for the DSA and all its capabilities)
         vod.append(coreStuff);

         // Now add any capabilities available
         // This should include e.g. the catalog 
         //System.out.println("CONE CAPS: ");
         //System.out.println(VORESOURCES_START + coreStuff+ConeResources.getConeCapabilities(catNames[i]) + closeResource+VORESOURCES_END);
         String capabilities = ConeResources.getConeCapabilities(catNames[i]);
         if (!"".equals(capabilities)) {
            checkAndAppendSubelements(vod, capabilities, 
                  VORESOURCES_START+coreStuff, closeResource+VORESOURCES_END, 
                  ConeResources.class.toString());
         }
         //System.out.println("CEA CAPS: ");
         //System.out.println(VORESOURCES_START + coreStuff+CeaResources.getCeaServerCapabilities(catNames[i]) + closeResource+VORESOURCES_END);
         capabilities = CeaResources.getCeaServerCapabilities(catNames[i]);
         if (!"".equals(capabilities)) {
            checkAndAppendSubelements(vod, capabilities, 
                  VORESOURCES_START+coreStuff, closeResource+VORESOURCES_END, 
                  CeaResources.class.toString());
         }
			// Now add delegation servlet capability
			capabilities = 
				"<capability standardID=\"ivo://ivoa.net/std/Delegation\">" +
				"  <interface xsi:type=\"vs:ParamHTTP\">" +
				"    <accessURL use=\"full\">" +
				MetadataHelper.getInstallationBaseURL()+"delegations"+
				"</accessURL>" +
				"  </interface>" +
				"</capability>";
          checkAndAppendSubelements(vod, capabilities, 
                VORESOURCES_START+coreStuff, closeResource+VORESOURCES_END, 
                VoDescriptionGenerator.class.toString());

         // Now add any table descriptions available
         String catID = 
            TableMetaDocInterpreter.getCatalogIDForName(catNames[i]);
         String tables = TableResources.getTableDescriptions(catID);
         checkAndAppendSubelements(vod, tables, 
               VORESOURCES_START+coreStuff, closeResource+VORESOURCES_END, 
               TableResources.class.toString());

         // Now close the main registration
         vod.append(VoResourceSupportBase.closeVoResourceElement_1_0());

         //Now add the CEA Application registration
         //System.out.println("CEA RESOURCES");
         //System.out.println(VORESOURCES_START+coreStuff+CeaResources.getCeaAppResource(catNames[i]) + closeResource+VORESOURCES_END);
         checkAndAppendSubelements(vod, CeaResources.getCeaAppResource(catNames[i]), VORESOURCES_START, VORESOURCES_END, CeaResources.class.toString());
         /*
         if (pluginList.size() != 0) {
            for (int p = 0; p < pluginList.size(); p++) {
               String pluginName = (String)pluginList.get(p);
               log.debug("Including Resource plugin "+pluginName);

               //make plugin
               VoResourcePlugin plugin = createVoResourcePlugin(pluginName);
               checkAndAppendResource(vod, plugin, VODESCRIPTION_ELEMENT, VODESCRIPTION_ELEMENT_END);
            }
         }
         */
      }
      vod.append(VORESOURCES_END);
      //System.out.println(vod.toString());
      return vod.toString();
   }

   /*
    * @deprecated  Used with old-style push registration. Not relevant now
    * that we are using new IVOA registration conventions, and NO LONGER
    * SUPPORTED.
    */
   public Document getWrappedResource(String resourceType) throws MetadataException, IOException
   {
      Element element = getResource(resourceType);
      if (element == null) {
        throw new MetadataException("No resource called '" + 
            resourceType + "' is available");
      }
      StringBuffer vod = new StringBuffer();
      vod.append(VORESOURCES_START+"\n");
      vod.append(element.toString());
      vod.append("\n"+VORESOURCES_END);
      Document doc;
      try {
         doc = DomHelper.newDocument(vod.toString());
      }
      catch (SAXException e) {
         throw new MetadataException("XML error with Metadata: "+e,e);
      }
      return doc;
   }
}
