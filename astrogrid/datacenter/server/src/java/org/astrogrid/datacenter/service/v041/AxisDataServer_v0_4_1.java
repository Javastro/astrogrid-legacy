/*
 * $Id: AxisDataServer_v0_4_1.java,v 1.6 2004/03/16 01:21:08 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.v041;

import java.io.StringWriter;
import java.net.URL;
import java.util.Hashtable;
import org.apache.axis.AxisFault;
import org.apache.axis.types.URI;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.axisdataserver.types.Language;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.queriers.JobNotifyServiceListener;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.TargetIndicator;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.astrogrid.store.Agsl;

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
   
   //has to keep a separate list of queriers as there is a point between creating one and submitting it
   //where it's not in any manager
   Hashtable madeQueriers = new Hashtable();

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
   public String doQuery(String resultsFormat,  Query axisQ)  throws AxisFault {
      
      try {
         
         if (resultsFormat == null || resultsFormat.length() == 0)  {
            log.error("Empty parameter for results format");
            throw makeFault("Empty parameter for results format");
         }
         if (!resultsFormat.toLowerCase().equals(QuerySearcher.VOTABLE.toLowerCase()))  {
            log.error("Can only produce votable results");
            throw makeFault("Can only produce votable results");
         }
      
         //ask as an adql select
         StringWriter sw = new StringWriter();
         server.askQuery(Account.ANONYMOUS,
                          new AdqlQuery(axisQ.getQueryBody()),
                          new TargetIndicator(sw),
                          QueryResults.FORMAT_VOTABLE);
         return sw.toString();
         
      }
      catch (Throwable e) {
         throw makeFault(SERVERFAULT, "Datacenter error", e);
      }
   }
   
   
   /**
    * Creates an asynchronous query, returns the query id
    * Does not start the query running - may want to register listeners with
    * it first
    * <p>
    * @soap
    */
   public String  makeQuery(Query axisQ) throws AxisFault {

      try {
         Querier querier = Querier.makeQuerier(Account.ANONYMOUS, new AdqlQuery(axisQ.getQueryBody()), null, QueryResults.FORMAT_VOTABLE);
         madeQueriers.put(querier.getId(), querier);
         return querier.getId();
      }
      catch (Exception e) {
         throw makeFault(SERVERFAULT, "Failed to make Query", e);
      }
   }
   
   /**
    * Creates an asynchronous query with the given id, returning that id
    * Does not start the query running - may want to register listeners with
    * it first.
    * NB Assigned ID now ignored
    * <p>
    * @soap
    */
   public String makeQueryWithId(Query axisQ, String assignedId) throws AxisFault {
      
      try {
         Querier querier = Querier.makeQuerier(Account.ANONYMOUS, new AdqlQuery(axisQ.getQueryBody()), null, QueryResults.FORMAT_VOTABLE);
         madeQueriers.put(querier.getId(), querier);
         return querier.getId();
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
         Querier querier = (Querier) madeQueriers.get(queryId);
         querier.setResultsTarget(new TargetIndicator(new Agsl(resultsDestination.toString())));
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

      /*
      try {
         Querier querier =server.getQuerier(queryId);
         //has querier finished?
         if (!querier.getStatus().getState().isBefore(QueryState.FINISHED)) {
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
       */
      throw makeFault("Not supporting this method");
      
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
         server.submitQuerier(querier);
      }
      catch (Throwable e) {
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
         Querier querier = (Querier) madeQueriers.get(queryId);
         if (querier == null) {
            throw new IllegalArgumentException("Unknown Query ID '" + queryId+"'");
         }
         
         querier.addListener(new JobNotifyServiceListener(queryId, u));
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
Revision 1.6  2004/03/16 01:21:08  mch
Fixed unknown query id on register job monitor

Revision 1.5  2004/03/14 04:13:04  mch
Wrapped output target in TargetIndicator

Revision 1.4  2004/03/14 00:39:55  mch
Added error trapping to DataServer and setting Querier error status

Revision 1.3  2004/03/13 23:38:46  mch
Test fixes and better front-end JSP access

Revision 1.2  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

Revision 1.3  2004/03/09 22:57:18  mch
Pass streams to DataServer instead getting string results (less lumpy, able to close SQL)

Revision 1.2  2004/03/08 13:24:35  mch
Fixes to reintroduce ADQL querying and cone searches

Revision 1.1  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.30  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

