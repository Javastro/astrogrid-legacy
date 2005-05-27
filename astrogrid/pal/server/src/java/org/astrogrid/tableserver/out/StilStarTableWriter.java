/*
 * $Id: StilStarTableWriter.java,v 1.4 2005/05/27 16:21:02 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import org.astrogrid.io.mime.MimeNames;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import uk.ac.starlink.table.AbstractStarTable;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTableWriter;

/**
 * Adaptor to the STIL (Starlink Library) StarTableWriter,
 * which can then be used to create fits files, votables, etc.
 * However it's a non streaming build (the whole table is built) so use with care
 * <p>
 * An alternative, if there is a suitable StarTable representation of the data
 * source, is to just create the appropriate StarTableWriter around the appropriate StarTable
 * (essentially a table reader) and write it out.
 *
 * @author M Hill
 */

public class StilStarTableWriter extends AbstractStarTable implements TableWriter {
   
   StarTableWriter starWriter = null;
// RowListStarTable starTable = null;
   OutputStream out = null;

   ColumnInfo[] cols = null;
   Vector rows = new Vector();
   
   /**
    * Constructor for a table with the given anme
    */
   public StilStarTableWriter(StarTableWriter writer, OutputStream target) throws IOException {
      starWriter = writer;
      this.out = target;
   }
   
   /** Opens writer = does nothing */
   public void open() {
   }
   
   /** Closes writer - does nothing
    */
   public void close() throws IOException {
   }
   
   /** try and return mime type from starWriters' format name */
   public String getMimeType() {
      return MimeNames.getMimeType(starWriter.getFormatName());
   }

   /** Starts table - stores column info for write */
   public void startTable(ColumnInfo[] newCols) throws IOException {
      cols = newCols;
   }
   
   
   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException {
      rows.add(colValues);
   }

   public void endTable() throws IOException {
      throw new UnsupportedOperationException();
//      starWriter.writeStarTable(this, out);
   }

   
   /** Aborting does nothing as nothing happens until endtable anyway */
   public void abort() {
   }
   
   /**
    * StarTable implementation, returns the number of rows built
    */
   public long getRowCount() {
      return rows.size();
   }
   
   /**
    * StarTable implementation, returns the number of columns stored
    */
   public int getColumnCount() {
      return cols.length;
   }
   
   /**
    * StarTable implementation, returns the columinfo suitable for startable
    */
   public uk.ac.starlink.table.ColumnInfo getColumnInfo(int icol) {
      uk.ac.starlink.table.ColumnInfo starColumn = new uk.ac.starlink.table.ColumnInfo(cols[icol].getName());
      starColumn.setUnitString(cols[icol].getUnits().toString());
      starColumn.setUCD(cols[icol].getUcd("1"));
      starColumn.setContentClass(cols[icol].getJavaType());
      return starColumn;
   }
   
   /**
    * StarTable implementation, returns access to the rows
    */
   public RowSequence getRowSequence() throws IOException {
      return new RowAdaptor();
   }
   
   private class RowAdaptor implements RowSequence {
   
      int rowCursor = -1;
      /**
       * Indicates whether this table contains any more rows after the current
       * one.
       */
      public boolean hasNext() {
         return (rowCursor<rows.size());
      }
      
      /**
       * Returns the contents of the current table row, as an array
       * with the same number of elements as there are columns in this
       * table.
       * An unchecked exception will be thrown if there is no current
       * row (<tt>next</tt> has not yet been called).
       *
       * @return  an array of the objects in each cell in row <tt>irow</tt>
       * @throws  IOException if there is an error reading the data
       */
      public Object[] getRow() throws IOException {
         if (rowCursor == -1) {
            throw new IllegalStateException("Next not yet called");
         }
         return (Object[]) rows.get(rowCursor);
      }
      
      /**
       * Indicates that this sequence will not be required any more.
       * This should release resources associated with this object.
       * The effect of calling any of the other methods following a
       * <code>close</code> is undefined.
       */
      public void close() throws IOException {
         cols = null;
         rows = null;
      }
      
      /**
       * Advances the current row to the next one.
       * Since the initial position of a RowSequence is before the first row,
       * this method must be called before current row data
       * can be accessed using the
       * {@link #getCell(int)} or {@link #getRow()} methods.
       * An unchecked exception such as <tt>NoSuchElementException</tt>
       * will be thrown if {@link #hasNext} returns <tt>false</tt>.
       *
       * @throws  IOException if there is some error in the positioning
       */
      public void next() throws IOException {
         rowCursor++;
      }
      
      /**
       * Returns the contents of a cell in the current row.
       * The class of the returned object should be the same as,
       * or a subclass of, the class returned by
       * <tt>getColumnInfo(icol).getContentClass()</tt>.
       * An unchecked exception will be thrown if there is no current
       * row (<tt>next</tt> has not yet been called).
       *
       * @return  the contents of cell <tt>icol</tt> in the current row
       * @throws IOException  if there is an error reading the data
       * @throws IllegalStateException if there is no current row (before the
       *         start of the table)
       */
      public Object getCell(int icol) throws IOException {
         return getRow()[icol];
      }
      
   }
   
}

/*
 $Log: StilStarTableWriter.java,v $
 Revision 1.4  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.3.10.2  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.3.10.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.3  2005/03/30 19:01:08  mch
 fixes to results format

 Revision 1.2  2005/03/30 18:25:45  mch
 fix for sql-server jdbc problem

 Revision 1.1  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.3  2005/03/10 15:13:48  mch
 Seperating out fits, table and xdb servers

 Revision 1.2  2005/03/10 13:49:52  mch
 Updating metadata

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.3  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.1.2.2  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.1  2004/12/03 11:54:52  mch
 added adaptor to STIL table-writing library

 Revision 1.1.2.3  2004/11/30 02:32:18  mch
 fix to 0-base of writerows

 Revision 1.1.2.2  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.1.2.1  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.1.2.1  2004/11/25 08:29:41  mch
 added table writers modelled on STIL


 */







