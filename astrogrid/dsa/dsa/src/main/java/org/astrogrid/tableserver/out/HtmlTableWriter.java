/*
 * $Id: HtmlTableWriter.java,v 1.1.1.1 2009/05/13 13:20:51 gtr Exp $
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
 * For writing out tables in html form
 *
 * @author M Hill
 */

public class HtmlTableWriter extends AsciiTableSupport {
   
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(HtmlTableWriter.class);
   
   PrintWriter printOut = null;
   
   long rows = 0;
   
   ColumnInfo[] cols = null;
   
   String title = null;
   String comment = null;

   protected int rowsWritten = 0;

   // How many rows to write before we check that the output stream is OK
   protected static final int CHECKFREQUENCY = 1000;
   
   /**
    * Construct this wrapping the given stream.  Writes out the first few tags
    */
   public HtmlTableWriter(TargetIdentifier target, String aTitle, String aComment, Principal user) throws IOException {
      
      target.setMimeType(MimeTypes.HTML);
      
      printOut = new PrintWriter(new BufferedWriter(target.openWriter()));
      
      this.title = aTitle;
      this.comment = aComment;
   }
   
   public void writeErrorTable(String errorDesc) throws IOException {
      printOut.println("<HTML>");
      printOut.println("<HEAD>");
      printOut.println("<TITLE>ERROR</TITLE>");
      printOut.println("</HEAD>");
      printOut.println("<BODY>");
      printOut.println("<H1>Error</H1>");
      printOut.println("<p>" + errorDesc + "</p>");
      printOut.println("</BODY>");
      printOut.println("</HTML>");
   }

   public void open() throws IOException {
      printOut.println("<HTML>");
      
      printOut.println("<HEAD>");
      printOut.println("<TITLE>"+title+"</TITLE>");
      printOut.println("</HEAD>");
      
      printOut.println("<BODY>");
      printOut.println("<H1>"+title+"</H1>");
      /*
      if (comment != null) {
         printOut.println("<P>"+comment+"</P>");
      }
      */
      // Make sure stream is still ok
      checkErrors();
   }
   
   /** Produces text/html */
   public String getMimeType() {
      return MimeTypes.HTML;
   }
   
   public void startTable(ColumnInfo[] givenCols) throws IOException {

      cols = givenCols; //remember column info for printing rows
      
      printOut.println("<TABLE border='1'>");  //it's wrong to put formatting in here, but the default seems to be 0
      
      printOut.println("<TR>");
      printOut.println("<TH>Name</TH>");
      for (int i = 0; i < cols.length; i++) {
         if (cols[i] == null) {
            throw new IllegalArgumentException("No information for column "+i);
         }
         String tabName = cols[i].getGroupName();
         if (tabName == null) { 
            tabName = "UNKNOWN"; 
         }
         String catName = cols[i].getParentName();
         if (catName == null) { 
            catName = "UNKNOWN"; 
         }
         //printOut.print("<TH>"+cols[i].getId()+"</TH>");
         printOut.print("<TH align='left'>Catalog:&nbsp;"+catName+
               "<br/>Table:&nbsp;"+tabName+
               "<br/>Column:&nbsp;"+cols[i].getName()+"</TH>");
      }
      printOut.println("</TR>");

      printOut.println("<TR>");
      printOut.println("<TH>Description</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+cols[i].getDescription()+"</TH>");
      }
      printOut.println("<TR>");

      printOut.println("<TR>");
      printOut.println("<TH>Public Type</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+cols[i].getPublicType()+"</TH>");
      }
      printOut.println("<TR>");

      printOut.println("<TR>");
      printOut.println("<TH>Java Class</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>");
         if (cols[i].getJavaType() != null) {
            printOut.print(cols[i].getJavaType().getName());
         }
         printOut.print("</TH>");
      }
      printOut.println("<TR>");

      printOut.println("<TR>");
      printOut.println("<TH>Native Type</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+cols[i].getBackType()+"</TH>");
      }
      printOut.println("</TR>");

      printOut.println("<TR>");
      printOut.println("<TH>UCD</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+emptyIfNull(cols[i].getUcd("1"))+"</TH>");
      }
      printOut.println("</TR>");

