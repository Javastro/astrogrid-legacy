/*
 * $Id: DummyQueryResults.java,v 1.6 2004/03/10 02:41:11 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sitedebug;
import java.io.*;

import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * For testing only; returns a dummy votable
 *
 * @author M Hill
 */

public class DummyQueryResults implements QueryResults
{
   private String id = null;

   /**
    * Create with some identifying mark (eg timestamp) if you want to distinguish
    * it from other results.  Loads example votable that should eb in the same
    * package
    */
   public DummyQueryResults(String someIdentifyingMark)
   {
      this.id = someIdentifyingMark;

      if (getExampleStream() == null)
         throw new RuntimeException("Could not find example votable");
   }

   /** Returns any old number */
   public int getCount() {
      return 15;
   }
   
   /**
    * Returns InputStream to results raw data.  In this case to the example
    * votable file.
    */
   public InputStream getExampleStream()
   {
      //example is in same directory
      InputStream table = getClass().getResourceAsStream("ExampleVotable.xml");
      
      //sometimes it cant' find stuff int he local directory if it's in a jar - try the classpath
      if (table == null) {
         table = this.getClass().getClassLoader().getResourceAsStream("ExampleVotable.xml");
      }
      
      return table;
   }

   /**
    * Returns the example file as a DOM structure - this means the example
    * will also be validated
    */
   public Document toVotable() throws IOException, SAXException
   {
      try
      {
         return DomHelper.newDocument(getExampleStream());
      }
      catch (javax.xml.parsers.ParserConfigurationException pce)
      {
         //should never happen, so rethrow as runtime (is this naughty?)
         throw new RuntimeException(pce);
      }

   }

   /** Stream version of the writer */
   public void toVotable(OutputStream out) throws IOException {
      toVotable(new OutputStreamWriter(out));
   }

   /**
    * Pipes the example file to the given output stream
    */
   public void toVotable(Writer out) throws IOException
   {
      
      Reader in = new InputStreamReader(getExampleStream());

      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
   }
   
}

