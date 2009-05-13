/*
 * $Id: XsvTableWriter.java,v 1.1 2009/05/13 13:20:52 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.ColumnInfo;

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

   protected int rowsWritten = 0;

   // How many rows to write before we check that the output stream is OK
   protected static final int CHECKFREQUENCY = 1000;
   
   /**
    * Construct this wrapping the given stream.  Values on a line will be separated with the given string
    */
   public XsvTableWriter(TargetIdentifier target, String title, String variableSeperator, Principal user) throws IOException {
      target.setMimeType(getMimeType());
      printOut = new PrintWriter(new BufferedWriter(target.openWriter()));
      separator = variableSeperator;
   }
   
   public void writeErrorTable(String errorDesc) throws IOException {
      printOut.print("ERROR: " + errorDesc);
   }

   public void open() {
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
         if (cols[i].getUcd("1") != null) {
            printOut.print(cols[i].getUcd("1"));
         }
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
      printOut.println();
      
      //units, line 3
      for (int i = 0; i < cols.length; i++) {
         if (cols[i].getUnits() != null) {
            printOut.print(cols[i].getUnits().toString());
         }
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
 
      printOut.println();

      //types
      for (int i = 0; i < cols.length; i++) {
         printOut.print(cols[i].getPublicType());
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
      printOut.println();
      for (int i = 0; i < cols.length; i++) {
         if (cols[i].getJavaType() != null) {
            printOut.print(cols[i].getJavaType().getName());
         }
         if (i < cols.length-1) {
            printOut.print(separator);
         }
      }
      
      printOut.println();//new line

      // Make sure stream is still ok
      checkErrors();
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
      
      rowsWritten = rowsWritten + 1;
      if (rowsWritten % CHECKFREQUENCY == 0) {
         // Make sure stream is still ok
         checkErrors();
      }
   }

   /** Does nothing */
   public void endTable() throws IOException {
     // Make sure stream is still ok
     checkErrors();
   }
   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() {
      printOut.println(" ------------------ Writing Aborted ----------------- ");
      close();
   }
   

   /** Method to check status of output stream - PrintWriters do NOT
      throw exceptions on write errors! */
   protected void checkErrors() throws IOException
   {
      boolean gotError = printOut.checkError();
      if (gotError) {
        // Unfortunately, no way to get at the cause of the error :-/
        log.error("Transfer of results to output stream failed");
        printOut.close();
        throw new IOException("Transfer of XSVTable results failed to complete successfully");
      }
   }
   
}

/*
 $Log: XsvTableWriter.java,v $
 Revision 1.1  2009/05/13 13:20:52  gtr
 *** empty log message ***

 Revision 1.10  2008/04/02 14:20:44  clq2
 KEA_PAL2654

 Revision 1.9.32.1  2008/03/31 17:15:38  kea
 Fixes for conesearch error reporting.

 Revision 1.9  2007/03/02 13:43:45  kea
 Added proper error checking to PrintWriter output stream writers in these
 classes;  failures were going undetected as PrintWriters do not throw
 exceptions.  See bugzilla bug 2139.

 Revision 1.8  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.7.8.2  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.7.8.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.7  2005/03/31 15:06:16  mch
 Fixes and workarounds for null values, misisng metadoc columns

 Revision 1.6  2005/03/31 12:10:28  mch
 Fixes and workarounds for null values, misisng metadoc columns

 Revision 1.5  2005/03/30 21:51:25  mch
 Fix to return Votable fits list for url list

 Revision 1.4  2005/03/30 18:25:45  mch
 fix for sql-server jdbc problem

 Revision 1.3  2005/03/30 15:52:15  mch
 debug etc for bad sql types

 Revision 1.2  2005/03/30 15:18:55  mch
 debug etc for bad sql types

 Revision 1.1  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.4  2005/03/10 22:39:17  mch
 Fixed tests more metadata fixes

 Revision 1.3  2005/03/10 15:13:48  mch
 Seperating out fits, table and xdb servers

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









