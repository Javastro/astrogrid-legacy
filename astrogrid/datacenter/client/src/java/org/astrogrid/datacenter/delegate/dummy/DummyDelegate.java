/*
 * $Id: DummyDelegate.java,v 1.1 2003/11/14 00:36:40 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.dummy;

import org.astrogrid.datacenter.delegate.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.QueryIdHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
   
/**
 * An implementation of the ConeSearcher and AdqlQuerier implementations that validates inputs and
 * returns valid results, but does not call any datacenter services.
 * Provided for unit and integration test purposes, so applications can run
 * against a realistic 'data center' without having to set one up.
 *
 * @todo not properly tidied after It04 refactoring
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class DummyDelegate implements AdqlQuerier, ConeSearcher
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

   /** Sample results file */
   public static final String RESULTSFILENAME = "ExampleResults.xml";
   
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
    * Simple cone-search call.
    * @param ra Right Ascension in decimal degrees, J2000
    * @param dec Decliniation in decimal degress, J2000
    * @param sr search radius.
    * @return VOTable
    * @todo returns set example results, not those in the given ra/dec
    */
   public Element coneSearch(double ra, double dec, double sr) throws DatacenterException
   {
      //hmmm needs to do more than this if the results are going to be realistic...
      return getSampleResults();
   }
   
   
   /**
    * Checks that the given ADQL is valid (makes a query from it), returns an
    * example VOTable supplied in this package
    */
   
   
   public Element doQuery(Element adql) throws RemoteException
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
   public Element makeQuery(Element adql)
   {
      setStatus(QueryStatus.CONSTRUCTED);

         String response =
            "<"+ResponseHelper.QUERY_CREATED_RESP_TAG+">"+
            QueryIdHelper.makeQueryIdTag(QUERY_ID)+
            "</"+ResponseHelper.QUERY_CREATED_RESP_TAG+">";

         return DocHelper.wrap(response).getDocumentElement();
   }

   /**
    * General purpose asynchronous query database.  Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param user for authorisation/authentication
    * @param pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * response document including the query id that corresponds to that query.
    * @see startAdqlQuery
    * @see registerListener
    * @see setResultsDestination
    */
   public DatacenterQuery makeQuery(Select adql) throws DatacenterException
   {
      return new DummyQuery(this, QUERY_ID);
   }
   
   /**
    * DUmmy starts query method - returns an example 'started' responses
    */
   public void startQuery(String queryId)
   {
      setStatus(QueryStatus.RUNNING_QUERY);
   }

   /**
    * private method for loading the example results document
    */
   public static Element getSampleResults()
   {
      //load example response votable
      URL url = null;
      try
      {
         url = DummyDelegate.class.getResource(RESULTSFILENAME);
         Document resultsDoc = XMLUtils.newDocument(url.openConnection().getInputStream());

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
    * Returns the url to the sample votable
    */
   public URL getResultsAndClose(String id) throws DatacenterException
   {
      if (id.equals(QUERY_ID))
      {
         return getClass().getResource(RESULTSFILENAME);
      }
      else
      {
         throw new DatacenterException("Unknown Query ID: '"+id+"'");
      }
   }


   /**
    * returns example metadata file.
    */
   public Metadata getMetadata() throws DatacenterException
   {
      try
      {
         URL url = getClass().getResource("ExampleMetadata.xml");
         return new Metadata(XMLUtils.newDocument(url.openConnection().getInputStream()));
      }
      catch (ParserConfigurationException e)
      {
         //rethrow as runtime as a setup error
         throw new RuntimeException("XML parser not configured: ("+e+") Dummy delegate failed to load example metadata");
      }
      catch (SAXException e)
      {
         //rethrow as runtime as the file is static and shouldn't be invalid
         throw new RuntimeException("Example dummy metadata is invalid:"+e);
      }
      catch (IOException e)
      {
         //rethrow as runtime as the file should just be there
         throw new RuntimeException("Fault opening dummy metadata:"+e);
      }
   }

   /**
    * Sets the status, does update, etc.
    * Public so that test tools can reach it.
    */
   public void setStatus(QueryStatus newStatus)
   {
      lastStatus = newStatus;

      //fireStatusChanged(QUERY_ID, lastStatus);
   }


   /**
    * Abort a  query.  Does nothing
    */
   public void abortQuery(String queryId) throws DatacenterException
   {
   }
   
   /**
    * Blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc)
    * @param ADQL
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(Certification user, String resultsFormat, Select adql) throws DatacenterException
   {
      return new DatacenterResults(getSampleResults());
   }
   
   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    * <p>
    * Returns a random number between 0 and 100...
    */
   public int countQuery(Certification user, Select adql) throws DatacenterException
   {
      return random.nextInt(100);
   }
   
   /**
    * Give the datacenter the location of the service that the results should
    * be sent to when complete.  If none is given, a default one might be used,
    * or the service may throw an exception when attempting to start the
    * query
    */
   protected void setResultsDestination(String queryId, URL myspace) throws DatacenterException
   {
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    * @param givenId an id for the query is assigned here rather than
    * generated by the server
    */
   public DatacenterQuery makeQuery(Select adql, String givenId) throws DatacenterException
   {
      return null;
   }
   
   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int countQuery(Select adql) throws DatacenterException
   {
      return 0;
   }
   
   /**
    * Blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc)
    * @param ADQL
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(String resultsFormat, Select adql) throws DatacenterException
   {
      return null;
   }
   
   /**
    * Polls the service and asks for the current status.  Used by clients that
    * spawn asynchronous queries but cannot publish a url for the service to
    * send status updates to.
    */
   protected QueryStatus getQueryStatus(String queryId) throws DatacenterException
   {
      return null;
   }
   
   
}

/*
$Log: DummyDelegate.java,v $
Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.12  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.11  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.10  2003/09/18 13:12:14  nw
renamed delegate methods to match those in web service

Revision 1.9  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

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


