/*
 * $Id: TableWriter.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.out.tables;
import org.astrogrid.dataservice.metadata.tables.*;

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

/*
 $Log: TableWriter.java,v $
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.4  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.1.2.3  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.2  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.1.2.1  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.1.2.1  2004/11/25 08:29:41  mch
 added table writers modelled on STIL


 */