      printOut.println("<TR>");
      printOut.println("<TH>Units</TH>");
      for (int i = 0; i < cols.length; i++) {
         if (cols[i].getUnits() == null) {
            printOut.print("<TH/>");
         }
         else {
            printOut.print("<TH>"+cols[i].getUnits().toString()+"</TH>");
         }
      }
      printOut.println("</TR>");

      rows = 0;

      // Make sure stream is still ok
      checkErrors();
   }
   
   
   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException {
      
      rows++;
      printOut.println("<TR>");
      printOut.println("<TH>"+rows+"</TH>");
      String align = null;
      for (int i=0;i<colValues.length;i++) {
         align="";
         if (cols[i].getJavaType() == Date.class) {
            printOut.print("<TD align='right'>");
            try {
               printOut.print(dateFormat.format(colValues[i]));
            }
            catch (IllegalArgumentException iae) {
               printOut.print(colValues[i]+" ["+iae.toString()+"]");
            }
            printOut.println("</TD>");
         }
         else {
            if (cols[i].getJavaType() != null) {
               if (Number.class.isAssignableFrom(cols[i].getJavaType())) {
                  align = "align='right'";
               }
               else {
                  align = "align='left'";
               }
            }
            printOut.println("<TD "+align+">"+colValues[i]+"</TD>");
         }
      }
      printOut.println("</TR>");
      
      rowsWritten = rowsWritten + 1;
      if (rowsWritten % CHECKFREQUENCY == 0) {
         // Make sure stream is still ok
         checkErrors();
      }
   }

   public void endTable() throws IOException {
      printOut.println("</TABLE>");
      printOut.println("<p>"+rows+" Rows</p>");

      // Make sure stream is still ok
      checkErrors();
   }

   /** Closes writer - writes out the closing tags and closes wrapped stream
    */
   public void close() throws IOException {

      if (comment != null) {
         printOut.println("<P>"+comment+"</P>");
      }
      
      printOut.println("</BODY>");
      
      printOut.println("</HTML>");

      // Make sure stream is still ok
      checkErrors();

      printOut.close();
   }
   
   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() throws IOException {
      printOut.println("<tr><td> ------------------ Writing Aborted -----------------</td></tr> ");
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
        throw new IOException("Transfer of HTMLTable results failed to complete successfully");
      }
   }
}

/*
 $Log: HtmlTableWriter.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:51  gtr


 Revision 1.11  2008/05/27 11:07:38  clq2
 merged PAL_KEA_2715

 Revision 1.10.2.1  2008/05/01 10:52:54  kea
 Fixes relating to:  BZ2127 BZ2657 BZ2720 BZ2721

 Revision 1.10  2008/04/02 14:20:44  clq2
 KEA_PAL2654

 Revision 1.9.22.1  2008/03/31 17:15:38  kea
 Fixes for conesearch error reporting.

 Revision 1.9  2007/09/07 09:30:52  clq2
 PAL_KEA_2235

 Revision 1.8.6.1  2007/08/10 14:13:29  kea
 Work-in-progress update.

 Revision 1.8  2007/03/02 13:43:45  kea
 Added proper error checking to PrintWriter output stream writers in these
 classes;  failures were going undetected as PrintWriters do not throw
 exceptions.  See bugzilla bug 2139.

 Revision 1.7  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.6.8.2  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.6.8.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.6  2005/03/31 15:06:16  mch
 Fixes and workarounds for null values, misisng metadoc columns

 Revision 1.5  2005/03/31 12:10:28  mch
 Fixes and workarounds for null values, misisng metadoc columns

 Revision 1.4  2005/03/30 21:51:25  mch
 Fix to return Votable fits list for url list

 Revision 1.3  2005/03/30 15:52:15  mch
 debug etc for bad sql types

 Revision 1.2  2005/03/30 15:18:55  mch
 debug etc for bad sql types

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

 Revision 1.1.2.8  2005/01/24 12:14:27  mch
 Fixes to VizieR proxy and resource stuff

 Revision 1.1.2.7  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.1.2.6  2004/12/13 21:53:14  mch
 Made the java types the intermediate types, added types to Xsv and html output

 Revision 1.1.2.5  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.4  2004/12/06 02:50:30  mch
 a few bug fixes

 Revision 1.1.2.3  2004/11/30 02:32:18  mch
 fix to 0-base of writerows

 Revision 1.1.2.2  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.1.2.1  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes

 Revision 1.1.2.1  2004/11/25 08:29:41  mch
 added table writers modelled on STIL


 */








