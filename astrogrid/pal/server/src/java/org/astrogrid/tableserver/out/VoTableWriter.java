/*
 * $Id: VoTableWriter.java,v 1.4 2005/03/30 21:51:25 mch Exp $
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
import org.astrogrid.slinger.mime.MimeTypes;
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
   
   
   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(VoTableWriter.class);
   
   protected PrintWriter printOut = null;
   
   protected ColumnInfo[] cols = null;
   
   public final static String TYPE_LONG      = "long";
   public final static String TYPE_BOOLEAN   = "boolean";
   public final static String TYPE_BIT       = "bit";
   public final static String TYPE_UBYTE     = "unsignedByte";
   public final static String TYPE_CHAR      = "char";
   public final static String TYPE_UNICHAR   = "unicodeChar";
   public final static String TYPE_DOUBLE    = "double";
   public final static String TYPE_FLOAT     = "float";
   public final static String TYPE_DOUBLECOMPLEX = "doubleComplex";
   public final static String TYPE_FLOATCOMPLEX  = "floatComplex";
   public final static String TYPE_INT       = "int";
   public final static String TYPE_SHORT     = "short";
   
   public final static String[] TYPES = new String[] {
      TYPE_LONG, TYPE_BOOLEAN, TYPE_BIT, TYPE_UBYTE, TYPE_CHAR, TYPE_UNICHAR, TYPE_DOUBLE, TYPE_FLOAT, TYPE_DOUBLECOMPLEX, TYPE_FLOATCOMPLEX, TYPE_INT, TYPE_SHORT
   };
   
   /**
    * Construct this wrapping the given stream.  Writes out the first few tags
    */
   public VoTableWriter(TargetIdentifier target, String title, Principal user) throws IOException {
      
      target.setMimeType(MimeTypes.VOTABLE, user);
      
      printOut = new PrintWriter(new BufferedWriter(target.resolveWriter(user)));
      
//         printOut.println("<!DOCTYPE VOTABLE SYSTEM 'http://us-vo.org/xml/VOTable.dtd'>");
         printOut.println("<VOTABLE version='1.0'>");
         
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
   
   /**Returns the VOTable type for the given java class type */
   public static String getVoTableType(Class javatype) {
      if (javatype == String.class) {        return TYPE_CHAR;    }
      else if (javatype == Integer.class) {  return TYPE_INT;     }
      else if (javatype == Long.class)    {  return TYPE_INT;    }
      else if (javatype == Float.class)   {  return TYPE_FLOAT;   }
      else if (javatype == Double.class) {   return TYPE_FLOAT;  }
      else if (javatype == Boolean.class) {  return TYPE_BOOLEAN;    }
      else if (javatype == Date.class)    {  return TYPE_FLOAT;    }
      else {
         log.error("Don't know what VOtable type the java class "+javatype+" maps to, returning string");
         return TYPE_CHAR;
      }
   }
   
   
   /** Start body - writes out header and preps col array */
   public void startTable(ColumnInfo[] cols) throws IOException {
      
      printOut.println("<TABLE>");
         
      for (int i = 0; i < cols.length; i++) {
         if (cols[i] == null) {
            throw new IllegalArgumentException("No information for column "+i);
         }
      
         printOut.print("<FIELD name='"+cols[i].getName()+"' ");
         if (cols[i].getId() != null) printOut.print("ID='"+cols[i].getId()+"' ");
         if (cols[i].getUcd("1") != null) printOut.print(" ucd='"+cols[i].getUcd("1")+"' ");
         
         //type
         if (cols[i].getPublicType() != null) {
            //do some checking on data types
            String type = null;
            for (int t = 0; t < TYPES.length; t++)
            {
               if (cols[i].getPublicType().equals(TYPES[t])) {
                  type = TYPES[t]; //found a match
               }
            }
            if (type == null) {
               //nothign found, try converting from common types
               if (cols[i].getPublicType().toLowerCase().equals("real")) {
                  type = TYPE_FLOAT;
               }
               else if (cols[i].getPublicType().toLowerCase().equals("smallint")) {
                  type = TYPE_INT;
               }
               else if (cols[i].getPublicType().toLowerCase().equals("varchar")) {
                  type = TYPE_CHAR;
               }
            }
         
            if (type==null) {
               type = getVoTableType(cols[i].getJavaType());
            }
            
            if (type != null) {
               printOut.print(" datatype='"+type+"'");
            }
         }

         //units
         if (cols[i].getJavaType() == Date.class) {
            printOut.print(" units='s' ");
         }
         else {
            if (cols[i].getUnits() != null) printOut.print(" units='"+cols[i].getUnits()+"' ");
         }
         printOut.println("/>");
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
         if (colValues[i] instanceof Date) {
            printOut.print("<TD>"+ (float) (((Date) colValues[i]).getTime()/1000) +"</TD>");
         }
         else {
            printOut.print("<TD>"+colValues[i]+"</TD>");
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



