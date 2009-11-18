/*
 * $Id: ConeSearchVoTableWriter.java,v 1.3 2009/11/18 16:33:49 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.StdDataTypes;
import org.astrogrid.dataservice.metadata.VoTypes;
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

public class ConeSearchVoTableWriter extends VoTableWriter {
   
   private static final Log log = LogFactory.getLog(ConeSearchVoTableWriter.class);
   
   /**
    * Construct this wrapping the given target.
    */
   public ConeSearchVoTableWriter(TargetIdentifier target, String title, Principal user) throws IOException {
     super(target, title, user);
   }

   /**
    * Construct this wrapping the given stream.
    */
   public ConeSearchVoTableWriter(OutputStream out, String title, Principal user) throws IOException {
     super(out, title, user);
   }

  /**
   * Writes the opening of a VOTable-1.1 document.
   * C.f. the superclass which writes a VOTable-1.2 document.
   *
   * @throws IOException If anything goes wrong.
   */
  @Override
  public void open() throws IOException {
    println("<?xml version='1.0' encoding='UTF-8'?>");
    println("<VOTABLE "
       + "xmlns='http://www.ivoa.net/xml/VOTable/v1.1'  "
       + "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'  "
       + "xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.1 http://software.astrogrid.org/schema/vo-formats/VOTable/v1.1/VOTable.xsd'  "
       + "version='1.1'"
       + ">");

    println("<RESOURCE>");

    // Make sure stream is still ok
    doAFlush();
  }
   
   
   /**
    * Writes the start of the TABLE element including the FIELD elements.
    * Collates and remembers the column metadata to inform the later writing
    * of table rows.
    */
   @Override
   public void startTable(ColumnInfo[] colinfo) throws IOException {

     cols = colinfo;
      
     println("<TABLE>");

     boolean doneIdMain = false;
     boolean doneRaMain = false;
     boolean doneDecMain = false;
     for (int i = 0; i < cols.length; i++) {
       if (cols[i] != null) { // null columns if it's not been found in the metadoc

         // For the first column labelled as the main ID of the table,
         // mark it up in the VOTable according to the cone-search rules.
         // For subsequent columns so labelled, change the UCD to say
         // "just another ID".
         if (cols[i].getUcd("1").equals("ID_MAIN") ||
             matchUcd(cols[i].getUcd("1+"), new String[]{"meta.id", "meta.main"})) {
           if (!doneIdMain) {
             println(String.format("<FIELD name='%s' ID='%s' ucd='ID_MAIN' datatype='char' arraysize='*'/>",
                     cols[i].getName(), cols[i].getId()));
             doneIdMain = true;
             continue;
           }
           else {
             cols[i].setUcd("meta.id", "1+");
           }
         }

         // For the first column labelled as the main RA of the table,
         // mark it up in the VOTable according to the cone-search rules.
         // For subsequent columns so labelled, change the UCD to say
         // "just another RA".
         if (cols[i].getUcd("1").equals("POS_EQ_RA_MAIN") ||
             matchUcd(cols[i].getUcd("1+"), new String[]{"pos.eq.ra", "meta.main"})) {
           if (!doneRaMain) {
             println(String.format("<FIELD name='%s' ID='%s' ucd='POS_EQ_RA_MAIN' datatype='%s'/>",
                     cols[i].getName(), cols[i].getId(), cols[i].getPublicType()));
             doneRaMain = true;
             continue;
           }
           else {
             cols[i].setUcd("pos.eq.ra", "1+");
           }
         }

         // For the first column labelled as the main declination of the table,
         // mark it up in the VOTable according to the cone-search rules.
         // For subsequent columns so labelled, change the UCD to say
         // "just another declination".
         if (cols[i].getUcd("1").equals("POS_EQ_DEC_MAIN") ||
             matchUcd(cols[i].getUcd("1+"), new String[]{"pos.eq.dec", "meta.main"})) {
           if (!doneDecMain) {
             println(String.format("<FIELD name='%s' ID='%s' ucd='POS_EQ_DEC_MAIN' datatype='%s'/>",
                     cols[i].getName(), cols[i].getId(), cols[i].getPublicType()));
             doneDecMain = true;
             continue;
           }
           else {
             cols[i].setUcd("pos.eq.dec", "1+");
           }
         }

         // The following writes the FIELD element for any column that isn't
         // given special treatment in Cone Search.
         printString(String.format("<FIELD name='%s' ", cols[i].getName()));
         String id = cols[i].getId();
         if (id != null) {
           printString(String.format("ID='%s' ", id));
         }

         String ucd = cols[i].getUcd("1+");
         if (ucd != null) {
           printString(String.format("ucd='%s' ", ucd));
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
   
   

   /**
    * Matches the words in a UCD. Matching is case sensitive.
    *
    * @param words The words to be matched.
    * @return True if the given UCD includes all the given words.
    */
   private boolean matchUcd(String ucd, String[] words) {
     if (ucd == null) {
       return false;
     }
     for (int i = 0; i < words.length; i++) {
       if (!ucd.contains(words[i])) {
         return false;
       }
     }
     return true;
   }
}