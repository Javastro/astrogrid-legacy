/*
 * $Id: WebQueryDelegate.java,v 1.3 2004/03/07 00:33:50 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.agws;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.axis.types.URI;
import org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingStub;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.DelegateQueryListener;
import org.astrogrid.datacenter.query.QueryState;


/**
 * Implementation of a query instance, represening the query at the
 * server side.  Package private.
 */
class WebQueryDelegate implements DatacenterQuery {
   
   private String queryId;
   /** Generated binding code that mirrors the service's methods */
   private AxisDataServerSoapBindingStub binding;
   
   public WebQueryDelegate(AxisDataServerSoapBindingStub delegateBinding, String id) {
      this.queryId = id;
      this.binding = delegateBinding;
   }
   
   /**
    * @see DatacenterQuery.getResultsAndClose()
    */
   public DatacenterResults getResultsAndClose() throws IOException {
      return new DatacenterResults(new String[] {binding.getResultsAndClose(queryId)});
   }
   
   /**
    * Give the datacenter the location of the service that the results should
    * be sent to when complete.  If none is given, a default one might be used,
    * or the service may throw an exception when attempting to start the
    * query
    */
   public void setResultsDestination(String resultsDestination) throws RemoteException {
      binding.setResultsDestination(queryId,buildURI(resultsDestination));
   }
   
   /**
    * Starts the query running - eg submits SQL to database.
    */
   public void start() throws RemoteException {
      binding.startQuery(queryId);
   }
   
   /**
    * Polls the service and asks for the current status.  Used by clients that
    * spawn asynchronous queries but cannot publish a url for the service to
    * send status updates to.
    */
   public QueryState getStatus() throws RemoteException {
      return QueryState.getFor(binding.getStatus(queryId));
   }
   
   /**
    * Returns some kind of handle to the query
    */
   public String getId() {
      return queryId;
   }
   
   /**
    * Tells the server to stop running the query.  Don't use the query id after this...
    */
   public void abort() throws RemoteException {
      binding.abortQuery(queryId);
   }
   
   /**
    * Register a listener with this query delegate, to listen to local
    * status changes
    * @todo implement for JobMonitor and WebListener
    */
   public void registerListener(DelegateQueryListener newListener) {
      throw new UnsupportedOperationException("You cannot register listeners using this method for web services");
   }
   
   public void registerJobMonitor(URL url) throws RemoteException {
      binding.registerJobMonitor(queryId, buildURI(url));
   }
   
   public void registerWebListener(URL url) throws RemoteException {
      binding.registerWebListener(queryId, buildURI(url));
   }
   
   /** helper method to build a URI from a URL.
    * @todo could replace URL with URI altogether - a better class in some ways, as URL will barf at protocols it doesn't know. (myspace://)
    * @todo switch to java.net.URI?
    * @param url
    * @return valid apache uri
    */
   private URI buildURI(String loc) {
      try {
         URI uri = new URI(loc);
         return uri;
      } catch (URI.MalformedURIException e) {
         // very unlikely to happen - just come from a URL, which is stricter..
         throw new IllegalArgumentException("Malformed URI: " + e.getMessage());
      }
   }
   
   private URI buildURI(URL url) {
      return buildURI(url.toString());
   }


}

/*
$Log: WebQueryDelegate.java,v $
Revision 1.3  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.2  2004/02/15 23:09:04  mch
Naughty Big Lump of changes: Updated myspace access, applicationcontroller interface, some tidy ups.

Revision 1.1  2004/01/05 19:06:26  mch
Introduced ApplicationController interface

Revision 1.13  2003/12/16 16:19:27  mch
minor exception check

Revision 1.12  2003/12/15 14:30:50  mch
Fixes to load doc from string not file, and use correct version of adql

Revision 1.11  2003/12/03 19:37:03  mch
Introduced DirectDelegate, fixed DummyQuerier

Revision 1.10  2003/12/01 16:53:16  nw
dropped _QueryId, back to string

Revision 1.9  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.8  2003/11/25 15:47:17  mch
Added certification

Revision 1.7  2003/11/25 11:54:41  mch
Added framework for SQL-passthrough queries

Revision 1.6  2003/11/21 17:30:19  nw
improved WSDL binding - passes more strongly-typed data

Revision 1.5  2003/11/18 14:25:23  nw
altered types to fit with new wsdl

Revision 1.4  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

Revision 1.3  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.2  2003/11/17 12:12:28  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.4  2003/11/06 22:04:48  mch
Temporary fixes to work with old version of AxisDataServer

Revision 1.3  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.2  2003/10/13 14:13:47  nw
massaged one method to fit wih new delegate. still lots more to do here
- don't understand intentions here myself

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/

