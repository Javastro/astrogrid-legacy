/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.metadata;

import org.astrogrid.dataservice.metadata.MetadataException;

/**
 * Defines the methods a name translator must implement.  
 *
 * Name translators provide functionality to convert the published catalog,
 * table and column names in the registry to local (system-specific)
 * database, table and column names.  This allows a level of indirection
 * in describing resources, allowing for descriptions in terms of datamodels
 * etc.
 *
 * @author K Andrews
 */

public interface NameTranslator {
   
   /** Gets the database name for the specified catalog */
   public String getCatalogRealname(String catalog) throws MetadataException;

   /** Gets the database name for the specified catalog */
   public String getTableRealname(String catalog, String table) throws MetadataException;

   /** Gets the database name for the specified catalog */
   public String getColumnRealname(String catalog, String table, String column) throws MetadataException;

   /** Returns the number of catalogs in the metadoc - useful for finding
    *  out if this DSA is running in multi-catalog mode. */
   public int getNumCatalogs() throws MetadataException;
}


