/*
 * $Id: DummyDelegate.java,v 1.1 2003/09/09 17:50:07 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.dummy;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.apache.axis.utils.XMLUtils;

import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.delegate.DatacenterStatusListener;
import org.astrogrid.datacenter.common.ServiceStatus;
/**
 * An implementation of the DatacenterDelegate that validates inputs and
 * returns valid results, but does not call any datacenter services.
 * Provided for unit and integration test purposes, so applications can run
 * against a realistic data center without having to set one up.
 *
 * @see DatacenterDelegate
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class DummyDelegate extends DatacenterDelegate
{
   /** used for generating random results... */
   protected static java.util.Random random = new java.util.Random();

   public static final String STARTING = "Starting";
   public static final String WAITING = "Waiting for Server";
   public static final String POST_PROCESSING = "Processing Results";
   public static final String FINISHED = "Processing Results";


   /** Generally speaking don't use this directly - use the factory
    * method DatacenterDelegate.makeDelegate(null), which is a
    * so that it can make decisions on new sorts
    * of datacenter delegates in the future...
    */
   public DummyDelegate()
   {
   }

   /**
    * Dummy call does nothing
    */
   public void setTimeout(int givenTimeout)
   {
      //nothing needs done
   }

   /**
    * Checks that the given ADQL is valid (makes a query from it), returns an
    * example VOTable supplied in this package
    * @todo returning a VOTable is not quite right - need to return the DOM
    * representation of the returning message which might contain admin info also
    */
   public Element adqlQuery(Element adql) throws RemoteException
   {
      fireStatusChanged(STARTING);

      fireStatusChanged(WAITING);

      //we *could* unmarshall the query here but that starts getting all
      //horribly involved with server code, so we just skip it...
      /*
      try
      {
         Query query = new Query(adql);
      }
      catch (QueryException e)
      {
         //rethrow as runtime exception - somethings gone wrong that shouldn't
         throw new RuntimeException("Query='"+adql+"',",e);
      }
       /**/

      fireStatusChanged(POST_PROCESSING);

      try
      {
         //load example response votable
         URL url = getClass().getResource("ExampleVotable.xml");
         Document resultsDoc = XMLUtils.newDocument(url.openConnection().getInputStream());

         fireStatusChanged(FINISHED);

         return resultsDoc.getDocumentElement();

      }
      catch (SAXException se)
      {
         //rethrow as runtime exception - somethings gone wrong that shouldn't
         throw new RuntimeException(se);
      }
      catch (IOException ioe)
      {
         //rethrow as runtime exception - somethings gone wrong that shouldn't
         throw new RuntimeException(ioe);
      }
      catch (ParserConfigurationException pce)
      {
         //should never happen, so rethrow as runtime (is this naughty?)
         throw new RuntimeException(pce);
      }


   }

   /**
    * Dummy spawn query method - kicks off query
    */
   public Element spawnAdqlQuery(Element adql) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }


   public Element getResults(String id) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }


   /**
    * Returns a random number between 0 and 100...
    */
   public int adqlCountDatacenter(Element adql)
   {
      return random.nextInt(100);
   }

   /**
    * returns an example metadata file.
    * @todo not supported yet.  Need to make a metadata file and return it
    */
   public Element getRegistryMetadata() throws IOException
   {
         //load example response votable
      try
      {
         URL url = getClass().getResource("ExampleVotable.xml");
         return XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();
      }
      catch (ParserConfigurationException e)
      {
         //rethrow as IOException
         throw new IOException("XML parser not configured: ("+e+") Dummy delegate failed to load example metadata");
      }
      catch (SAXException e)
      {
         //rethrow as IOException
         throw new IOException("Example dummy VoRegistry metadata is invalid:"+e);
      }
   }

   /**
    * Returns unknown
    */
   public ServiceStatus getServiceStatus(String id)
   {
      return ServiceStatus.UNKNOWN;
   }

   /**
    * Registers a web listener with this service.  It's a bit of a pain to
    * implement this properly using the dummy, so not doing it yet...
    */
   public void registerWebListener(URL listenerUrl)
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }


}

/*
$Log: DummyDelegate.java,v $
Revision 1.1  2003/09/09 17:50:07  mch
Class renames, configuration key fixes, registry/metadata methods and spawning query methods

Revision 1.6  2003/09/08 16:34:04  mch
Added documentation

Revision 1.5  2003/09/07 18:52:39  mch
Added typesafe ServiceStatus

Revision 1.4  2003/09/05 12:01:56  mch
Minor doc/error changes

Revision 1.3  2003/08/29 07:57:01  maven
- changed '&' to '&amp;'

Revision 1.2  2003/08/27 23:54:20  mch
test bug fixes

Revision 1.1  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/


