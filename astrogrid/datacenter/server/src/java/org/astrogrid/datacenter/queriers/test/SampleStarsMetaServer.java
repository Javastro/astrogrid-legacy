/*
 * $Id: SampleStarsMetaServer.java,v 1.2 2004/09/06 20:23:00 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.test;

import java.io.IOException;
import java.net.URL;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.FileResourcePlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;

/**
 * A special file-serving metadata server; it checks that the SampleStarsPlugin
 * is the current plugin and throws an exception if not, so that we don't get
 * incompatible metadata vs plugin
 * <p>
 * @author M Hill
 */

public class SampleStarsMetaServer extends FileResourcePlugin
{
   /** Returns a URL to the metadata file */
   public URL getMetadataUrl() throws IOException {

      String pluginClass = SimpleConfig.getSingleton().getString(QuerierPluginFactory.PLUGIN_KEY);
      String sampleStarsClass = SampleStarsPlugin.class.getName();
      if (!pluginClass.equals(SampleStarsPlugin.class.getName())) {
         throw new ConfigException("Server is configured to return sample stars metadata but the plugin is "+pluginClass);
      }
      return Config.resolveFilename("samplestars.metadata.xml");
   }
   
   
   
}


