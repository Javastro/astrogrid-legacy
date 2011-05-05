/*
 * $Id: VoTableWriter.java,v 1.7 2011/05/05 14:49:37 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.security.Principal;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.astrogrid.dataservice.metadata.StdDataTypes;
import org.astrogrid.dataservice.metadata.VoTypes;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.ColumnInfo;

/**
 * For writing out tables in votable.  As far as I'm aware dates are not handled
 * by VOTables (by Jan 2005).  Since I'm stroppely ignoring VOTable development as
 * irrelevent (ie I'm not going to try and push for standard date support, they can
 * get around to it whenever), dates here are written as the 'C standard' number of seconds since
 * 1970 (negative numbers before 1970), with type 'float' so we can provide milliseconds
 * etc. NB we need to handle dates from several thousand BC.
 *
 * @author M Hill
 */

public class VoTableWriter implements TableWriter {
   
   private static final Log log = org.apache.commons.logging.LogFactory.getLog(VoTableWriter.class);

   protected BufferedWriter bufferedOut = null;
   
   protected ColumnInfo[] cols = null;

   protected int rowsWritten = 0;

   // How many rows to write before we do a flush
   protected static final int FLUSHFREQUENCY = 1000;
   
   /**
    * Construct this wrapping the given stream.
    */
   public VoTableWriter(TargetIdentifier target, String title, Principal user) throws IOException {
      target.setMimeType(MimeTypes.VOTABLE);
      bufferedOut = new BufferedWriter(target.openWriter());
   }


   /**
    * Construct this wrapping the given stream.
    */
   public VoTableWriter(OutputStream out, String title, Principal user) throws IOException {
      bufferedOut = new BufferedWriter( new OutputStreamWriter (out));
   }

   /**
    * Constructs a VOTable writer on a given Writer.
    *
    * @param out The Writer to receive the output.
    */
   public VoTableWriter(Writer out, String title, Principal user) throws IOException {
     bufferedOut = new BufferedWriter(out);
   }
   
   protected void printString(String toPrint) throws IOException
   {
      bufferedOut.write(toPrint, 0, toPrint.length());
   }
   protected void println(String toPrint) throws IOException
   {
      bufferedOut.write(toPrint, 0, toPrint.length());
      bufferedOut.write("\n",0,1); //TOFIX USE SYSTEM-SPECIFIC EOLN?
   }

   public void writeErrorTable(String errorDesc) throws IOException {
      errorDesc = makeSafeString(errorDesc);

      println("<?xml version='1.0' encoding='UTF-8'?>");
      println("<!DOCTYPE VOTABLE SYSTEM \"http://us-vo.org/xml/VOTable.dtd\">");
      println("<VOTABLE version=\"1.0\">");
      println("  <DESCRIPTION>Error processing query</DESCRIPTION>");
      println("  <RESOURCE type='results'>");
      println("    <INFO name='QUERY_STATUS' value='ERROR'>" +  errorDesc + "</INFO>");
      println("  </RESOURCE>");
      println("</VOTABLE>");
   }

  public void open() throws IOException {
    println("<VOTABLE xmlns='http://www.ivoa.net/xml/VOTable/v1.2' " +
            "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
            "xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.2 " +
            "http://www.ivoa.net/xml/VOTable/v1.2' " +
            "version='1.2'>");
    println("<RESOURCE type='results'>");
    println("<INFO name='QUERY_STATUS' value='OK'/>");
    doAFlush();
  }
   
