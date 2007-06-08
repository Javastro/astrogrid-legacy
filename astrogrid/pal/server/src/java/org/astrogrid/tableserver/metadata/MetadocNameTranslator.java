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
}


