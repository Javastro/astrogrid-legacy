/*
 * $Id: AxisDataServer.java,v 1.27 2004/03/02 01:37:20 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
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
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.snippet.ResponseHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * The implementation of the Datacenter web service
 * <p>
 * When Axis receives a SOAP message from the client it is routed to this class for processing.
 * It is a singleton
 * - all state depends on the DataService instances, one of
 * which is created for each data query call.
 *<p>
 * @soap this should be configured to be constructed once (not on every call).
 * <p>
 * This is what was DatasetAgent in the It02 but with the extensions to handle
 * all the service methods required (eg metadata etc)
 *

 * @author M Hill
 * @author Noel Winstanly
 * @modified nww - added a load of parameter checking, to ensure all requests passed on to query system are valid.
 *
 */

public class AxisDataServer extends ServiceServer implements org.astrogrid.datacenter.axisdataserver.AxisDataServer  {
   
   public static Log log = LogFactory.getLog(AxisDataServer.class);
   
//   public static String CONFIG_URL = AttomConfig.CONFIG_URL;//"java:comp/env/org.astrogrid.config.url";
   
   /**
    * Initialises the configuration.
    * extodo NW this is a quick hack to get config from JNDI, as code in AttomConfig doesn't want to work.
    * later find out why the library code isn't doing the job.
    * extodo MCH another hack to see if the SimpleConfig.loadJndi is now OK
    * I think this is now solved by the new lazyloading AttomConfig...
   public AxisDataServer()  throws IOException {

      AttomConfig.autoLoad();
   }
    */
   /**
    * Returns the metadata file
    * NWW - at present pass in a dummy parameter - haven't worked out how to say 'no parameters please' yet.
    * @soap
    * @todo - improve type of this method. - return an object model? or element?
    */
   public String getMetadata(Object ignored) {
      BufferedReader is = null;
      PrintWriter os = null;
      try  {
         is = new BufferedReader( new InputStreamReader (super.getMetadataStream()));
         StringWriter sw = new StringWriter();
         os = new PrintWriter(sw);
         String line = null;
         while ( (line = is.readLine()) != null)  {
            os.println(line);
         }
         return sw.toString();
      }
      catch (IOException e)  {
         throw new RuntimeException("Could not access metadata",e);
      }
      finally  {
         try  {
            if (is != null)  {
               is.close();
            }
            if (os != null)  {
               os.close();
            }
         }
         catch (IOException notBothered)  {
         }
      }
   }
   
   
   /**
    * Carries out a full synchronous (ie blocking) adql query.  Note that queries
    * that take a long time might therefore cause a timeout at the client as
    * it waits for its response.
    * Returns a document
    * including the results (or location of the results)
    * <p>
    * @soap
    */
   public String doQuery(String resultsFormat,  Query q) throws IOException {
      
      if (resultsFormat == null || resultsFormat.length() == 0)  {
         log.error("Empty parameter for results format");
         throw new IllegalArgumentException("Empty parameter for results format");
      }
      if (!resultsFormat.toLowerCase().equals(FullSearcher.VOTABLE.toLowerCase()))  {
         log.error("Can only produce votable results");
         throw new IllegalArgumentException("Can only produce votable results");
      }
      
      Querier querier = null;
      try {
         querier =  QuerierManager.createQuerier(q);
         QueryResults results = querier.doQuery();
         querier.setStatus(QueryStatus.RUNNING_RESULTS);
         Element result = ResponseHelper.makeResultsResponse(
            querier,
            results.toVotable().getDocumentElement()
         ).getDocumentElement();
         querier.setStatus(QueryStatus.FINISHED);
         
         return XMLUtils.ElementToString(result);
      }
      catch (SAXException e) {
         throw new DatacenterException("Failed to convert results to VOTable", e);
      }
      finally  {
         QuerierManager.closeQuerier(querier);
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
    * @todo - add our own prefix to this assigned id before using it internally to ensure its unique?
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
      Querier querier = getQuerier(queryId);
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
      Querier querier =getQuerier(queryId);
      if (querier == null) {
         throw new IllegalArgumentException("Unknown qid:" + queryId);
      }
      //has querier finished?
      if (!querier.getStatus().isBefore(QueryStatus.FINISHED)) {
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
      Querier querier = getQuerier(queryId);
      if (querier != null)  {
         querier.abort();
      }
   }
   
   /**
    * Starts an existing query running
    * @soap
    */
   public void startQuery(String id) {
      Querier querier = getQuerier(id);
      startQuery(querier);
   }
   
   /**
    * Returns the status of the service with the given id
    * <p>
    * @soap
    */
   public String getStatus(String queryId) {
      Querier querier = getQuerier(queryId);
      if (querier == null) {
         throw new IllegalArgumentException("Unknown qid:" + queryId);
      }
      return querier.getStatus().toString();
   }
   
   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerWebListener(String queryId , URI uri) throws RemoteException {
      
      try  {
         URL u = new URL(uri.toString());
         Querier querier = getQuerier(queryId);
         if (querier == null) {
            throw new IllegalArgumentException("Unknown qid" + queryId);
         }
         
         
         querier.registerListener(new WebNotifyServiceListener(u));
      }
      catch (MalformedURLException e)  {
         throw new RemoteException("Malformed URL",e);
      }
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
         Querier querier = getQuerier(queryId);
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




