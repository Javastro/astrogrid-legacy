/*
 * $Id: MetadataServer.java,v 1.4 2004/03/13 23:38:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types.Language;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.spi.PluginQuerier;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Serves the service's metadata file.  See package documentation.
 * <p>
 * @author M Hill
 */

public class MetadataServer
{
   protected static Log log = LogFactory.getLog(MetadataServer.class);
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_FILE_LOC_KEY = "datacenter.metadata.filename";
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_URL_LOC_KEY = "datacenter.metadata.url";

   /** Default metadata filename */
   public static final String METADATA_DEFAULT_FILENAME = "datacenter.metadata.xml";
   /**
    * Returns the VODescription element of the metadata
    * If there is more than one, logs an error but does not fail.
    */
   public static Element getVODescription() throws IOException
   {
      NodeList nodes = getMetadata().getElementsByTagName("VODescription");

      if (nodes.getLength()>1) {
         log.error("Server not configured properly: Too many VODescription nodes in metadata - place all VOResource elements in one VODescription");
      }

      if (nodes.getLength()==0) {
         throw new DatacenterException("Server not configured completely; no VODescription element in its metadata");
      }
      
      return (Element) nodes.item(0);
   }
   
   /** Returns a stream to the metadata file */
   public static URL getMetadataUrl() throws IOException {

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
      
      return url;
   }
   
   
   /**
    * Returns the whole metadata file as a DOM document
    */
   public static Document getMetadata() throws IOException
   {
      InputStream is = getMetadataUrl().openStream();
      try
      {
         return DomHelper.newDocument(is);
      }
      catch (ParserConfigurationException e)
      {
         log.error("XML Parser not configured properly",e);
         throw new RuntimeException("Server not configured properly",e);
      }
      catch (SAXException e)
      {
         log.error("Invalid metadata",e);
         throw new RuntimeException("Server not configured properly - invalid metadata",e);
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
   
   
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.axisdataserver.AxisDataServer#getLanguageInfo(java.lang.Object)
    */
   public Language[] getLanguageInfo(Object arg0) throws RemoteException {
      
      try {
         return PluginQuerier.instantiateQuerierSPI().getTranslatorMap().list();
      } catch (DatabaseAccessException e) {
         throw new RemoteException("Could not instantiate querier SPI",e);
      }
   }
   
}


