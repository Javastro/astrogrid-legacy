/*
 * $Id: VoTableFitsWriter.java,v 1.1.1.1 2009/05/13 13:20:51 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;

import java.io.IOException;
import java.security.Principal;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.ColumnInfo;

/**
 * For writing out fits wrapped in votable
 *
 * @author M Hill
 */

public class VoTableFitsWriter extends VoTableWriter {
   
   
   /**
    * Construct this wrapping the given stream.  Writes out the first few tags
    */
   public VoTableFitsWriter(TargetIdentifier target, String title, Principal user) throws IOException {
      super(target, title, user);
   }
   
   /** Opens writer - starts the document with appropriate headers */
   public void open() throws IOException {
      println("<?xml version='1.0' encoding='UTF-8'?>");
            // The current astrogrid/xml validator wants this next
            // line, but then falls over at the xmlns:xsi stuff.  
            // printOut.println("<!DOCTYPE VOTABLE SYSTEM \"http://us-vo.org/xml/VOTable.dtd\">");
             println("<VOTABLE "
                +"xmlns='http://www.ivoa.net/xml/VOTable/v1.1'  "
                +"xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'  "
                +"xsi:schemaLocation='http://www.ivoa.net/xml/VOTable/v1.1 http://software.astrogrid.org/schema/vo-formats/VOTable/v1.1/VOTable.xsd'  "
                +"version='1.1'"                                                                +">");
   }
   
   /** Start body - writes out header and preps col array */
   public void startTable(ColumnInfo[] cols) throws IOException {
   }
   
   /** Actually writes out a table.  Assumes only 1 element in given colValues,
    * that element being the fits url */
   public void writeRow(Object[] colValues) throws IOException {
      
      println("<TABLE>");
      println("<DATA><FITS>");
      
      println("   <STREAM href='"+colValues[0]+"' />");
      
      println("</FITS></DATA>");
      println("</TABLE>");
      
   }
   
   public void endTable() {
      
   }
   public void close() throws IOException {
      println("</VOTABLE>");
      bufferedOut.close();
   }
}

/*
 $Log: VoTableFitsWriter.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:51  gtr


 Revision 1.6  2007/06/08 13:16:12  clq2
 KEA-PAL-2169

 Revision 1.5.8.1  2007/04/23 16:45:19  kea
 Checkin of work in progress.

 Revision 1.5  2006/10/17 10:11:41  clq2
 PAL_KEA_1869

 Revision 1.4.48.1  2006/10/12 16:40:15  kea
 Tweaks while fixing registration issues (see bugzilla ticket 1920)

 Revision 1.4  2005/11/21 12:54:18  clq2
 DSA_KEA_1451

 Revision 1.3.38.1  2005/11/15 15:37:28  kea
 Fixed open() and close() methods so as to produce valid VOTable.

 Revision 1.3  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.2.10.2  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.2.10.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/30 21:51:25  mch
 Fix to return Votable fits list for url list

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

 Revision 1.1.2.3  2005/01/13 18:57:31  mch
 Fixes to metadata mostly

 Revision 1.1.2.2  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.1  2004/11/30 01:26:42  mch
 added tablewriters



 */
