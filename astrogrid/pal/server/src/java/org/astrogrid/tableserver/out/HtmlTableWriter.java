/*
 * $Id: HtmlTableWriter.java,v 1.1 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.slinger.mime.MimeTypes;

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
   
   /**
    * Construct this wrapping the given stream.  Writes out the first few tags
    */
   public HtmlTableWriter(Writer target, String title, String comment) throws IOException {
      printOut = new PrintWriter(new BufferedWriter(target));
      
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
   
   /** Closes writer - writes out the closing tags and closes wrapped stream
    */
   public void close() {
      
      printOut.println("</BODY>");
      
      printOut.println("</HTML>");
      printOut.close();
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
      printOut.println("<TH>Type</TH>");
      for (int i = 0; i < cols.length; i++) {
         printOut.print("<TH>"+cols[i].getDatatype()+"</TH>");
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
         printOut.print("<TH>"+emptyIfNull(cols[i].getUnits().toString())+"</TH>");
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
            printOut.println("<TD align='right'>"+dateFormat.format(colValues[i])+"</TD>");
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
            printOut.println("<TD "+align+">"+colValues[i].toString()+"</TD>");
         }
      }
      printOut.println("</TR>");
      
   }

   public void endTable() {
      printOut.println("</TABLE>");
      printOut.println("<p>"+rows+" Rows</p>");
   }

   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() {
      printOut.println("<tr><td> ------------------ Writing Aborted -----------------</td></tr> ");
      close();
   }
   
   
}

/*
 $Log: HtmlTableWriter.java,v $
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







