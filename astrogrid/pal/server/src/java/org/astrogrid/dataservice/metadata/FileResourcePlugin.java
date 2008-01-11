/*
 * $Id: FileResourcePlugin.java,v 1.3 2008/01/11 15:58:24 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.io.IOException;
import java.net.URL;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.cfg.ConfigException;
import org.astrogrid.cfg.ConfigFactory;

/**
 * Serves a metadata resource from a file on disk.
 * <p>
 * @author M Hill
 * @deprecated  Used with old-style push registration. Not relevant now
 * that we are using new IVOA registration conventions, and NO LONGER
 * SUPPORTED.
 *
 */

public class FileResourcePlugin extends UrlResourcePlugin
{
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_FILE_LOC_KEY = "datacenter.resource.filename";
   
   /** Returns the URLs to the metadata files given by the configuration properties */
   public URL[] getResourceUrls() throws IOException {

      Object[] filenames = ConfigFactory.getCommonConfig().getProperties(METADATA_FILE_LOC_KEY);
      
      if (filenames.length == 0 ) {
         throw new ConfigException("Server not configured properly: no '"+METADATA_FILE_LOC_KEY+"' keys are set in config ("+ConfigFactory.getCommonConfig().loadedFrom()+") to locate metadata file.");
      }

      URL[] resourceUrls = new URL[filenames.length];

      for (int f = 0; f < filenames.length; f++) {
         resourceUrls[f] = ConfigReader.resolveFilename(filenames[f].toString());
         
         if (resourceUrls[f] == null) {
            throw new IOException("Resource file at '"+filenames[f]+"' not found");
         }
      }
      return resourceUrls;
   }
   
   
}


