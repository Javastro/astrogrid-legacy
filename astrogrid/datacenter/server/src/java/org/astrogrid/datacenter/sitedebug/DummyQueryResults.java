/*
 * $Id: DummyQueryResults.java,v 1.2 2004/03/08 00:31:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sitedebug;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.astrogrid.datacenter.queriers.QueryResults;
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
   private URL table = null;

   private String id = null;

   /**
    * Create with some identifying mark (eg timestamp) if you want to distinguish
    * it from other results.  Loads example votable that should eb in the same
    * package
    */
   public DummyQueryResults(String someIdentifyingMark)
   {
      this.id = someIdentifyingMark;

      //example is in same directory
      table = getClass().getResource("ExampleVotable.xml");
      if (table == null)
         throw new RuntimeException("Could not find example votable");
   }

   /**
    * Returns InputStream to results raw data.  In this case to the example
    * votable file.
    */
   public InputStream getInputStream() throws IOException
   {
      return table.openConnection().getInputStream();
   }

   /**
    * Returns the example file as a DOM structure - this means the example
    * will also be validated
    */
   public Document toVotable() throws IOException, SAXException
   {
      try
      {
         return DomHelper.newDocument(getInputStream());
      }
      catch (javax.xml.parsers.ParserConfigurationException pce)
      {
         //should never happen, so rethrow as runtime (is this naughty?)
         throw new RuntimeException(pce);
      }

   }

   /**
    * Pipes the example file to the given output stream
    */
   public void toVotable(OutputStream out) throws IOException
   {
      
      InputStream in = new BufferedInputStream(getInputStream());

      byte[] block = new byte[100];
      int bytesRead = 0;

      while (in.available() > 0)
      {
         bytesRead = in.read(block);
         out.write(block, 0, bytesRead);
      }

      in.close();
      out.close();
   }
   
}

