/*
 * $Id DatacenterResults.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.net.URL;

import org.astrogrid.datacenter.query.QueryState;

/**
 * Handles the interface to the query at the server.  Implementations are tightly
 * bound to delegates /service interfaces and may be inner classes
 *
 * @author M Hill
 */

public interface DatacenterQuery
{
   /**
    * Returns some kind of handle to the query
    */
   public String getId();
   /**/
   
   public void registerListener(DelegateQueryListener newListener);
   
   /** Register a job monitor with the query.
    * @deprecated - use registerWebListener
    */
   public void registerJobMonitor(URL url) throws IOException;
   
   /** Register a web listener with the query.  This is the url of a web
    * service with a particular method (not yet defined) that will be called
    * when the query status chagnes
    */
   public void registerWebListener(URL url) throws IOException;

   /**
    * Give the datacenter the location of the service that the results should
    * be sent to when complete.  If none is given, a default one might be used,
    * or the service may throw an exception when attempting to start the
    * query
    */
   public void setResultsDestination(String uri) throws IOException;

   /**
    * Starts the query running - eg submits SQL to database.
    */
   public void start() throws IOException;
   
   /**
    * Tells the server to stop running the query.  Don't use the query id after this...
    */
   public void abort() throws IOException;

   /**
    * Polls the service and asks for the current status.  Used by clients that
    * spawn asynchronous queries but cannot publish a url for the service to
    * send status updates to.
    */
   public QueryState getStatus() throws IOException;
   
   /**
    * Polls the status of the service; when the results are ready, the service
    * returns them and closes.
    * <p>
    * This is provided for clients that want to spawn an asynchronous query but do not
    * have a way of publishing a 'listener' url for status updates.
    * Note that the datacenter is not expected to hold onto results when they
    * are ready - it will immediately send those results to a store (eg Myspace)
    * - and so this method will return a URL only, not the results themselves.
    * <p>
    * After this call, the queryId cannot be used for any more operations at the
    * datacenter
    */
   public DatacenterResults getResultsAndClose() throws IOException;


}

/*
$Log: DatacenterQuery.java,v $
Revision 1.5  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.4  2004/02/15 23:09:04  mch
Naughty Big Lump of changes: Updated myspace access, applicationcontroller interface, some tidy ups.

Revision 1.3  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.2  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.2  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
