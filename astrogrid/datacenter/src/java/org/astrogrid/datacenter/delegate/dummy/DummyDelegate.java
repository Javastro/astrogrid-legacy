/*
 * $Id: DummyDelegate.java,v 1.8 2003/09/16 12:53:40 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.dummy;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.QueryIdHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.delegate.DatacenterStatusListener;
import org.astrogrid.datacenter.delegate.WebNotifyServiceListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
   private static java.util.Random random = new java.util.Random();

   /** Status string - initialising/starting */
   public static final String STARTING = "Starting";
   /** Status string - query submitted, waitinf for response */
   public static final String WAITING = "Waiting for Server";
   /** Status string - resposne received, being processed */
   public static final String POST_PROCESSING = "Processing Results";
   /** Status string - response dealt with */
   public static final String FINISHED = "Finished";

   /** Pretend service id */
   public static final String QUERY_ID = "DummyId";

   /** Last status set */
   private QueryStatus lastStatus = QueryStatus.CONSTRUCTED;

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
    */
   public Element query(Element adql) throws RemoteException
   {
      setStatus(QueryStatus.STARTING);

      setStatus(QueryStatus.RUNNING_QUERY);

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

      setStatus(QueryStatus.RUNNING_RESULTS);

      Element results = getSampleResults();

      setStatus(QueryStatus.FINISHED);

      return results;
   }

   /**
    * Dummy create query method - returns an example 'created' response
    */
   public Element makeQuery(Element adql) throws RemoteException
   {
      setStatus(QueryStatus.CONSTRUCTED);

         String response =
            "<"+ResponseHelper.QUERY_CREATED_RESP_TAG+">"+
            QueryIdHelper.makeQueryIdTag(QUERY_ID)+
            "</"+ResponseHelper.QUERY_CREATED_RESP_TAG+">";

         return DocHelper.wrap(response).getDocumentElement();
   }

   /**
    * DUmmy starts query method - returns an example 'started' responses
    */
   public Element startQuery(String queryId) throws RemoteException
   {
      setStatus(QueryStatus.RUNNING_QUERY);

      String response =
            "<"+ResponseHelper.QUERY_STARTED_RESP_TAG+">"+
            QueryIdHelper.makeQueryIdTag(QUERY_ID)+
            "</"+ResponseHelper.QUERY_STARTED_RESP_TAG+">";

      return DocHelper.wrap(response).getDocumentElement();
   }

   /**
    * private method for loading the example results document
    */
   private Element getSampleResults()
   {
      //load example response votable
      URL url = null;
      try
      {
         setStatus(QueryStatus.RUNNING_RESULTS);

         url = getClass().getResource("ExampleResults.xml");
         Document resultsDoc = XMLUtils.newDocument(url.openConnection().getInputStream());

         setStatus(QueryStatus.FINISHED);

         return resultsDoc.getDocumentElement();
      }
      catch (Exception e)
      {
         if (url == null)
         {
            throw new RuntimeException("ExampleResults.xml not found",e);
         }
         else
         {
            throw new RuntimeException("Failed to create dummy/example results",e);
         }
      }
   }

   /**
    * Returns a sample votable
    */
   public Element getResults(String id) throws RemoteException
   {
      if (id.equals(QUERY_ID))
      {
         return getSampleResults();
      }
      else
      {
         return ResponseHelper.makeUnknownIdResponse(id).getDocumentElement();
      }
   }


   /**
    * Returns a random number between 0 and 100...
    */
   public int adqlCountDatacenter(Element adql)
   {
      return random.nextInt(100);
   }

   /**
    * returns example voregistry - formatted metadata file.
    */
   public Element getRegistryMetadata() throws IOException
   {
         //load example response votable
      try
      {
         URL url = getClass().getResource("ExampleVoRegistry.xml");
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
    * Sets the status, does update, etc
    */
   public void setStatus(QueryStatus newStatus)
   {
      lastStatus = newStatus;

      fireStatusChanged(QUERY_ID, lastStatus);
   }

   /**
    * Returns unknown
    */
   public QueryStatus getQueryStatus(String queryId)
   {
      return lastStatus;
   }

   /**
    * Registers a listener with this service.
    */
   public void registerListener(String queryId, DatacenterStatusListener listener)
   {
      addListener(listener);
   }


}

/*
$Log: DummyDelegate.java,v $
Revision 1.8  2003/09/16 12:53:40  mch
DocHelper.wrap now throws IllegalArgumentException (runtime error) rather than SAXException, as XML is all softwired

Revision 1.7  2003/09/15 22:56:02  mch
Test fixes

Revision 1.6  2003/09/15 22:38:42  mch
Split spawnQuery into make and start, so we can add listeners in between

Revision 1.5  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.4  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.3  2003/09/15 16:11:44  mch
Fixes to handle updates when multiple queries are running through one delegate

Revision 1.2  2003/09/15 15:22:20  mch
Implemented asynch queries; improved dummy results

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


