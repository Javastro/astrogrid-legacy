/*
 * $Id: FileResourcePlugin.java,v 1.3 2004/10/12 23:09:53 mch Exp $
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

public class FileResourcePlugin extends UrlResourcePlugin
{
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_FILE_LOC_KEY = "datacenter.resource.filename";
   
   /** Returns the URLs to the metadata files given by the configuration properties */
   public URL[] getResourceUrls() throws IOException {

      Object[] filenames = SimpleConfig.getSingleton().getProperties(METADATA_FILE_LOC_KEY);
      
      if (filenames.length == 0 ) {
         throw new ConfigException("Server not configured properly: files "+METADATA_FILE_LOC_KEY+".*  are not given in config ("+SimpleConfig.loadedFrom()+") to locate metadata file.");
      }

      URL[] resourceUrls = new URL[filenames.length];

      for (int f = 0; f < filenames.length; f++) {
         resourceUrls[f] = Config.resolveFilename(filenames[f].toString());
         
         if (resourceUrls[f] == null) {
            throw new IOException("Resource file at '"+filenames[f]+"' not found");
         }
      }
      return resourceUrls;
   }
   
   
}


