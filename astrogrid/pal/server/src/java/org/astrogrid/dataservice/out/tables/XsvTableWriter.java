/*
 * $Id: XsvTableWriter.java,v 1.2 2005/03/10 13:49:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.out.tables;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.astrogrid.dataservice.metadata.tables.ColumnInfo;
import org.astrogrid.slinger.mime.MimeTypes;

/**
 * For writing out tables in something-separated form, eg comma separated or
 * tab separated
 *
 * @author M Hill
 */

public class XsvTableWriter extends AsciiTableSupport {
   
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(HtmlTableWriter.class);
   
   PrintWriter printOut = null;
   
   String separator = ",";
   
   /**
    * Construct this wrapping the given stream.  Values on a line will be separated with the given string
    */
   public XsvTableWriter(Writer target, String title, String variableSeparator) throws IOException {
      printOut = new PrintWriter(new BufferedWriter(target));
      separator = variableSeparator;
   }
   
   /** Closes writer
    */
   public void close() {
      printOut.close();
   }
   
   /** Produces text/html */
   public String getMimeType() {
      if (separator.trim().equals(",")) {
         return MimeTypes.CSV;
      }
      else if (separator.equals("\t")) {
         return MimeTypes.TSV;
      }
      else {
         return MimeTypes.PLAINTEXT;
      }
   }
   
   public void startTable(ColumnInfo[] cols) throws IOException {
      
      //column names, line 1
      for (int i = 0; i < cols.length; i++) {
         if (cols[i] == null) {
            throw new IllegalArgumentException("No information for column "+i);
         }
         printOut.print(cols[i].getName());
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
      printOut.println();
      
      //ucds, line 2
      for (int i = 0; i < cols.length; i++) {
         printOut.print(cols[i].getUcd("1"));
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
      printOut.println();
      
      //units, line 3
      for (int i = 0; i < cols.length; i++) {
         printOut.print(cols[i].getUnits());
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
 
      //types
      for (int i = 0; i < cols.length; i++) {
         printOut.print(cols[i].getDatatype());
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
     
      
      printOut.println();//new line
   }
   
   
   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException {
      
      for (int i=0;i<colValues.length;i++) {
         if (colValues[i] instanceof Date) {
            printOut.print(dateFormat.format(colValues[i]));
         }
         else {
            printOut.print(colValues[i]);
         }
         if (i < colValues.length-1) { printOut.print(separator);    }
      }
      printOut.println();
      
   }

   /** Does nothing */
   public void endTable() {
      
   }
   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() {
      printOut.println(" ------------------ Writing Aborted ----------------- ");
      close();
   }
   
   
}

/*
 $Log: XsvTableWriter.java,v $
 Revision 1.2  2005/03/10 13:49:52  mch
 Updating metadata

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.7  2005/01/24 12:14:28  mch
 Fixes to VizieR proxy and resource stuff

 Revision 1.1.2.6  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.1.2.5  2004/12/13 21:53:14  mch
 Made the java types the intermediate types, added types to Xsv and html output

 Revision 1.1.2.4  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.3  2004/12/06 02:50:30  mch
 a few bug fixes

 Revision 1.1.2.2  2004/11/30 02:32:18  mch
 fix to 0-base of writerows

 Revision 1.1.2.1  2004/11/30 01:26:42  mch
 added tablewriters

 Revision 1.1.2.1  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.1.2.1  2004/11/25 08:29:41  mch
 added table writers modelled on STIL


 */







