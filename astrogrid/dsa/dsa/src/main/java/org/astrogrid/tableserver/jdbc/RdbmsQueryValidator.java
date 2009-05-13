/*
 * $Id: RdbmsQueryValidator.java,v 1.1 2009/05/13 13:20:43 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
//import org.astrogrid.query.DefaultQueryTraverser;
//import org.astrogrid.query.condition.ColumnReference;


/**
 * Checks the query against the rdbms resource
 *
 * @author M Hill
 * @author K Andrews
 */

public class RdbmsQueryValidator 
{
   //TableMetaDocInterpreter interpreter = null;

   /*
   public RdbmsQueryValidator(TableMetaDocInterpreter reader)  {
      this.interpreter = reader;
   }
   */
   public void validateQuery(Query query)
   {
      try {
         // Extract table refs from source query, and parent catalog refs
         // for those tables (NB (some of) the parent catalog refs might 
         // be null)
         String[] tableRefs = query.getTableReferences();
         String[] parentRefs = query.getParentCatalogReferences();

         // Extract catalog names from metadata in case they're needed
         String[] catalogNames;
         try {
            catalogNames = TableMetaDocInterpreter.getCatalogNames();
         }
         catch (MetadataException e) {
            throw new IllegalArgumentException("Server error: metadata is invalid: " + e.toString());
         }
         if (catalogNames.length == 0) {
            throw new IllegalArgumentException("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!");
         }
         String defaultCatName = "";
         boolean insertDefaultCat = false;

         for (int i = 0; i < tableRefs.length; i++) {
            String tableName = tableRefs[i];
            String catalogName = parentRefs[i];
            // Strip any catalog prefix off table name, just in case
            int dotIndex = tableName.lastIndexOf(".");
            if (dotIndex > 0) {
               tableName = tableName.substring(dotIndex+1);
            }
            try {
               //If we have a catalog name, validate directly
               if ((catalogName != null) && (!"".equals(catalogName))) {
                  if (TableMetaDocInterpreter.getTableInfoByName(
                       catalogName, tableName) == null) {
                     throw new IllegalArgumentException( "Table '"
                       +tableRefs[i]+ "' is not supplied in catalog '" + 
                       catalogName +
                    "' in this DSA/catalog installation.");
                  }
               }   
               else {
                  // No catalog name - default to first catalogue, barf
                  // if table not found
                  boolean found = false;
                  try {
                     if (TableMetaDocInterpreter.getTableInfoByName(
                          catalogNames[0], tableName) != null) {
                        catalogName = catalogNames[0];
                        found = true;
                        defaultCatName = catalogName;
                        insertDefaultCat = true;
                     }
                  }
                  catch (MetadataException me) {
                     throw new IllegalArgumentException( "Table '"
                       +tableRefs[i]+ "' is not found in default catalog with name '" + catalogNames[0] + "', please specify which catalog to use in your query.");
                  }
                  if (!found) {
                     throw new IllegalArgumentException( "Table '"
                       +tableRefs[i]+ "' is not found in default catalog with name '" + catalogName + "', please specify which catalog to use in your query.");
                  }
               }
           }
           catch (MetadataException e) {
              throw new IllegalArgumentException("Problem with query: " + e.getMessage());
           }
            // Once here, catalogName is set
           String[] columnRefs = query.getColumnReferences(tableRefs[i]);
           for (int j = 0; j < columnRefs.length; j++) {
              try {
                  if (TableMetaDocInterpreter.getColumnInfoByName(
                        catalogName,
                        tableName, columnRefs[j]) == null) {
                     throw new IllegalArgumentException("Column "+
                         columnRefs[j] +" in table "+ tableRefs[i] +
                         " in catalog " + catalogName +
                         " is not available in this DSA/catalog installation");
                  }
              } 
              catch (MetadataException me) {
                throw new IllegalArgumentException("Problem with query: " + me.getMessage());
              }
            }
         }
         if (insertDefaultCat == true) {
           query.setParentCatalogReferences(defaultCatName);
         }
      }
      catch (QueryException qe) {
         throw new IllegalArgumentException("Couldn't validate query: " + qe.getMessage());
      }
   }
}
