/*
 * $Id: SampleStarsMetaServer.java,v 1.1 2004/09/28 15:02:13 mch Exp $
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
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
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
      //this works OK for unit test, but not deployment...
      URL url = SampleStarsMetaServer.class.getResource("samplestars.metadata.xml");
      if (url == null) {
         //this works OK for deployment, but not unit tests...
         url = Config.resolveFilename("samplestars.metadata.xml");
      }
      return url;
   }
   
   /**
    * Initialises config so that authority ID, etc are set
    */
   public static void initConfig() {
      SimpleConfig.setProperty(VoDescriptionServer.PLUGIN_KEY, SampleStarsMetaServer.class.getName());
      SimpleConfig.setProperty("datacenter.name", "SampleStars AstroGrid Datacenter");
      SimpleConfig.setProperty("datacenter.shortname", "PAL-Sample");
      SimpleConfig.setProperty("datacenter.publisher", "AstroGrid");
      SimpleConfig.setProperty("datacenter.description", "An unconfigured datacenter; it contains two tables of sample stars and galaxies for testing and demonstration purposes.");
      SimpleConfig.setProperty("datacenter.contact.name", "Martin Hill");
      SimpleConfig.setProperty("datacenter.contact.name", "mch@roe.ac.uk");

      SimpleConfig.setProperty("datacenter.authority.metadata.plugin", "org.astrogrid.datacenter.metadata.AuthorityConfigPlugin");
      SimpleConfig.setProperty("datacenter.authorityId", "astrogrid.org");
      SimpleConfig.setProperty("datacenter.resourceKey", "pal-sample");

   }
   
}


