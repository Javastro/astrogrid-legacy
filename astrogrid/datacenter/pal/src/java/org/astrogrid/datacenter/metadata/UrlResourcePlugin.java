/*
 * $Id: UrlResourcePlugin.java,v 1.3 2004/10/25 10:43:12 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.io.xml.XmlTagPrinter;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Serves a metadata resource from a file on disk.
 * <p>
 * @author M Hill
 */

public class UrlResourcePlugin implements VoResourcePlugin
{
   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_URL_LOC_KEY = "datacenter.resource.url";

   /** Returns the URLs to the metadata given by the configuration properties */
   public URL[] getResourceUrls() throws IOException {

      Object[] urls = SimpleConfig.getSingleton().getProperties(METADATA_URL_LOC_KEY);
      
      if (urls.length == 0) {
         throw new ConfigException("Server not configured properly: no url "+METADATA_URL_LOC_KEY+".* keys are given in config ("+SimpleConfig.loadedFrom()+") to locate metadata.");
      }

      URL[] resourceUrls = new URL[urls.length];

      for (int u=0; u<urls.length; u++) {
         resourceUrls[u] = new URL(urls[u].toString());
      }
      
      return resourceUrls;
   }
   
   /** Returns an array of resources by going through the URLs given by the config
    * key, extracting the resources in each one (as each one may have more than one) and appedning them
    */
   public String[] getVoResources() throws IOException
   {
      Vector resources = new Vector();
      URL[] urls = getResourceUrls();
      for (int u = 0; u < urls.length; u++) {
         String[] urlResources = getVoResources(urls[u]);
         for (int i = 0; i < urlResources.length; i++) {
            resources.add(urlResources[i]);
         }
      }
      return (String[]) resources.toArray(new String[] {});
   }
   
   /**
    * Returns the resource elements at the given URL
    */
   public String[] getVoResources(URL resourceUrl) throws IOException
   {
      URLConnection connection = resourceUrl.openConnection();
      InputStream is = connection.getInputStream();

      if (is == null) {
         throw new FileNotFoundException("Metadata file not found at "+resourceUrl);
      }
      
      try
      {
         Document diskDoc = DomHelper.newDocument(is);
         
         String rootElementName = diskDoc.getDocumentElement().getNodeName();
         
         //if the root element is a resource, return that
         if (diskDoc.getDocumentElement().getNodeName().equals("Resource")) {
            checkResource( diskDoc.getDocumentElement(), connection);
            return new String[] { DomHelper.ElementToString(diskDoc.getDocumentElement()) };
         }

         //if the root element is a vodescription, return all resource elements
         if (diskDoc.getDocumentElement().getNodeName().equals("VODescription")) {
            NodeList voResources = diskDoc.getElementsByTagName("Resource");
            if (voResources.getLength()==0) {
               throw new MetadataException("No <Resource> elements in metadata file "+resourceUrl);
            }
            String[] returns = new String[voResources.getLength()];

            for (int i = 0; i < voResources.getLength(); i++) {
               Element resource = (Element) voResources.item(i);
               
               checkResource(resource, connection);
               
               returns[i] = DomHelper.ElementToString(resource);
            }

            return returns;
         }
   
         throw new MetadataException("Metadata at "+resourceUrl+" does not have <Resource> or <VODescription> as root element");
         
      }
      catch (ParserConfigurationException e)
      {
         log.error("XML Parser not configured properly",e);
         throw new RuntimeException("Server not configured properly",e);
      }
      catch (SAXException e)
      {
         log.error("Invalid metadata",e);
         throw new RuntimeException("Server not configured properly - invalid metadata: "+e,e);
      }
      finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               // not bothered
            }
         }
      }
   }

   
   /** Checks that a resource is OK for this datacenter */
   public void checkResource(Element resource, URLConnection connection) throws MetadataException {
      
      //use file/url date if available, otherwise current time
      Date updated = null;
      if (connection.getLastModified() != 0) {
         updated = new Date(connection.getLastModified());
      }
      else if (connection.getDate() != 0) {
         updated = new Date(connection.getDate());
      }
      else {
         updated = new Date();
      }
      
      resource.setAttribute("status", "active");
      resource.setAttribute("updated", VoDescriptionServer.REGISTRY_DATEFORMAT.format(updated));
      
      checkIdentifierTag(resource);
      
      VoDescriptionServer.ensureSummary(resource);
      VoDescriptionServer.ensureCuration(resource);
   }
   
   public void checkIdentifierTag(Element resource) {

      //check that each 'returns' resource has a child <Identifier>, as some early ones didn't and we want to catch them
      Element voIdTag = DsaDomHelper.ensuredGetSingleChild(resource, "Identifier");
      
      String dsaAuthId = SimpleConfig.getSingleton().getString(VoDescriptionServer.AUTHID_KEY);
      String dsaResKey = SimpleConfig.getSingleton().getString(VoDescriptionServer.RESKEY_KEY);
   
      //we need to override/make sure that they are correct for *this* service, eg
      //we may be proxying to get the metadata
      Element authIdTag = DsaDomHelper.ensuredGetSingleChild(voIdTag, "AuthorityID");
      String docAuthId = DomHelper.getValue(authIdTag);

      if (!docAuthId.equals(dsaAuthId)) {
         log.warn("Authority is "+docAuthId+", but datacenter is configured for "+dsaAuthId+", changing resource to config value in Resource type "+resource.getAttribute("xsi:type"));
         DsaDomHelper.setElementValue(authIdTag, dsaAuthId);
      }

      Element resKeyTag = DsaDomHelper.ensuredGetSingleChild(voIdTag, "ResourceKey");
      String docResKey = DomHelper.getValue(resKeyTag);

      //bit messy - work out how to change resource key without knowing what the one is in the docuemnt.
      //just look for dsaResKey+"/" and replace that bit if nec.
      if (!docResKey.startsWith(dsaResKey+"/")) {
         String newValue = dsaResKey+"/"+docResKey.substring(docResKey.indexOf("/")+1);
         DsaDomHelper.setElementValue(resKeyTag, newValue);
      }
   }

}


