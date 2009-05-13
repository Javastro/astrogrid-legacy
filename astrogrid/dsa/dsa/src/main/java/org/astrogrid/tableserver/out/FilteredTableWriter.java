/*
 * $Id: FilteredTableWriter.java,v 1.1.1.1 2009/05/13 13:20:50 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;

import org.astrogrid.tableserver.metadata.*;

import java.io.IOException;

/**
 * Similar to FilteredInputStream, an instance of this class 'wraps' around another
 * table writer, and forwards all calls to that writer.  Subclasses therefore can
 * override particular calls to transform the information on the way through.
 *
 * For example, the value of a particular column might be altered, or new calculated
 * columns added
 *
 * @author M Hill
 */

public abstract class FilteredTableWriter {

   protected TableWriter writer;
   
   
   protected FilteredTableWriter(TableWriter targetWriter) {
      this.writer = writer;
   }
   
   /** Sets all the column details  */
   public void startTable(ColumnInfo[] colInfo) throws IOException {
      writer.startTable(colInfo);
   }

   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException {
      writer.writeRow(colValues);
   }

   /** Ends a table (eg closes tags)  */
   public void endTable() throws IOException {
      writer.endTable();
   }
   
   /** Close the writer */
   public void close() throws IOException {
      writer.close();
   }
   
   /** If the writer needs to stop before normal completion, call this.  It will,
    * if appropriate, write some message to indicate that the table is incomplete */
   public void abort() throws IOException {
      writer.abort();
   }
   
   /** Returns the mime type that this writer produces */
   public String getMimeType() {
      return writer.getMimeType();
   }
}


