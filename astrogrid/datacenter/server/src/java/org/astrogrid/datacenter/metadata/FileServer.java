/*
 * $Id: FileServer.java,v 1.1 2004/08/18 18:44:12 mch Exp $
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
 * Serves the service's metadata from a file on disk.
 * <p>
 * @author M Hill
 */

public class FileServer implements MetadataPlugin
{
   protected static Log log = LogFactory.getLog(MetadataServer.class);
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_FILE_LOC_KEY = "datacenter.metadata.filename";
   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_URL_LOC_KEY = "datacenter.metadata.url";

   /** Default metadata filename */
   public static final String METADATA_DEFAULT_FILENAME = "datacenter.metadata.xml";
   
   /** Returns a stream to the metadata file */
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
    * Returns the whole metadata file as a DOM document
    */
   public Document getMetadata() throws IOException
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


