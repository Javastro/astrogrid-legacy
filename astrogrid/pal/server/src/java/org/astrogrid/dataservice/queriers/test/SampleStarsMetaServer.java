/*
 * $Id: SampleStarsMetaServer.java,v 1.3 2005/03/10 13:49:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.test;

import java.io.IOException;
import java.net.URL;
import org.astrogrid.config.Config;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.FileResourcePlugin;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.tables.TableMetaDocInterpreter;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.dataservice.queriers.sql.RdbmsMetadataResources;
import org.astrogrid.dataservice.queriers.sql.TabularDbResources;
import org.astrogrid.dataservice.queriers.sql.TabularSkyServiceResources;

/**
 * A special file-serving metadata server; it checks that the SampleStarsPlugin
 * is the current plugin and throws an exception if not, so that we don't get
 * incompatible metadata vs plugin
 * <p>
 * @author M Hill
 */

public class SampleStarsMetaServer extends FileResourcePlugin
{
   
   /**
    * Initialises config so that authority ID, etc are set.
      bear in mind that this is only called when SampleStarsPlugin is called,
      so getmetadata may fail if called beforehand
    */
   public static void initConfig() {

      //set where to find the data description meta document
      //this works OK for unit test, but not deployment...
      URL url = SampleStarsMetaServer.class.getResource("samplestars.metadoc.xml");
      if (url == null) {
         //this works OK for deployment, but not unit tests...
         try {
            url = Config.resolveFilename("samplestars.metadoc.xml");
         }
         catch (IOException e) {
            throw new RuntimeException(e);
         }
      }
      SimpleConfig.setProperty(TableMetaDocInterpreter.TABLE_METADOC_KEY, url.toString());

      //configure which resources to produce
      SimpleConfig.getSingleton().setProperties(VoDescriptionServer.RESOURCE_PLUGIN_KEY, new Object[] {
               TabularSkyServiceResources.class.getName(),
               RdbmsMetadataResources.class.getName(),
               TabularDbResources.class.getName(),
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


