/*
 * $Id: FileResourcePlugin.java,v 1.1 2004/09/28 15:02:13 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
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

public class FileResourcePlugin implements VoResourcePlugin
{
   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_FILE_LOC_KEY = "datacenter.metadata.filename";
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_URL_LOC_KEY = "datacenter.metadata.url";

   /** Default metadata filename */
   public static final String METADATA_DEFAULT_FILENAME = "datacenter.metadata.xml";
   
   /** Returns the URL to the metadata file given by the configuration properties */
   public URL getMetadataUrl() throws IOException {

      String filename = SimpleConfig.getSingleton().getString(METADATA_FILE_LOC_KEY, null);
      URL url = SimpleConfig.getSingleton().getUrl(METADATA_URL_LOC_KEY, null);
      
      if ((filename != null) && (url != null)) {
         throw new ConfigException("Server not configured properly: both file "+filename+" and url "+url+" given in config ("+SimpleConfig.loadedFrom()+") to locate metadata file.  Specify only one.");
      }

      if ((filename == null) && (url == null)) {
         log.warn("Server not configured properly: neither file "+METADATA_FILE_LOC_KEY+" nor url "+METADATA_URL_LOC_KEY+" are given in config ("+SimpleConfig.loadedFrom()+") to locate metadata file. Looking in classpath for "+METADATA_DEFAULT_FILENAME);
         filename = METADATA_DEFAULT_FILENAME;
      }

      //file given - get a url to it
      if (filename != null) {
         url = Config.resolveFilename(filename);

         if (url == null) {
            throw new IOException("metadata file at '"+filename+"' not found");
         }
      }
      
      log.info("Returning metadata from "+url);
      return url;
   }
   
   
   /**
    * Returns the resource elements in the metadata file
    */
   public String getVoResource() throws IOException
   {
      URL url = getMetadataUrl();
      InputStream is = url.openStream();

      if (is == null) {
         throw new FileNotFoundException("Metadata file not found at "+url);
      }
      
      try
      {
         Document diskDoc = DomHelper.newDocument(is);
         
         String rootElementName = diskDoc.getDocumentElement().getNodeName();
         
         //if the root element is a resource, return that
         if (diskDoc.getDocumentElement().getNodeName().equals("Resource")) {
            return DomHelper.ElementToString(diskDoc.getDocumentElement());
         }

         //if the root element is a vodescription, return all resource elements
         if (diskDoc.getDocumentElement().getNodeName().equals("VODescription")) {
            NodeList voResources = diskDoc.getElementsByTagName("Resource");
            if (voResources.getLength()==0) {
               throw new MetadataException("No <Resource> elements in metadata file "+url);
            }
            StringBuffer s = new StringBuffer();
            for (int i = 0; i < voResources.getLength(); i++) {
               s.append(DomHelper.ElementToString((Element) voResources.item(i))+"\n");
            }
            return s.toString();
         }
   
         throw new MetadataException("Metadata file "+url+" does not have <Resource> or <VODescription> as root element");
         
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


