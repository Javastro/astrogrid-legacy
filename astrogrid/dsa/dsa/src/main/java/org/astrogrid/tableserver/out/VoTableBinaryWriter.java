/*
 * $Id: VoTableBinaryWriter.java,v 1.1 2009/05/13 13:20:51 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.out;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
/*
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
*/
import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.metadata.StdDataTypes;
import org.astrogrid.dataservice.metadata.VoTypes;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.ucd.UcdVersions;
import org.astrogrid.ucd.UcdException;
import org.astrogrid.io.Piper;

/*
import uk.ac.starlink.votable.VOTableWriter;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.votable.DataFormat;

import uk.ac.starlink.ttools.task.VotCopy;
*/
import uk.ac.starlink.ttools.copy.VotCopyHandler;



/**
 * For writing out tables in votable-binary format.
 *
 * @author K Andrews
 */

public class VoTableBinaryWriter implements TableWriter {
   
   protected static final Log log = 
         LogFactory.getLog(VoTableBinaryWriter.class);

   protected VoTableWriter voTableWriter = null;


   /**
    * Construct this wrapping the given stream.
    */
   public VoTableBinaryWriter(TargetIdentifier target, String title, Principal user) throws IOException {
      
      target.setMimeType(MimeTypes.VOTABLE_BINARY);

      VotCopyHandler handler = new VotCopyHandler(true, 
          uk.ac.starlink.votable.DataFormat.BINARY, true, null, false, null );

      // Wrap the real output stream from the target
      OutputStream tempOutputStream = new VotCopyOutputStream(
          new BufferedOutputStream(target.openOutputStream()), handler);

      tempOutputStream = new java.io.BufferedOutputStream(tempOutputStream);

      voTableWriter = new VoTableWriter(tempOutputStream, title, user);
   }

   public void writeErrorTable(String errorDesc) throws IOException {
     voTableWriter.writeErrorTable(errorDesc);
   }

   public void open() throws IOException {
     voTableWriter.open();
   }
   
   /** Produces text/html */
   public String getMimeType() {
      return MimeTypes.VOTABLE_BINARY;
   }
   
   /** Start body - writes out header and preps col array */
   public void startTable(ColumnInfo[] colinfo) throws IOException {
      voTableWriter.startTable(colinfo);
   }
   
   /** Writes the given array of values out */
   public void writeRow(Object[] colValues) throws IOException {
      voTableWriter.writeRow(colValues);
   }

   public void endTable() throws IOException {
      voTableWriter.endTable();
      /*
      log.debug("Ending table");
      try {
         tempOutputStream.close();
        log.debug("Got here 1");
        StarTableFactory starTableFactory = new StarTableFactory();
        log.debug("Got here 2");
        VOTableBuilder builder = new VOTableBuilder();
        log.debug("Got here 3");
        StarTable starTable = 
              starTableFactory.makeStarTable(inputStream, builder);
        log.debug("Got here 4");
        VOTableWriter writer = new VOTableWriter(DataFormat.BINARY, true);
        log.debug("Got here 5");
           writer.writeStarTable(starTable, outputStream);
         inputStream.close();
        log.debug("Got here 6");

      }
      catch (Exception e) {
        // TOFIX WHAT TO DO HERE?
        log.debug("AAAAAAAAAAAAAAAAAAAAAAAAGH");
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAGH");
        log.debug(e.getMessage());
      }
      */
   }
   
   /** Closes writer - writes out the closing tags and closes wrapped stream
    */
   public void close() throws IOException {
      voTableWriter.close();
   }
   
   /** Abort writes out a line to show the table is incomplete */
   public void abort() throws IOException {
      voTableWriter.close();
      close();
   }
   
   /** Convenience method to get direct access to the output stream, so that we can pipe votables direct */
   /*
   public Writer getOut() {
      return printOut;
   }
   */
}
