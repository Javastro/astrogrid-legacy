/*
 * $Id: TableWriterMaker.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 */
package org.astrogrid.dataservice.out.tables;

/**
 * Builds the correct tableWriter depending on the mime type
 */
import java.io.IOException;
import java.io.Writer;
import org.astrogrid.slinger.mime.MimeTypes;
import uk.ac.starlink.fits.FitsTableWriter;

public class TableWriterMaker {
   
   public TableWriter makeTableWriter(String mimeType, Writer target, String title, String comment) throws IOException {
      if (mimeType.toLowerCase().equals(MimeTypes.HTML)) {
         return new HtmlTableWriter(target, title, comment);
      }
      if (mimeType.toLowerCase().equals(MimeTypes.CSV)) {
         return new XsvTableWriter(target, title, ",");
      }
      if (mimeType.toLowerCase().equals(MimeTypes.TSV)) {
         return new XsvTableWriter(target, title, "\t");
      }
      if (mimeType.toLowerCase().equals(MimeTypes.VOTABLE)) {
         return new VoTableWriter(target, title);
      }
//      if (mimeType.toLowerCase().equals(MimeTypes.FITS)) {
//         return new StilStarTableWriter(new FitsTableWriter(), target);
//      }
      else {
         throw new UnsupportedOperationException("Don't have a writer for mime type '"+mimeType+"'");
      }
   }
}
