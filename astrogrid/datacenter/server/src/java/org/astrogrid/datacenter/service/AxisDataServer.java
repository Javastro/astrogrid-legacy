/*
 * $Id: AxisDataServer.java,v 1.13 2003/11/27 00:52:58 nw Exp $
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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types._language;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.axisdataserver.types._QueryId;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.snippet.ResponseHelper;
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
 * @modified nww put it back in - otherwise we don't know whether the client and server will interoperate.
 * @author M Hill
 * @author Noel Winstanly
 *
 */

public class AxisDataServer extends ServiceServer implements org.astrogrid.datacenter.axisdataserver.AxisDataServer  {
   
   public static Log log = LogFactory.getLog(AxisDataServer.class);
   
   /**
    * Initialises the configuration.
    * @todo NW this is a quick hack to get config from JNDI, as code in SImpleConfig doesn't want to work.
    * later find out why the library code isn't doing the job.
    */
   public AxisDataServer() throws IOException {
      try {
         String s = (String)new InitialContext().lookup("java:comp/env/org.astrogrid.config.url");
         System.out.println("Context value " + s);
         SimpleConfig.load(s);
      } catch (NamingException e) {
         log.warn("JNDI lookup failed");
         SimpleConfig.autoLoad();
      }
   }
   
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
   public String doQuery(String resultsFormat,  _query q) throws IOException {
      
      if (resultsFormat == null || resultsFormat.length() == 0)  {
         throw new IllegalArgumentException("Empty parameter for results format");
      }
      if (!resultsFormat.toLowerCase().equals(AdqlQuerier.VOTABLE.toLowerCase()))  {
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
         return XMLUtils.ElementToString(result);
      }
      catch (SAXException e) {
         throw new DatacenterException("Failed to convert results to VOTable", e);
      }
      finally  {
         if (querier != null)  {
            querier.close();
         }
      }
   }
   
   /**
    * Carries out a full synchronous (ie blocking) passthrough sql query.  Note that queries
    * that take a long time might therefore cause a timeout at the client as
    * it waits for its response.
    * Returns a document
    * including the results
    * <p>
    * @soap
    */
   /* @todo move elsewhere
   public String doSqlQuery(String resultsFormat,  String sql) throws IOException {
      
      if (resultsFormat == null || resultsFormat.length() == 0)  {
         throw new IllegalArgumentException("Empty parameter for results format");
      }
      if (!resultsFormat.toLowerCase().equals(AdqlQuerier.VOTABLE))  {
         log.error("Can only produce votable results");
         throw new IllegalArgumentException("Can only produce votable results");
      }
      
      SqlQuerier querier = null;
      try {
         querier = (SqlQuerier) QuerierManager.createSqlQuerier(sql);
         QueryResults results = querier.queryDatabase(sql);
         querier.setStatus(QueryStatus.RUNNING_RESULTS);
         Element result = ResponseHelper.makeResultsResponse(
            querier,
            results.toVotable().getDocumentElement()
         ).getDocumentElement();
         return XMLUtils.ElementToString(result);
      }
      catch (SAXException e) {
         throw new DatacenterException("Failed to convert results to VOTable", e);
      }
      finally  {
         if (querier != null)  {
            querier.close();
         }
      }
   }
   */
   /**
    * Creates an asynchronous query, returns the query id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * @soap
    */
   public _QueryId  makeQuery(_query q) throws IOException {
      
      Querier querier = QuerierManager.createQuerier(q);
      
      //construct reply with id in it...
      _QueryId result = new _QueryId();
      result.setId( querier.getHandle() );
      return result;
   }
   
   /**
    * Creates an asynchronous query with the given id, returning that id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * NWW - changed name (was makeQuery). Method overloading is tricky for soap.
    * @soap
    */
   public _QueryId makeQueryWithId(_query q, String assignedId) throws QueryException, IOException, SAXException {
      
      if (assignedId == null || assignedId.length() == 0)  {
         throw new IllegalArgumentException("Empty assigned id");
      }
      Querier querier = QuerierManager.createQuerier(q, assignedId);
      
      //construct reply with id in it...
      _QueryId result = new _QueryId();
      result.setId(querier.getHandle());
      return result;
   }
   
   /**
    * Sets where the results are to be sent
    * @soap
    */
   public void setResultsDestination(_QueryId queryId, URI resultsDestination) {
      if (resultsDestination == null )  {
         throw new IllegalArgumentException("Empty results destination");
      }
      Querier querier = getQuerier(queryId);
      
      querier.setResultsDestination(resultsDestination.toString());
   }
   
   /**
    * Starts an existing query running
    * @soap
    * @todo - use a thread pool system here - threads are resource-hungry.
    */
   public void startQuery(_QueryId id) {
      Querier querier = getQuerier(id);
      
      Thread queryThread = new Thread(querier);
      queryThread.start();
   }
   
   /**
    * Checks the query specified by the given id
    * If the query has finished, returns the URL of where the results are
    * <p>
    * @soap
    */
   public String getResultsAndClose(_QueryId queryId) {
      Querier querier =getQuerier(queryId);
      
      //has querier finished?
      if (!querier.getStatus().isBefore(QueryStatus.FINISHED)) {
         return querier.getResultsLoc().toString();
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
   public void abortQuery(_QueryId queryId) {
      Querier querier = getQuerier(queryId);
      if (querier != null)  {
         querier.abort();
      }
   }
   
   /**
    * Returns the status of the service with the given id
    * <p>
    * @soap
    */
   public String getStatus(_QueryId queryId) {
      return getQuerier(queryId).getStatus().toString();
   }
   
   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerWebListener(_QueryId queryId , URI uri) throws RemoteException {
      
      try  {
         URL u = new URL(uri.toString());
         Querier querier = getQuerier(queryId);
         
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
   public void registerJobMonitor(_QueryId queryId, URI uri) throws RemoteException  {
      // check we can create an URL first..
      try  {
         URL u = new URL(uri.toString());
         Querier querier = getQuerier(queryId);
         
         querier.registerListener(new WebNotifyServiceListener(u));
      }
      catch (MalformedURLException e)  {
         throw new RemoteException("Malformed URL",e);
      }
   }

/* (non-Javadoc)
 * @see org.astrogrid.datacenter.axisdataserver.AxisDataServer#getLanguageInfo(java.lang.Object)
 */
public _language[] getLanguageInfo(Object arg0) throws RemoteException {
    
        try {
            return QuerierManager.instantiateQuerierSPI().getTranslatorMap().list();
        } catch (DatabaseAccessException e) {
            throw new RemoteException("Could not instantiate querier SPI",e);
        }   
}
   
}



