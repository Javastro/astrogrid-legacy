/*
 * $Id: UrlResourcePlugin.java,v 1.1 2004/10/12 23:09:53 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
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
         InputStream is = resourceUrl.openStream();
   
         if (is == null) {
            throw new FileNotFoundException("Metadata file not found at "+resourceUrl);
         }
         
         try
         {
            Document diskDoc = DomHelper.newDocument(is);
            
            String rootElementName = diskDoc.getDocumentElement().getNodeName();
            
            //if the root element is a resource, return that
            if (diskDoc.getDocumentElement().getNodeName().equals("Resource")) {
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
                  returns[i] = DomHelper.ElementToString((Element) voResources.item(i));
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
   
   
}


