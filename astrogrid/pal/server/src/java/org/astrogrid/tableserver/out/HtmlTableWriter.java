/*
 * $Id: HtmlTableWriter.java,v 1.7 2005/05/27 16:21:02 clq2 Exp $
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
   
   /**
    * Construct this wrapping the given stream.  Writes out the first few tags
    */
   public HtmlTableWriter(TargetIdentifier target, String aTitle, String aComment, Principal user) throws IOException {
      
      target.setMimeType(MimeTypes.HTML);
      
      printOut = new PrintWriter(new BufferedWriter(target.openWriter()));
      
      this.title = aTitle;
      this.comment = aComment;
   }
   
   public void open() {
      printOut.println("<HTML>");
      
      printOut.println("<HEAD>");
      printOut.println("<TITLE>"+title+"</TITLE>");
      printOut.println("</HEAD>");
      
      printOut.println("<BODY>");
      printOut.println("<H1>"+title+"</H1>");
      if (comment != null) {
         printOut.println("<P>"+comment+"</P>");
      }
   }
   
   /** Produces text/html */
   public String getMimeType() {
      return MimeTypes.HTML;
   }
   
   public void startTable(ColumnInfo[] givenCols) throws IOException {

      cols = givenCols; //remember column info for printing rows
      
      printOut.println("<TABLE border='1'>");  //it's wrong to put formatting in here, but the default seems to be 0
      
      printOut.println("</TR>");
      printOut.println("<TH>ID</TH>");
      for (int i = 0; i < cols.length; i++) {
         if (cols[i] == null) {
            throw new IllegalArgumentException("No information for column "+i);
         }
         printOut.print("<TH>"+cols[i].getId()+"</TH>");
      }
      printOut.println("</TR>");

      printOut.println("</TR>");
      printOut.println("<TH>Public Type</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+cols[i].getPublicType()+"</TH>");
      }
      printOut.println("</TR>");

      printOut.println("</TR>");
      printOut.println("<TH>Java Class</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>");
         if (cols[i].getJavaType() != null) {
            cols[i].getJavaType().getName();
         }
         printOut.print("</TH>");
      }
      printOut.println("</TR>");

      printOut.println("</TR>");
      printOut.println("<TH>Native Type</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+cols[i].getBackType()+"</TH>");
      }
      printOut.println("</TR>");

      printOut.println("</TR>");
      printOut.println("<TH>UCD</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+emptyIfNull(cols[i].getUcd("1"))+"</TH>");
      }
      printOut.println("</TR>");

      printOut.println("</TR>");
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
      
   }

   public void endTable() {
      printOut.println("</TABLE>");
      printOut.println("<p>"+rows+" Rows</p>");
   }

   /** Closes writer - writes out the closing tags and closes wrapped stream
    */
   public void close() {
      
      printOut.println("</BODY>");
      
      printOut.println("</HTML>");
      printOut.close();
   }
   
   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() {
      printOut.println("<tr><td> ------------------ Writing Aborted -----------------</td></tr> ");
      close();
   }
   
   
}

/*
 $Log: HtmlTableWriter.java,v $
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








