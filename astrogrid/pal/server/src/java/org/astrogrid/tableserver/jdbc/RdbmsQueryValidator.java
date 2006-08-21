/*
 * $Id: RdbmsQueryValidator.java,v 1.4 2006/08/21 15:39:30 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.query.Query;
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
   TableMetaDocInterpreter interpreter = null;

   public RdbmsQueryValidator(TableMetaDocInterpreter reader)  {
      this.interpreter = reader;
   }
  
   public void validateQuery(Query query)
   {
      String[] tableRefs = query.getTableReferences();
      for (int i = 0; i < tableRefs.length; i++) {
         String[] catalogNames = interpreter.getCatalogs();
         if (catalogNames.length == 0) {
            throw new IllegalArgumentException("Server error: no catalog or table metadata are defined for this DSA/catalog installation;  please check your metadoc file and/or configuration!");
         }
         if (interpreter.getTable(
               catalogNames[0], tableRefs[i]) ==null) {
            throw new IllegalArgumentException( "Table '"+tableRefs[i]+
                "' is not available in this DSA/catalog installation.");
         }
         String[] columnRefs = query.getColumnReferences(tableRefs[i]);
         for (int j = 0; j < columnRefs.length; j++) {
            try {
               if (interpreter.getColumn(
                     catalogNames[0],
                     tableRefs[i], columnRefs[j])   == null) {
                  throw new IllegalArgumentException("Column "+
                      columnRefs[j] +" in table "+
                      tableRefs[i] +
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
