/*
 * $Id: FileResourcePlugin.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;

import java.io.IOException;
import java.net.URL;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;

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
         throw new ConfigException("Server not configured properly: no '"+METADATA_FILE_LOC_KEY+"' keys are set in config ("+SimpleConfig.loadedFrom()+") to locate metadata file.");
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


