/*
 * $Id: SampleStarsMetaServer.java,v 1.5 2004/10/25 00:49:17 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.test;

import java.io.IOException;
import java.net.URL;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.AuthorityConfigPlugin;
import org.astrogrid.datacenter.metadata.CeaResourceServer;
import org.astrogrid.datacenter.metadata.FileResourcePlugin;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.sql.TabularSkyServicePlugin;

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
   public URL[] getResourceUrls() throws IOException {

      //check that this isn't still serving metadata for a non SampleStars query plugin
      String pluginClass = SimpleConfig.getSingleton().getString(QuerierPluginFactory.QUERIER_PLUGIN_KEY);
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
      return new URL[] { url };
   }
   
   /**
    * Initialises config so that authority ID, etc are set
    */
   public static void initConfig() {

      //bear in mind that this is only called when SampleStarsPlugin is called,
      //so getmetadata may fail if called beforehand
      
      //configure so it looks for itself
      SimpleConfig.getSingleton().setProperties(VoResourcePlugin.RESOURCE_PLUGIN_KEY, new Object[] {
               AuthorityConfigPlugin.class.getName(),
               SampleStarsMetaServer.class.getName(),
               TabularSkyServicePlugin.class.getName(),
               CeaResourceServer.class.getName()
            });

      
      //set up the properties for the authority bit
      SimpleConfig.setProperty("datacenter.name", "SampleStars AstroGrid Datacenter");
      SimpleConfig.setProperty("datacenter.shortname", "PAL-Sample");
      SimpleConfig.setProperty("datacenter.publisher", "AstroGrid");
      SimpleConfig.setProperty("datacenter.description", "An unconfigured datacenter; it contains two tables of sample stars and galaxies for testing and demonstration purposes.");
      SimpleConfig.setProperty("datacenter.contact.name", "Martin Hill");
      SimpleConfig.setProperty("datacenter.contact.email", "mch@roe.ac.uk");

      SimpleConfig.setProperty("datacenter.authorityId", "astrogrid.org");
      SimpleConfig.setProperty("datacenter.resourceKey", "pal-sample");

   }
   
}


