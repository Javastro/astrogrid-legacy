/*
 * $Id: DummyQueryResults.java,v 1.1 2003/08/26 16:41:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

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
   public Document toVotable() throws IOException
   {
      try
      {
         DocumentBuilderFactory  factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         return builder.parse(new InputSource( getInputStream() ));
      }
      catch (org.xml.sax.SAXException se)
      {
         //rethrow as io exception
         IOException ioe = new IOException("SAXEception: se.getMessage()");
         ioe.setStackTrace(se.getStackTrace());
         throw ioe;
      }
      catch (javax.xml.parsers.ParserConfigurationException pce)
      {
         //should never happen, so rethrow as runtime (is this naughty?)
         throw new RuntimeException(pce);
      }

   }

}

