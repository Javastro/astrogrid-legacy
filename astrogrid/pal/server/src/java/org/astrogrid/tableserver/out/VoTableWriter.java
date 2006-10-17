/*
 * $Id: VoTableWriter.java,v 1.9 2006/10/17 10:11:41 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.astrogrid.ucd.UcdException;

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
   
   
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(VoTableWriter.class);

   protected PrintWriter printOut = null;
   
   protected ColumnInfo[] cols = null;
   
   /**
    * Construct this wrapping the given stream.
    */
   public VoTableWriter(TargetIdentifier target, String title, Principal user) throws IOException {
      
      target.setMimeType(MimeTypes.VOTABLE);
      
      printOut = new PrintWriter(new BufferedWriter(target.openWriter()));
   }
   
   public void open() {
      printOut.println("<?xml version='1.0' encoding='UTF-8'?>");
      printOut.println("<VOTABLE " 
         +"xmlns='http://www.ivoa.net/xml/VOTable/v1.1'  "
         +"xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'  "
         +"xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.1 http://software.astrogrid.org/schema/vo-formats/VOTable/v1.1/VOTable.xsd'  "
         +"version='1.1'"
         +">");
         
         /* don't know where to find this info - in the netadata I expect
          <DEFINITIONS>
          <COOSYS ID="myJ2000" system="eq_FK5" equinox="2000." epoch="2000."/>
          </DEFINITIONS>
          */
         
         printOut.println("<RESOURCE>");
         /* don't know where to find this info - in the netadata I expect
          <PARAM ID="RA" datatype="E" value="200.0"/>
          <PARAM ID="DE" datatype="E" value="40.0"/>
          <PARAM ID="SR" datatype="E" value="30.0"/>
          <PARAM ID="PositionalError" datatype="E" value="0.1"/>
          <PARAM ID="Credit" datatype="A" arraysize="*" value="Charles Messier, Richard Gelderman"/>
          */
      
      
   }
   
   
   /** Produces text/html */
   public String getMimeType() {
      return MimeTypes.VOTABLE;
   }
   
   
   /** Start body - writes out header and preps col array */
   public void startTable(ColumnInfo[] colinfo) throws IOException {
      
      String ucdVersion = UcdVersions.getUcdVersion();

      this.cols = colinfo;
      
      printOut.println("<TABLE>");

      for (int i = 0; i < cols.length; i++) {
         if (cols[i] != null) { // null columns if it's not been found in the metadoc
         
            printOut.print("<FIELD name='"+cols[i].getName()+"' ");
            if (cols[i].getId() != null) printOut.print("ID='"+cols[i].getId()+"' ");
            if (cols[i].getUcd(ucdVersion) != null) printOut.print(" ucd='"+cols[i].getUcd(ucdVersion)+"' ");
            
            //Create the votable type attributes from the metadoc type. 
            //Convert using java class as the medium
            if (cols[i].getPublicType() != null) {
               Class colType = StdDataTypes.getJavaType(cols[i].getPublicType());
               printOut.print(VoTypes.getVoTableTypeAttributes(colType));
            }
            /*
            // KEA FUTURE: I added this, but not sure if it should
            // work or not, needs further investigation.
            // Try java type if no public type
            else if (cols[i].getJavaType() != null) { 
               printOut.print(VoTypes.getVoTableTypeAttributes(
                     cols[i].getJavaType()));
            }
            */
            else {
              // Use String type as default, since any real data type
              // will be parsable as a string.  THis is not ideal; 
              // hopefully we can fix the problem of not knowing the
              // type in future.
               printOut.print(VoTypes.getVoTableTypeAttributes(String.class));
            }
   
            //units
            if (cols[i].getJavaType() == Date.class) {
               printOut.print(" unit='s' ");
            }
            else {
               if ((cols[i].getUnits() != null) && (cols[i].getUnits().toString().trim().length()>0)) {
                  printOut.print(" unit='"+cols[i].getUnits()+"' ");
               }
            }
            printOut.println("/>");
         }
      }

      printOut.flush(); //so something comes back to the browser quickly
      
      //start body table
      printOut.println("<DATA>");
      printOut.println("<TABLEDATA>");
      
   }
   
   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException {
      
      printOut.print("<TR>");
      for (int i=0;i<colValues.length;i++) {
         if (cols[i] != null) { //skip columns with no metadata
            if (colValues[i] instanceof Date) {
               printOut.print("<TD>"+ (float) (((Date) colValues[i]).getTime()/1000) +"</TD>");
            }
            else {
               if ( (colValues[i] == null) || (colValues[i].equals("null")) ){
                 //From VOTable spec: "In the TABLEDATA data representation, 
                 //the default representation of a ``null'' value is an 
                 //empty column (i.e. <TD></TD>)";
                 printOut.print("<TD></TD>");
               }
               else {
                 printOut.print("<TD>"+colValues[i]+"</TD>");
               }
            }
         }
      }
      printOut.println("</TR>");
      
   }

   public void endTable() {
            //close row body
         printOut.println("</TABLEDATA>");
         printOut.println("</DATA>");

      //close document
      printOut.println("</TABLE>");
   }
   
   /** Closes writer - writes out the closing tags and closes wrapped stream
    */
   public void close() {
      
      printOut.println("</RESOURCE>");
      
      printOut.println("</VOTABLE>");
      
      printOut.close();
   }
   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() {
      printOut.println("<tr><td> ------------------ Writing Aborted -----------------</td></tr> ");
      close();
   }
   
   /** Convenience method to get direct access to the output stream, so that we can pipe votables direct */
   public Writer getOut() {
      return printOut;
   }
   
}

/*
 $Log: VoTableWriter.java,v $
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



