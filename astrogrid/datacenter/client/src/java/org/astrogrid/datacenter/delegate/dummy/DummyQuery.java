/*
 * $Id DummyQuery.java $
 *
 */

package org.astrogrid.datacenter.delegate.dummy;

import java.net.URL;

import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.DelegateQueryListener;
import org.astrogrid.datacenter.query.QueryStatus;


/**
 *
 * @author M Hill
 */

public class DummyQuery implements DatacenterQuery
{
   String id = null;
   DummyDelegate delegate = null;
   
   public DummyQuery(DummyDelegate givenDelegate, String givenId)
   {
      this.id = givenId;
      this.delegate = givenDelegate;
   }
   
   public String getId()
   {
      return id;
   }
   
   public DatacenterResults getResultsAndClose() throws DatacenterException
   {
      return new DatacenterResults(new String[] {getClass().getResource(DummyDelegate.RESULTSFILENAME).toString()});
   }
   
   /**
    * Give the datacenter the location of the service that the results should
    * be sent to when complete.  If none is given, a default one might be used,
    * or the service may throw an exception when attempting to start the
    * query
    */
   public void setResultsDestination(URL myspace) throws DatacenterException
   {
   }
   
   /**
    * Starts the query running - eg submits SQL to database.
    */
   public void start() throws DatacenterException
   {
   }
   
   /**
    * Polls the service and asks for the current status.  Used by clients that
    * spawn asynchronous queries but cannot publish a url for the service to
    * send status updates to.
    */
   public QueryStatus getStatus() throws DatacenterException
   {
      return null;
   }
   
   /**
    * Tells the server to stop running the query.  Don't use the query id after this...
    */
   public void abort() throws DatacenterException
   {
   }
   
   public void registerListener(DelegateQueryListener newListener)
   {
   }
   
   public void registerJobMonitor(URL url)
   {
   }
   
   public void registerWebListener(URL url)
   {
   }
   
}

/*
$Log: DummyQuery.java,v $
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
