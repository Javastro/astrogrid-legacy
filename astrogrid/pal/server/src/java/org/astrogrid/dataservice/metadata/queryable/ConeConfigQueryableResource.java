/*
 * $Id: ConeConfigQueryableResource.java,v 1.7 2006/09/26 15:34:42 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.queryable;
import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;

/**
 * An implementation of QueryableResourceReader that looks in the config file
 * for what to search on for cones
 */

public class ConeConfigQueryableResource extends TableMetaDocInterpreter {
   
   /** Key used to look up the column containing RA for cone searches */
   public static final String CONE_SEARCH_TABLE_KEY = "conesearch.table";
   public static final String CONE_SEARCH_RA_COL_KEY = "conesearch.ra.column";
   public static final String CONE_SEARCH_DEC_COL_KEY = "conesearch.dec.column";

   /** A temporary key specifying whether the db columns asre in degrees or radians */
   public static final String CONE_SEARCH_COL_UNITS_KEY = "conesearch.columns.units";
   
   public ConeConfigQueryableResource() throws IOException {
      super();
      try {
         // Initialise SampleStars plugin if required (may not be initialised
         // if admin has not run the self-tests)
         String plugin = ConfigFactory.getCommonConfig().getString(
              "datacenter.querier.plugin");
         if (plugin.equals(
               "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
           // This has no effect if the plugin is already initialised
            SampleStarsPlugin.initialise();  // Just in case
         }
      }
      catch (DatabaseAccessException dbe) {
        throw new IOException(dbe.getMessage());
      }
   }
   
   
   /** Special case for spatial searches.  Returns the groups that contain
    * fields suitable for spatial searching */
   public SearchGroup[] getSpatialGroups()
   {
      String configTable = ConfigFactory.getCommonConfig().getString(CONE_SEARCH_TABLE_KEY);
      SearchGroup table = new TableInfo();
      table.setId(configTable);
      table.setName(configTable);
      return new SearchGroup[] { table };
      
   }
   
   /** botch botch botch */
   public SearchField[] getSpatialFields(SearchGroup parent) throws IOException
   {
      if (parent.getId().equals(ConfigFactory.getCommonConfig().getString(CONE_SEARCH_TABLE_KEY))) {
         //get which columns given RA & DEC for cone searches
         String[] catalogNames = getCatalogs();
         if (catalogNames.length == 0) {
           throw new MetadataException("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!");
         }
         // Got at least one catalog (NB only one expected at present)
         SearchField[] fields = {
            getColumn(catalogNames[0], parent.getName(), ConfigFactory.getCommonConfig().getString(CONE_SEARCH_RA_COL_KEY)),
            getColumn(catalogNames[0], parent.getName(), ConfigFactory.getCommonConfig().getString(CONE_SEARCH_DEC_COL_KEY))
         };
         return fields;
      }
      else {
         return null;
      }
   }
}