  public String getMimeType() {
    return MimeTypes.VOTABLE;
  }
   
   
   /** Start body - writes out header and preps col array */
   public void startTable(ColumnInfo[] colinfo) throws IOException {
      
      this.cols = colinfo;
      
      println("<TABLE>");

      for (int i = 0; i < cols.length; i++) {
         if (cols[i] != null) { // null columns if it's not been found in the metadoc
         
            printString("<FIELD name='"+cols[i].getName()+"' ");
            if (cols[i].getId() != null) {
              printString("ID='"+cols[i].getId()+"' ");
            }
            String ucd = cols[i].getUcd("1+");
            if (ucd != null) {
              printString(" ucd='"+ucd+"' ");
            }
            if (cols[i].getUtype() != null) {
              printString(" utype='" + cols[i].getUtype() +"' ");
            }

            //Create the votable type attributes from the metadoc type. 
            //Convert using java class as the medium
            if (cols[i].getPublicType() != null) {

               log.debug("VOTable generator: public type of column " +
                   cols[i].getName() + " is " + cols[i].getPublicType()); 

               Class colType = StdDataTypes.getJavaType(cols[i].getPublicType());
               log.debug("VOTable generator: coltype of column " +
                   cols[i].getName() + " is " + colType.toString());

               printString(VoTypes.getVoTableTypeAttributes(colType));
            }
            // KEA FUTURE: I added this, but not sure if it should
            // work or not, needs further investigation.
            // Try java type if no public type
            else if (cols[i].getJavaType() != null) {
              try {
                printString(VoTypes.getVoTableTypeAttributes(cols[i].getJavaType()));
              }
              catch (IllegalArgumentException e) {
                log.warn(String.format("Column %s has type %s and we can't handle that; treating it as a string.",
                         cols[i].getId(), cols[i].getJavaType()));
                printString(VoTypes.getVoTableTypeAttributes(String.class));
              }
            }
            else {
              // Use String type as default, since any real data type
              // will be parsable as a string.  THis is not ideal; 
              // hopefully we can fix the problem of not knowing the
              // type in future.
               printString(VoTypes.getVoTableTypeAttributes(String.class));
            }
   
            //units
            if (cols[i].getJavaType() == Date.class) {
               printString(" unit='s' ");
            }
            else {
               if ((cols[i].getUnits() != null) && (cols[i].getUnits().toString().trim().length()>0)) {
                  printString(" unit='"+cols[i].getUnits()+"' ");
               }
            }
            println(">");
            println(" <DESCRIPTION>");
            String desc = cols[i].getDescription();
            // Make safe
            println(" " + desc);
            println(" </DESCRIPTION>");
            println("</FIELD>");
         }
      }

      bufferedOut.flush();
      
      //start body table
      println("<DATA>");
      println("<TABLEDATA>");
      
      // Make sure stream is still ok
      doAFlush();
   }
   
  /** Writes the given array of values out */
  public void writeRow(Object[] colValues) throws IOException {
    printString("<TR>");
    for (int i=0; i < colValues.length; i++) {
      if (cols[i] != null) { //skip columns with no metadata
        printString(formatCell(colValues[i]));
      }
    }
    println("</TR>");

    rowsWritten = rowsWritten + 1;
    if (rowsWritten % FLUSHFREQUENCY == 0) {
      doAFlush();
    }
  }

   /**
    * Supplies the XML encoding of a cell of a table for a given value.
    * The cell is an XML TD element, as specified in the VOTable standard.
    * Java Dates are encoded as counts of seconds since the start of
    * 1970. Null values are encoded as empty cells (with opening and closing
    * tags, not as an empty XML element; the unit test depends on this).
    * Strings are modified to turn XML's reserved characters &, < and > into
    * character entities {@code &amp;}, {@code &lt;} and {@code &gt;}. All
    * other kinds of data are represented by the output of their
    * {@code toString()} methods.
    *
    * @param x The datum to be encoded in the cell.
    * @return The XML fragment for the cell.
    */
   protected String formatCell(final Object x) {
     if (x == null) {
       return "<TD></TD>";
     }
     else if (x instanceof Date) {
       Date d = (Date) x;
       double milliseconds = (double) d.getTime();
       return "<TD>"+ (milliseconds/1000.0) +"</TD>";
     }
     else if (x instanceof String) {
       String safe = (String) x;
       safe = safe.replaceAll("&", "&amp;");
       safe = safe.replaceAll("<", "&lt;");
       safe = safe.replaceAll(">", "&gt;");
       return "<TD>" + safe + "</TD>";
     }
     else {
       return "<TD>" + x + "</TD>";
     }
   }

   public void endTable() throws IOException {
            //close row body
         println("</TABLEDATA>");
         println("</DATA>");

      //close document
      println("</TABLE>");

      // Make sure stream is still ok
      doAFlush();
   }
   
   /** Closes writer - writes out the closing tags and closes wrapped stream
    */
   public void close() throws IOException {
      
      println("</RESOURCE>");
      println("</VOTABLE>");
      
      doAFlush();
      bufferedOut.close();
   }
   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() throws IOException {
      println("<tr><td> ------------------ Writing Aborted -----------------</td></tr> ");
      close();
   }
   
   /** Convenience method to get direct access to the output stream, so that we can pipe votables direct */
   public Writer getOut() {
      return bufferedOut;
   }
   
   protected void doAFlush() throws IOException
   {
     bufferedOut.flush();
   }

   protected String makeSafeString(String input) {
      String output = input.replaceAll("&", "&amp;"); //Do this first!!
      output = output.replaceAll("<", "&lt;");
      output = output.replaceAll(">", "&gt;");
      return output;
   }
}