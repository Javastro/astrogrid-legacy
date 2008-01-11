/*
 * $Id: UrlResourcePlugin.java,v 1.7 2008/01/11 15:58:24 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.v0_10.ProxyResourceSupport;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Serves a metadata resource from a file on disk.
 * <p>
 * @author M Hill
 * @deprecated  Used with old-style push registration. Not relevant now
 * that we are using new IVOA registration conventions, and NO LONGER
 * SUPPORTED.
 *
 */

public class UrlResourcePlugin extends ProxyResourceSupport implements VoResourcePlugin {

   
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_URL_LOC_KEY = "datacenter.resource.url";
   
   /** Returns the URLs to the metadata given by the configuration properties */
   public URL[] getResourceUrls() throws IOException {
      
      Object[] urls = ConfigFactory.getCommonConfig().getProperties(METADATA_URL_LOC_KEY);
      
      if (urls.length == 0) {
         throw new ConfigException("Server not configured properly: no url "+METADATA_URL_LOC_KEY+".* keys are given in config ("+ConfigFactory.getCommonConfig().loadedFrom()+") to locate metadata.");
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
   public String getVoResource() throws IOException {
      StringBuffer resources = new StringBuffer();
      
      URL[] urls = getResourceUrls();
      for (int u = 0; u < urls.length; u++) {
         
         URLConnection connection = urls[u].openConnection();
         InputStream is = connection.getInputStream();
         
         if (is == null) {
            throw new FileNotFoundException("Metadata file not found at "+urls[u]);
         }
         
         try {
            resources.append( makeLocal(is));
            
         }
         catch (MetadataException me) {
            log.error("Error in metadata file "+urls[u],me);
            //add information
            MetadataException me2 = new MetadataException(me.getMessage()+" in file "+urls[u],me.getCause());
            me2.setStackTrace(me.getStackTrace());
            throw me2;
         }
      }
      return resources.toString();
   }
   
   
   
}


