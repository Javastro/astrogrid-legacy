/*
 * $Id: AxisDataServer_v0_4_1.java,v 1.1 2004/03/08 00:31:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.axis.AxisFault;
import org.apache.axis.types.URI;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.axisdataserver.types.Language;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.datacenter.snippet.ResponseHelper;
import org.astrogrid.io.Piper;
import org.astrogrid.util.DomHelper;
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

public class AxisDataServer_v0_4_1 extends AxisDataServer implements org.astrogrid.datacenter.axisdataserver.AxisDataServer  {
   

   /**
    * Returns the metadata file
    * NWW - at present pass in a dummy parameter - haven't worked out how to say 'no parameters please' yet.
    * @soap
    */
   public String getMetadata(Object ignored) throws AxisFault {
      return getMetadata();
   }

   /**
    * Carries out a full synchronous (ie blocking) adql query.  Note that queries
    * that take a long time might therefore cause a timeout at the client as
    * it waits for its response.
    * <p>
    * @soap
    */
   public String doQuery(String resultsFormat,  Query q)  throws AxisFault {
      
      if (resultsFormat == null || resultsFormat.length() == 0)  {
         log.error("Empty parameter for results format");
         throw makeFault("Empty parameter for results format");
      }
      if (!resultsFormat.toLowerCase().equals(FullSearcher.VOTABLE.toLowerCase()))  {
         log.error("Can only produce votable results");
         throw makeFault("Can only produce votable results");
      }
      
      //transform ADQL v0.5 query into thingamy
      //@todo
         
      //re-read as string
      return askQuery(Account.ANONYMOUS, q.toString());
   }
   
   
   /**
    * Creates an asynchronous query, returns the query id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * @soap
    */
   public String  makeQuery(Query q) throws AxisFault {

      try {
         Querier querier = QuerierManager.createQuerier(q);
         return querier.getQueryId();
      }
      catch (Exception e) {
         throw makeFault(SERVERFAULT, "Failed to make Query", e);
      }
   }
   
   /**
    * Creates an asynchronous query with the given id, returning that id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * @soap
    */
   public String makeQueryWithId(Query q, String assignedId) throws AxisFault {
      
      try {
      if (assignedId == null || assignedId.length() == 0)  {
         throw new IllegalArgumentException("Empty assigned id");
      }
      
      Querier querier = QuerierManager.createQuerier(q, assignedId);
      return querier.getQueryId();
      }
      catch (Exception e) {
         throw makeFault(SERVERFAULT, "Failed to make Query", e);
      }
      
   }
   
   /**
    * Sets where the results are to be sent
    * @soap
    */
   public void setResultsDestination(String queryId, URI resultsDestination)  throws AxisFault {
      if (resultsDestination == null )  {
         throw makeFault("Illegal Argument: Empty results destination");
      }
      try {
         Querier querier = server.getQuerier(queryId);
         querier.setResultsDestination(resultsDestination.toString());
      } catch (Exception e) {
         throw makeFault(CLIENTFAULT, e+" setting queryId ["+queryId+"] results target to "+resultsDestination, e);
      }
   }
   
   /**
    * Checks the query specified by the given id
    * If the query has finished, returns the URL of where the results are
    * <p>
    * @soap
    */
   public String getResultsAndClose(String queryId) throws AxisFault {

      try {
         Querier querier =server.getQuerier(queryId);
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
      catch (Exception e) {
         throw makeFault(SERVERFAULT, e+" getting results & closing for query ["+queryId+"]", e);
      }
         
      
   }
   
   /**
    * Aborts the query specified by the given id.  Returns
    * nothing - if there are any problems doing this it's a server-end problem.
    * <p>
    * @soap
    */
   public void abortQuery(String queryId) throws AxisFault {
      abortQuery(Account.ANONYMOUS, queryId);
   }
   
   /**
    * Starts an existing query running
    * @soap
    */
   public void startQuery(String id) throws AxisFault {
      try {
         Querier querier = server.getQuerier(id);
         Thread queryThread = new Thread(querier);
         queryThread.start();
      }
      catch (Exception e) {
         throw makeFault(SERVERFAULT, e+" starting Query ["+id+"]", e);
      }
   }
   
   /**
    * Returns the state of the query with the given id
    * <p>
    * @soap
    */
   public String getStatus(String queryId) throws AxisFault {
      //this call already throws SoapFaultException on error
      return getQueryStatus(Account.ANONYMOUS, queryId).getState().toString();
   }
   
   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerWebListener(String queryId , URI uri)  throws AxisFault {
      throw makeFault("Unsupported Operation (registerWebListener) use a later service");
   }
   
   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerJobMonitor(String queryId, URI uri) throws AxisFault  {
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
      catch (Exception e)  {
         throw makeFault(SERVERFAULT, e+" registering job monitor "+uri+" for query ["+queryId+"]", e);
      }
   }
   
   /* (non-Javadoc)
    * @see org.astrogrid.datacenter.axisdataserver.AxisDataServer#getLanguageInfo(java.lang.Object)
    */
   public Language[] getLanguageInfo(Object arg0) throws AxisFault {

      /*
      try {
         return PluginQuerier.instantiateQuerierSPI().getTranslatorMap().list();
      }
       */
      throw makeFault("Unsupported Operation: getLanguageInfo()");
   }
   
}

/*
$Log: AxisDataServer_v0_4_1.java,v $
Revision 1.1  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.30  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

