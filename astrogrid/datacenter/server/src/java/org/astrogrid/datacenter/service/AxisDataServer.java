/*
 * $Id: AxisDataServer.java,v 1.2 2003/11/17 12:16:33 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * This class is the public web interface, called by Axis
 * when Axis receives the SOAP message from the client. It is a singleton
 * - all state depends on the DataService instances, one of
 * which is created for each data query call.
 *<p>
 * @soap this should be configured to be constructed once (not on every call).
 * <p>
 * This is what was DatasetAgent in the It02 but with the extensions to handle
 * all the service methods required (eg metadata etc)
 *
 *NW- extended to implement generated interface - acts as an assertion that service implementation
 *fits with WSDL description.
 * MCH - temporarily commented this out until I've learned to generate the WSDL + service implementation, as otherwise this
 * circular dependency breaks every time I modify this interface and try to run unit tests :-)
 * @author M Hill
 * @author Noel Winstanly
 *
 */

public class AxisDataServer extends ServiceServer //MCH see above implements org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServer
{
   /**
    * Initialises the configuration.  The configuration is loaded from the
    * classpath, rather than file, because we don't know our home directory
    * - depends on how the tomcat server is run
    * uch easier to place a config file in the WEB-INF/classes directory of
    * the axis web app.
    */
   public AxisDataServer() throws IOException
   {
      SimpleConfig.autoLoad();
   }

   /**
    * Returns the metadata file
    * NWW - at present pass in a dummy parameter - haven't worked out how to say 'no parameters please' yet.
    * @soap
    */
   public Element getMetadata(Object ignored)
   {
      return super.getMetadata();
   }


   /**
    * Carries out a full synchronous (ie blocking) query.  Note that queries
    * that take a long time might therefore cause a timeout at the client as
    * it waits for its response.
    * Returns a document
    * including the results (or location of the results)
    * <p>
    * @soap
    */
   public Element doQuery(String resultsFormat,  Select adql) throws IOException
   {
      Log.affirm(resultsFormat.toLowerCase().equals("votable"), "Can only produce votable results");
      DatabaseQuerier querier = DatabaseQuerierManager.createQuerier(adql, null);

      QueryResults results = querier.doQuery();

      querier.setStatus(QueryStatus.RUNNING_RESULTS);

      try
      {
         Element result = ServiceResponseHelper.makeResultsResponse(
            querier,
            results.toVotable().getDocumentElement()
         ).getDocumentElement();
         
         querier.close();
         return result;
         
      }
      catch (SAXException e)
      {
         throw new DatacenterException("Failed to convert results to VOTable", e);
      }
   }

   /**
    * Creates an asynchronous query, returns the query id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * @soap
    */
   public String makeQuery(Select adql) throws IOException
   {
      DatabaseQuerier querier = DatabaseQuerierManager.createQuerier(adql, null);

      //construct reply with id in it...
      return querier.getHandle();
   }

   /**
    * Creates an asynchronous query with the given id, returning that id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * NWW - changed name (was makeQuery). Method overloading is tricky for soap.
    * @soap
    */
   public String makeQueryWithId(Select adql, String assignedId) throws QueryException, IOException, SAXException
   {
      DatabaseQuerier querier = DatabaseQuerierManager.createQuerier(adql, assignedId);

      //construct reply with id in it...
      return querier.getHandle();
   }

   /**
    * Sets where the results are to be sent
    * @soap
    */
   public void setResultsDestination(String queryId, String resultsDestination)
   {
      DatabaseQuerier querier = DatabaseQuerierManager.getQuerier(queryId);
      
      querier.setResultsDestination(resultsDestination);
   }

   /**
    * Starts an existing query running
    * @soap
    */
   public void startQuery(String id)
   {
      //get id from soap body
      DatabaseQuerier querier = DatabaseQuerierManager.getQuerier(id);

      Thread queryThread = new Thread(querier);
      queryThread.start();
   }

   /**
    * Checks the query specified by the given id
    * If the query has finished, returns the URL of where the results are
    * <p>
    * @soap
    */
   public String getResultsAndClose(String queryId)
   {

      DatabaseQuerier querier = DatabaseQuerierManager.getQuerier(queryId);

      //has querier finished?
      if (!querier.getStatus().isBefore(QueryStatus.FINISHED))
      {
         return querier.getResultsLoc().toString();
      }
      else
      {
         return null;
      }

   }

   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if there are any problems doing this it's a server-end problem.
    * <p>
    * @soap
    */
   public void abortQuery(String queryId)
   {
      DatabaseQuerier querier = DatabaseQuerierManager.getQuerier(queryId);

      querier.abort();
   }

   /**
    * Returns the status of the service with the given id
    * <p>
    * @soap
    */
   public String getStatus(String queryId)
   {
      return DatabaseQuerierManager.getQuerier(queryId).getStatus().toString();
   }

   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerWebListener(String queryId, String url) throws MalformedURLException
   {
      DatabaseQuerier querier = DatabaseQuerierManager.getQuerier(queryId);

      querier.registerListener(new WebNotifyServiceListener(new URL(url)));
   }

   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerJobMonitor(String queryId, String url) throws MalformedURLException
   {
      DatabaseQuerier querier = DatabaseQuerierManager.getQuerier(queryId);

      querier.registerListener(new WebNotifyServiceListener(new URL(url)));
   }

}



