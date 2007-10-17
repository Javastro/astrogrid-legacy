/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata;

import org.astrogrid.dataservice.metadata.MetadataException;

/**
 * Implements the default NameTranslator used with DSA metadoc files.
 *
 * Name translators provide functionality to convert the published catalog, 
 * table and column names in the registry to local (system-specific) 
 * database, table and column names.  This allows a level of indirection 
 * in describing resources, allowing for descriptions in terms of datamodels 
 * etc.
 *
 * @author K Andrews
 */

public class MetadocNameTranslator implements NameTranslator {
   
   /** Gets the database name for the specified catalog */
   public String getCatalogRealname(String catalog) 
         throws MetadataException
   {
      return TableMetaDocInterpreter.getCatalogIDForName(catalog);
   }

   /** Gets the database name for the specified table */
   public String getTableRealname(String catalog, String table) 
         throws MetadataException
   {
      return TableMetaDocInterpreter.getTableIDForName(catalog, table);
   }

   /** Gets the database name for the specified column */
   public String getColumnRealname(String catalog, String table, String column) 
         throws MetadataException
   {
      return TableMetaDocInterpreter.getColumnIDForName(catalog, table, column);
   }

   /** Returns the number of catalogs in the metadoc - useful for finding
    * out if this DSA is running in multi-catalog mode. */
   public int getNumCatalogs() throws MetadataException {
      String catalogIDs[] = TableMetaDocInterpreter.getCatalogIDs();
      if (catalogIDs == null) { // Shouldn't happen
         return 0;
      }
      else {
         return catalogIDs.length;
      }
   }
}


