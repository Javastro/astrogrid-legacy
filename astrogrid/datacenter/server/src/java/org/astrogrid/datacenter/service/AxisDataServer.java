/*
 * $Id: AxisDataServer.java,v 1.30 2004/03/07 00:33:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.axisdataserver.types.Language;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.spi.PluginQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.datacenter.snippet.ResponseHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * The implementation of the Datacenter web service for It4.1.
 * <p>
 * When Axis receives a SOAP message from the client it is routed to this class for processing.
 * It is a singleton; state comes from the Queriers.

 * @author M Hill
 * @author Noel Winstanly
 *
 */

public class AxisDataServer extends SoapDataServer implements org.astrogrid.datacenter.axisdataserver.AxisDataServer  {
   
   //overrides parent
   protected static Log log = LogFactory.getLog(AxisDataServer.class);
   
   /**
    * Returns the metadata file
    * NWW - at present pass in a dummy parameter - haven't worked out how to say 'no parameters please' yet.
    * @soap
    */
   public String getMetadata(Object ignored) {
      return getMetadataString();
   }

   /**
    * Carries out a full synchronous (ie blocking) adql query.  Note that queries
    * that take a long time might therefore cause a timeout at the client as
    * it waits for its response.
    * <p>
    * @soap
    */
   public String doQuery(String resultsFormat,  Query q)  {
      
      if (resultsFormat == null || resultsFormat.length() == 0)  {
         log.error("Empty parameter for results format");
         throw makeSoapFault("Empty parameter for results format");
      }
      if (!resultsFormat.toLowerCase().equals(FullSearcher.VOTABLE.toLowerCase()))  {
         log.error("Can only produce votable results");
         throw makeSoapFault("Can only produce votable results");
      }

      //for iteration 4.1, there is no security checking
      Querier querier = null;
      try {
         querier =  QuerierManager.createQuerier(q);
         QueryResults results = querier.doQuery();
         querier.setState(QueryState.RUNNING_RESULTS);
         Element result = ResponseHelper.makeResultsResponse(
            querier,
            results.toVotable().getDocumentElement()
         ).getDocumentElement();
         querier.setState(QueryState.FINISHED);
         
         return XMLUtils.ElementToString(result);
      }
      catch (Exception e) {
         throw makeSoapFault("Failed to convert results to VOTable", e);
      }
      finally  {
         try {
            QuerierManager.closeQuerier(querier);
         } catch (IOException ioe) {
            log.error(ioe+" closing querier "+querier,ioe);
         }
      }
   }
   
   
   /**
    * Creates an asynchronous query, returns the query id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * @soap
    */
   public String  makeQuery(Query q) throws IOException {
      
      Querier querier = QuerierManager.createQuerier(q);
      return querier.getQueryId();
   }
   
   /**
    * Creates an asynchronous query with the given id, returning that id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * @soap
    */
   public String makeQueryWithId(Query q, String assignedId) throws QueryException, IOException, SAXException {
      
      log.debug("MakeQueryWithId ["+assignedId+"]");
                   
      if (assignedId == null || assignedId.length() == 0)  {
         throw new IllegalArgumentException("Empty assigned id");
      }
      
      Querier querier = QuerierManager.createQuerier(q, assignedId);
      return querier.getQueryId();
   }
   
   /**
    * Sets where the results are to be sent
    * @soap
    */
   public void setResultsDestination(String queryId, URI resultsDestination)  {
      if (resultsDestination == null )  {
         throw new IllegalArgumentException("Empty results destination");
      }
      Querier querier = server.getQuerier(queryId);
      if (querier == null) {
         throw new IllegalArgumentException("Unknown qid: " + queryId);
      }
      try {
         querier.setResultsDestination(resultsDestination.toString());
      } catch (MalformedURLException mue) {
         throw new IllegalArgumentException("Results destination invalid: "+resultsDestination);
      }
   }
   
   
   /**
    * Checks the query specified by the given id
    * If the query has finished, returns the URL of where the results are
    * <p>
    * @soap
    */
   public String getResultsAndClose(String queryId) {
      Querier querier =server.getQuerier(queryId);
      if (querier == null) {
         throw new IllegalArgumentException("Unknown qid:" + queryId);
      }
      //has querier finished?
      if (!querier.getState().isBefore(QueryState.FINISHED)) {
         String results = querier.getResultsLoc();
         try {
            QuerierManager.closeQuerier(querier);
         } catch (IOException e){
            log.warn("Exception closing querier");
         }
         return results;
      }
      else {
         return null;
      }
      
   }
   
   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if there are any problems doing this it's a server-end problem.
    * <p>
    * @soap
    */
   public void abortQuery(String queryId) {
      abortQuery(Account.ANONYMOUS, queryId);
   }
   
   /**
    * Starts an existing query running
    * @soap
    */
   public void startQuery(String id) {
      Querier querier = server.getQuerier(id);
      Thread queryThread = new Thread(querier);
      queryThread.start();
   }
   
   /**
    * Returns the state of the query with the given id
    * <p>
    * @soap
    */
   public String getStatus(String queryId) {
      return getQueryStatus(Account.ANONYMOUS, queryId).getState().toString();
   }
   
   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerWebListener(String queryId , URI uri) throws RemoteException {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerJobMonitor(String queryId, URI uri) throws RemoteException  {
      // check we can create an URL first..
      log.debug("Querier ["+queryId+"] registering JobMonitor at "+uri);
      try  {
         URL u = new URL(uri.toString());
         Querier querier = server.getQuerier(queryId);
         if (querier == null) {
            throw new IllegalArgumentException("Unknown qid" + queryId);
         }
         
         querier.registerListener(new JobNotifyServiceListener(u));
      }
      catch (MalformedURLException e)  {
         throw new RemoteException("Malformed URL",e);
      }
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.axisdataserver.AxisDataServer#getLanguageInfo(java.lang.Object)
    */
   public Language[] getLanguageInfo(Object arg0) throws RemoteException {
      
      try {
         return PluginQuerier.instantiateQuerierSPI().getTranslatorMap().list();
      } catch (DatabaseAccessException e) {
         throw new RemoteException("Could not instantiate querier SPI",e);
      }
   }
   
}

/*
$Log: AxisDataServer.java,v $
Revision 1.30  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

