/*
 * $Id: ConeConfigQueryableResource.java,v 1.5 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.queryable;
import java.io.IOException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

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
         SearchField[] fields = {
            getColumn(getCatalogs()[0], parent.getName(), ConfigFactory.getCommonConfig().getString(CONE_SEARCH_RA_COL_KEY)),
            getColumn(getCatalogs()[0], parent.getName(), ConfigFactory.getCommonConfig().getString(CONE_SEARCH_DEC_COL_KEY))
         };
         return fields;
      }
      else {
         return null;
      }
   }
}


