/*
 * $Id: TabularResourceReader.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata.tables;

import java.io.IOException;

/**
 * Things that represent tabular data can present their metadata through this
 * interface.  Publishing the whole thing may be too large for aggregate systems
 * (eg Vizier) so this allows us to ask for a bit at a time, suitably eg for
 * query builders.
 */

public interface TabularResourceReader {
   
   public TableInfo getTable(String datasetId, String table) throws IOException ;

   public ColumnInfo getColumn(String datasetId, String tableId, String column) throws IOException;
   
   /** Used by SQL results where only the column name is known. Returns the element
    * corresponding to the only matching column, if there are several or none
    * throws an exception*/
   public ColumnInfo guessColumn(String[] scope, String column) throws IOException;

   public TableInfo[] getTables(String datasetId) throws IOException;
   
   public ColumnInfo[] getColumns(String datasetId, String tableName)  throws IOException;

}


