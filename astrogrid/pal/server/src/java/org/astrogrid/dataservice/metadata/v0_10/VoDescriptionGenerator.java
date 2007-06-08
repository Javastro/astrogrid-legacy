/*
 * (C) Copyright Astrogrid...
 */
package org.astrogrid.dataservice.metadata.v0_10;

import java.io.IOException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionGeneratorBase;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;

// v0.10 resources
import org.astrogrid.dataservice.metadata.v0_10.VoResourceSupport;
import org.astrogrid.dataservice.service.cea.v0_10.CeaResources;
import org.astrogrid.dataservice.service.cone.v0_10.ConeResources;

// XML parsing stuff
import org.astrogrid.xml.DomHelper;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Assembles the various v0.10 VoResource elements provided by the plugins, 
 * and serves them all up wrapped in a VoDescription element for submitting 
 * to registries
 *
 * @see VoResourceSupport for how resource elements are generated
 * @see package documentation
 * <p>
 * @author M Hill
 */

public class VoDescriptionGenerator extends VoDescriptionGeneratorBase {

   protected static Log log = LogFactory.getLog(VoDescriptionGenerator.class);
   
   public final static String VODESCRIPTION_ELEMENT=
         "<vor:VOResources xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xmlns:vor=\"http://www.ivoa.net/xml/RegistryInterface/v0.1\" xsi:schemaLocation=\"http://www.ivoa.net/xml/RegistryInterface/v0.1 http://software.astrogrid.org/schema/registry/RegistryInterface/v0.1/RegistryInterface.xsd\">\n";

   public final static String VODESCRIPTION_ELEMENT_END= "</vor:VOResources>";


   /**
    * Returns the whole metadata file as a DOM document
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
    * Make a VODescription document out of all the voResourcePlugins, 
    * returning an unvalidated string.  This means we can view the made 
    * (finished) document separate from the validating process. */
   public String makeVoDescription() throws IOException, MetadataException {

      // Check whether v0.10 resources are required
      if (VoDescriptionServer.isEnabled(VoDescriptionServer.V0_10) == false) {
        throw new MetadataException("V0.10 resources are disabled in DSA configuration file");
      }
      // Should we publish conesearch resources?  
      boolean doCone = false; 
      String s = ConfigFactory.getCommonConfig().getString(
          "datacenter.implements.conesearch",null);
      if ( (s != null) && ( s.equals("true") || s.equals("TRUE") ) ) {
         doCone = true;
      }
      /*
      // NOTE:  WE CURRENTLY DON'T HAVE AN AuthorityID REGISTRATION PLUGIN!
      // Should we register the Authority ID?  (Default no)
      boolean doAuthID = false;
      s = ConfigFactory.getCommonConfig().getString(
          "datacenter.resource.register.authID",null);
      if ( (s != null) && ( s.toLowerCase().equals("enabled") ) ) {
         doAuthID = true;
      }
      */
      Vector pluginList = new Vector();
    // Set up v0.10 resources
      pluginList.add((Object)"org.astrogrid.tableserver.metadata.v0_10.TabularDbResources");
      pluginList.add((Object)"org.astrogrid.dataservice.service.cea.v0_10.CeaResources");
      if (doCone) {
         pluginList.add((Object)"org.astrogrid.dataservice.service.cone.v0_10.ConeResources");
      }
      /*
      if (doAuthID) {
         pluginList.add((Object)"org.astrogrid.datacenter.metadata.AuthorityConfigPlugin");

      }
      */
      //start the vodescription document
      StringBuffer vod = new StringBuffer();
      vod.append(VODESCRIPTION_ELEMENT+"\n");
      boolean ceaDone = false; //backwards compatiblity marker to see if CeaResource has been done
      
      //loop through plugins adding each one's list of resources
      //if (plugins != null) {
         //for (int p = 0; p < plugins.size(); p++) {
      if (pluginList.size() != 0) {
         for (int p = 0; p < pluginList.size(); p++) {
            String pluginName = (String)pluginList.get(p);
            log.debug("Including Resource plugin "+pluginName);

            //make plugin
            VoResourcePlugin plugin = createVoResourcePlugin(pluginName);
            checkAndAppendResource(vod, plugin, VODESCRIPTION_ELEMENT, VODESCRIPTION_ELEMENT_END);
         }
      }
      vod.append(VODESCRIPTION_ELEMENT_END);
      return vod.toString();
   }

   public Document getWrappedResource(String resourceType) throws MetadataException, IOException
   {
      Element element = getResource(resourceType);
      if (element == null) {
        throw new MetadataException("No resource called '" + 
            resourceType + "' is available");
      }
      StringBuffer vod = new StringBuffer();
      vod.append(VODESCRIPTION_ELEMENT+"\n");
      vod.append(element.toString());
      vod.append("\n"+VODESCRIPTION_ELEMENT_END);
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
