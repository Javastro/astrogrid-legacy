/*
 * $Id: AxisDataServer.java,v 1.7 2003/11/21 17:37:56 nw Exp $
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
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.axisdataserver.types.QueryId;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
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

public class AxisDataServer extends ServiceServer implements org.astrogrid.datacenter.axisdataserver.AxisDataServer {
    
    public static Log log = LogFactory.getLog(AxisDataServer.class);
    
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
    * @todo - improve type of this method. - return an object model? or element?
    */
   public String getMetadata(Object ignored)
   {
       BufferedReader is = null;
       PrintWriter os = null;
       try {
        is = new BufferedReader( new InputStreamReader (super.getMetadataStream()));
        StringWriter sw = new StringWriter();
        os = new PrintWriter(sw);
        String line = null;
        while ( (line = is.readLine()) != null) {
            os.println(line);
         }
      return sw.toString();
       } catch (IOException e) {
           throw new RuntimeException("Could not access metadata",e);
       } finally {
           try {
               if (is != null) {
                   is.close();
               }
               if (os != null) {
                   os.close();
               }
           } catch (IOException notBothered) {
           }
       }
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
   public String doQuery(String resultsFormat,  Query q) throws IOException
   {
       if (resultsFormat == null || resultsFormat.length() == 0) {
           throw new IllegalArgumentException("Empty parameter for results format");
       }
      if (!resultsFormat.toLowerCase().equals("votable")) {
            log.error("Can only produce votable results");
            throw new IllegalArgumentException("Can only produce votable results");
      }

        DatabaseQuerier querier = null;
      try
      {
          querier = DatabaseQuerierManager.createQuerier(q);
          QueryResults results = querier.doQuery();
          querier.setStatus(QueryStatus.RUNNING_RESULTS);          
         Element result = ResponseHelper.makeResultsResponse(
                querier,
                results.toVotable().getDocumentElement()
            ).getDocumentElement();         
         return XMLUtils.ElementToString(result);        
      }
      catch (SAXException e)
      {
         throw new DatacenterException("Failed to convert results to VOTable", e);
      } finally {
          if (querier != null) {
              querier.close();
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
   public QueryId  makeQuery(Query q) throws IOException
   {

      DatabaseQuerier querier = DatabaseQuerierManager.createQuerier(q);

      //construct reply with id in it...
      QueryId result = new QueryId();
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
   public QueryId makeQueryWithId(Query q, String assignedId) throws QueryException, IOException, SAXException
   {
      
       if (assignedId == null || assignedId.length() == 0) {
           throw new IllegalArgumentException("Empty assigned id");
       }
      DatabaseQuerier querier = DatabaseQuerierManager.createQuerier(q, assignedId);

      //construct reply with id in it...
      QueryId result = new QueryId();      
      result.setId(querier.getHandle());
      return result;
   }

   /**
    * Sets where the results are to be sent
    * @soap
    */
   public void setResultsDestination(QueryId queryId, URI resultsDestination)
   {
       if (resultsDestination == null ) {
           throw new IllegalArgumentException("Empty results destination");
       }
      DatabaseQuerier querier = getQuerier(queryId);
      
      querier.setResultsDestination(resultsDestination.toString());
   }

   /**
    * Starts an existing query running
    * @soap
    * @todo - use a thread pool system here - threads are resource-hungry.
    */
   public void startQuery(QueryId id)
   {
      DatabaseQuerier querier = getQuerier(id);

      Thread queryThread = new Thread(querier);
      queryThread.start();
   }

   /**
    * Checks the query specified by the given id
    * If the query has finished, returns the URL of where the results are
    * <p>
    * @soap
    */
   public String getResultsAndClose(QueryId queryId)
   {
      DatabaseQuerier querier =getQuerier(queryId);

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
   public void abortQuery(QueryId queryId)
   {
      DatabaseQuerier querier = getQuerier(queryId);
      if (querier != null) {
        querier.abort();
      }
   }

   /**
    * Returns the status of the service with the given id
    * <p>
    * @soap
    */
   public String getStatus(QueryId queryId)
   {
      return getQuerier(queryId).getStatus().toString();
   }

   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerWebListener(QueryId queryId , URI uri) throws RemoteException
   {

       try {
       URL u = new URL(uri.toString());
      DatabaseQuerier querier = getQuerier(queryId);

      querier.registerListener(new WebNotifyServiceListener(u));
       } catch (MalformedURLException e) {
           throw new RemoteException("Malformed URL",e);
       }
   }

   /**
    * Register the given URL as a service to be notified when the status changes
    * <p>
    * @soap
    */
   public void registerJobMonitor(QueryId queryId, URI uri) throws RemoteException 
   {
       // check we can create an URL first..
       try {
       URL u = new URL(uri.toString());
      DatabaseQuerier querier = getQuerier(queryId);

      querier.registerListener(new WebNotifyServiceListener(u));
       } catch (MalformedURLException e) {
           throw new RemoteException("Malformed URL",e);
       }
   }

}



