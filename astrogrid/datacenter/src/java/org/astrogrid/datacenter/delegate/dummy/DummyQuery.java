/*
 * $Id DummyQuery.java $
 *
 */

package org.astrogrid.datacenter.delegate.dummy;

import java.net.URL;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DelegateQueryListener;


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
   
   public URL getResultsAndClose() throws DatacenterException
   {
      return getClass().getResource(DummyDelegate.RESULTSFILENAME);
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
   
   public void registerJobListener(URL url)
   {
   }
   
   public void registerWebListener(URL url)
   {
   }
   
}

/*
$Log: DummyQuery.java,v $
Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
