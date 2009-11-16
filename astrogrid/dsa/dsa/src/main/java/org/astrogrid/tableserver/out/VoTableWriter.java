/*
 * $Id: VoTableWriter.java,v 1.4 2009/11/16 15:37:32 gtr Exp $
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
import org.astrogrid.ucd.UcdVersions;

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
      println("<DESCRIPTION>Error processing query</DESCRIPTION>");
      println("<INFO ID=\"Error\" name=\"Error\" value=\"" + 
            errorDesc + "\"/>");
      println("</VOTABLE>");
   }

  public void open() throws IOException {
    println("<VOTABLE xmlns='http://www.ivoa.net/xml/VOTable/v1.2' " +
            "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
            "xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.2 " +
            "http://www.ivoa.net/xml/VOTable/v1.2' " +
            "version='1.2'>");
    println("  <RESOURCE>");
    doAFlush();
  }
   
  public String getMimeType() {
    return MimeTypes.VOTABLE;
  }
   
   
   /** Start body - writes out header and preps col array */
   public void startTable(ColumnInfo[] colinfo) throws IOException {
      
      String ucdVersion = UcdVersions.getUcdVersion();

      this.cols = colinfo;
      
      println("<TABLE>");

      for (int i = 0; i < cols.length; i++) {
         if (cols[i] != null) { // null columns if it's not been found in the metadoc
         
            printString("<FIELD name='"+cols[i].getName()+"' ");
            if (cols[i].getId() != null) printString("ID='"+cols[i].getId()+"' ");
            if (cols[i].getUcd(ucdVersion) != null) printString(" ucd='"+cols[i].getUcd(ucdVersion)+"' ");
            
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
      for (int i=0;i<colValues.length;i++) {
         if (cols[i] != null) { //skip columns with no metadata
            if (colValues[i] instanceof Date) {
               printString("<TD>"+ (float) (((Date) colValues[i]).getTime()/1000) +"</TD>");
            }
            else {
               if ( (colValues[i] == null) || (colValues[i].equals("null")) ){
                 //From VOTable spec: "In the TABLEDATA data representation, 
                 //the default representation of a ``null'' value is an 
                 //empty column (i.e. <TD></TD>)";
                 printString("<TD></TD>");
               }
               else {
                 String output = makeSafeString( (String)colValues[i] );
                 printString("<TD>"+output+"</TD>");
               }
            }
         }
      }
      println("</TR>");

      rowsWritten = rowsWritten + 1;
      if (rowsWritten % FLUSHFREQUENCY == 0) {
         // Make sure stream is still ok
         doAFlush();
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

/*
 $Log: VoTableWriter.java,v $
 Revision 1.4  2009/11/16 15:37:32  gtr
 VOTables are now v1.2 except for cone search where they are v1.1.

 Revision 1.3  2009/11/13 13:50:14  gtr
 The log is now private.

 Revision 1.2  2009/11/12 11:25:18  gtr
 Data of type java.lang.Object returned from JDBC no longer abort the querty processing.

 Revision 1.1.1.1  2009/05/13 13:20:51  gtr


 Revision 1.14  2008/05/27 11:07:38  clq2
 merged PAL_KEA_2715

 Revision 1.13.2.1  2008/05/01 10:52:54  kea
 Fixes relating to:  BZ2127 BZ2657 BZ2720 BZ2721

 Revision 1.13  2008/04/02 14:20:44  clq2
 KEA_PAL2654

 Revision 1.12.30.1  2008/03/31 17:15:38  kea
 Fixes for conesearch error reporting.

 Revision 1.12  2007/06/08 13:16:12  clq2
 KEA-PAL-2169

 Revision 1.11.2.2  2007/06/08 13:06:41  kea
 Ready for trial merge.

 Revision 1.11.2.1  2007/04/23 16:45:19  kea
 Checkin of work in progress.

 Revision 1.11  2007/03/02 13:43:45  kea
 Added proper error checking to PrintWriter output stream writers in these
 classes;  failures were going undetected as PrintWriters do not throw
 exceptions.  See bugzilla bug 2139.

 Revision 1.10  2007/02/20 12:22:15  clq2
 PAL_KEA_2062

 Revision 1.9.4.2  2007/02/13 15:54:29  kea
 A useful utility from Mark Taylor to help with binary votable output.

 Revision 1.9.4.1  2007/02/01 11:16:50  kea
 Fix to bug whereby double columns were being reported as floats in VOTable
 (bug 2078);  extra debug logging.

 Revision 1.9  2006/10/17 10:11:41  clq2
 PAL_KEA_1869

 Revision 1.8.2.1  2006/10/12 16:40:15  kea
 Tweaks while fixing registration issues (see bugzilla ticket 1920)

 Revision 1.8  2006/09/26 15:34:43  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.7.10.2  2006/09/19 12:19:47  kea
 Fixing TabularDB registrations to use system-default UCD version;
 moved ucd version management stuff to org.astrogrid.ucd package.

 Revision 1.7.10.1  2006/09/14 14:53:03  kea
 Updating.

 Revision 1.7  2006/06/15 16:50:10  clq2
 PAL_KEA_1612

 Revision 1.6.26.2  2006/06/15 14:08:04  kea
 Nearly ready to branch.

 Revision 1.6.26.1  2006/06/13 21:05:17  kea
 Getting tests working fully after Jeff's new ADQLbeans jar.

 Revision 1.6  2005/11/21 12:54:18  clq2
 DSA_KEA_1451

 Revision 1.5.38.1  2005/11/15 15:38:46  kea
 Layout change only.

 Revision 1.5  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.4.10.3  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.4.10.2  2005/04/28 17:14:49  mch
 fix to not add 'unit' if unit is empty

 Revision 1.4.10.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.4  2005/03/30 21:51:25  mch
 Fix to return Votable fits list for url list

 Revision 1.3  2005/03/30 18:54:03  mch
 fixes to results format

 Revision 1.2  2005/03/30 15:52:15  mch
 debug etc for bad sql types

 Revision 1.1  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.4  2005/03/10 15:13:48  mch
 Seperating out fits, table and xdb servers

 Revision 1.3  2005/03/10 13:49:52  mch
 Updating metadata

 Revision 1.2  2005/03/01 15:58:33  mch
 Changed to use starlinks tamfits library

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.10  2005/01/24 12:14:28  mch
 Fixes to VizieR proxy and resource stuff

 Revision 1.1.2.9  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.1.2.8  2004/12/13 21:53:14  mch
 Made the java types the intermediate types, added types to Xsv and html output

 Revision 1.1.2.7  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.6  2004/12/07 21:21:09  mch
 Fixes after a days integration testing

 Revision 1.1.2.5  2004/12/07 00:49:42  mch
 minor changes to put rows on one line (compact)

 Revision 1.1.2.4  2004/12/06 02:50:30  mch
 a few bug fixes

 Revision 1.1.2.3  2004/12/05 19:33:16  mch
 changed skynode to 'raw' soap (from axis) and bug fixes

 Revision 1.1.2.2  2004/11/30 02:32:18  mch
 fix to 0-base of writerows

 Revision 1.1.2.1  2004/11/30 01:26:42  mch
 added tablewriters



 */



