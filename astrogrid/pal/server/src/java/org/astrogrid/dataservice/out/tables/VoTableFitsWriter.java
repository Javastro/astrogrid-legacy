/*
 * $Id: VoTableFitsWriter.java,v 1.2 2005/03/10 13:49:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.out.tables;
import org.astrogrid.dataservice.metadata.tables.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.commons.logging.Log;
import org.astrogrid.dataservice.queriers.sql.RdbmsTableMetaDocGenerator;
import org.astrogrid.slinger.mime.MimeTypes;

/**
 * For writing out fits wrapped in votable
 *
 * @author M Hill
 */

public class VoTableFitsWriter extends VoTableWriter {
   
   
   /**
    * Construct this wrapping the given stream.  Writes out the first few tags
    */
   public VoTableFitsWriter(Writer target, String title) throws IOException {
      super(target, title);
      
   }
   
   /** Start body - writes out header and preps col array */
   public void startTable(ColumnInfo[] cols) throws IOException {
   }
   
   /** Actually writes out a table.  Assumes only 1 element in given colValues,
    * that element being the fits url */
   public void writeRow(Object[] colValues) throws IOException {
      
      printOut.println("<TABLE>");
      printOut.println("<DATA><FITS>");
      
      printOut.println("   <STREAM>"+colValues[0]+"</STREAM>");
      
      printOut.println("</FITS></DATA>");
      printOut.println("</TABLE>");
      
   }
   
   public void endTable() {
      
   }
   
   
}

/*
 $Log: VoTableFitsWriter.java,v $
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



