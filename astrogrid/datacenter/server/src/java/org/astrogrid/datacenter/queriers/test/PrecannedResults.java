/*
 * $Id: PrecannedResults.java,v 1.2 2004/03/14 02:17:07 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.test;
import java.io.*;

import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * For testing only; returns a dummy votable
 *
 * @author M Hill
 */

public class PrecannedResults extends QueryResults
{
   private String id = null;

   /**
    * Create with some identifying mark (eg timestamp) if you want to distinguish
    * it from other results.  Loads example votable that should eb in the same
    * package
    */
   public PrecannedResults(String someIdentifyingMark)
   {
      this.id = someIdentifyingMark;

//      if (getExampleStream() == null)
//         throw new RuntimeException("Could not find example votable");
   }

   /** Returns any old number */
   public int getCount() {
      return 15;
   }
   

   /**
    * Writes out a completely different file as CSV...
    */
   public void toCSV(Writer out, QuerierProcessingResults statusToUpdate) throws IOException
   {
      //example is in same directory
      InputStream csv = getClass().getResourceAsStream("ExampleResults.txt");
      
      if (csv == null) {
           throw new RuntimeException("Could not find example votable");
      }
      
      Reader in = new InputStreamReader(csv);

      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
   }
   
   /**
    * Pipes the example file to the given output stream
    */
   public void toVotable(Writer out, QuerierProcessingResults statusToUpdate) throws IOException
   {
      //example is in same directory
      InputStream table = getClass().getResourceAsStream("ExampleVotable.xml");
      
      if (table == null) {
           throw new RuntimeException("Could not find example votable");
      }
      
      Reader in = new InputStreamReader(table);
      
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
   }
   
}

