/*
 * $Id: RdbmsQueryValidator.java,v 1.6 2007/06/08 13:16:12 clq2 Exp $
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
         //
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

         for (int i = 0; i < tableRefs.length; i++) {
            String tableName = tableRefs[i];
            String catalogName = parentRefs[i];
            for (int k = 0; k < catalogNames.length; k++) {
               if ((catalogName == null) 
                     || catalogName.equals(catalogNames[k])) {
                  try {
                     if (TableMetaDocInterpreter.getTableInfoByName(
                          catalogNames[k], tableName) == null) {
                        if (catalogName == null) { 
                           catalogName = ""; 
                        }
                        throw new IllegalArgumentException( "Table '"
                          +tableRefs[i]+ "' from catalog '" + catalogName +
                       "' is not available in this DSA/catalog installation.");
                    }
                  }
                  catch (MetadataException e) {
                     throw new IllegalArgumentException("Server error: metadata is invalid: " + e.toString());
                  }
                  String[] columnRefs = query.getColumnReferences(tableRefs[i]);
                  for (int j = 0; j < columnRefs.length; j++) {
                     try {
                        if (TableMetaDocInterpreter.getColumnInfoByName(
                              catalogNames[k],
                              tableName, columnRefs[j]) == null) {
                           throw new IllegalArgumentException("Column "+
                               columnRefs[j] +" in table "+ tableRefs[i] +
                               " is not available in this DSA/catalog installation");
                        }
                     } 
                     catch (MetadataException me) {
                        throw new IllegalArgumentException( "Couldn't validate query, DSA metadata appears to be misconfigured :" + me.getMessage());
                     }
                  }
               }
            }
         }
      }
      catch (QueryException qe) {
         throw new IllegalArgumentException("Couldn't validate query: " + qe.getMessage());
      }
   }
}
