/*
 * $Id: TableWriter.java,v 1.2 2005/05/27 16:21:02 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;

import org.astrogrid.tableserver.metadata.*;

import java.io.IOException;

/**
 * Defines the methods a table writer must implement...
 *
 * @author M Hill
 */

public interface TableWriter {
   
   /** Sets the details of a column. Fails if col is bigger than the existing internal
   * array; use setColumnMetadata(array) to set the size */
//   public void setColumnMetadata(int col, ColumnInfo columnInfo) throws IOException;

   /** Starts the writer */
   public void open() throws IOException;
   
   /** Sets all the column details  */
   public void startTable(ColumnInfo[] colInfo) throws IOException;

   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException;

   /** Ends a table (eg closes tags)  */
   public void endTable() throws IOException;
   
   /** Close the writer */
   public void close() throws IOException;
   
   /** If the writer needs to stop before normal completion, call this.  It will,
    * if appropriate, write some message to indicate that the table is incomplete */
   public void abort() throws IOException;
   
   /** Returns the mime type that this writer produces */
   public String getMimeType();
}


