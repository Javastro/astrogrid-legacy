/*
 * $Id: DatacenterDelegate.java,v 1.7 2003/09/07 18:50:13 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.delegate.dummy.DummyDatacenterDelegate;
import org.astrogrid.datacenter.servicestatus.ServiceStatus;
import org.w3c.dom.Element;

/**
 * A convenience class for java clients of datacenters.  They can create and
 * use this class to manage all the connections/calls/etc without having to
 * mess around with all the SOAP messages directly.
 *
 * An instance of one of these corresponds to one connection to one database
 *
 * This is an abstract class that also provides the factory creation.
 * @See DummyDatacenterDelegate and @see StdDatacenterDelegate for implemenations
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public abstract class DatacenterDelegate
{
   private Vector statusListeners = new Vector();

   /** Creates a delegate given an endpoint (a url to the service)
    */
   public static DatacenterDelegate makeDelegate(String givenEndPoint) throws MalformedURLException, ServiceException
   {
      if (givenEndPoint == null)
      {
         return new DummyDatacenterDelegate();
      }
      else
      {
         return new StdDatacenterDelegate(givenEndPoint);
      }
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public abstract void setTimeout(int givenTimeout);

   /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    */
   public abstract Element adqlQueryDatacenter(Element adql) throws RemoteException;

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public abstract int adqlCountDatacenter(Element adql);

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public abstract Element getRegistryMetadata();

   /**
    * Polls the service and asks for the current status
    */
   public abstract ServiceStatus getStatus();

   /**
    * Register a status listener.  This will be informed of changes in status
    * to the service - IF the service supports such info.  Otherwise it will
    * just get 'starting', 'working' and 'completed' messages based around the
    * basic http exchange.
    */
   public void registerStatusListener(DatacenterStatusListener aListener)
   {
      statusListeners.add(aListener);
   }

   /** informs all listeners of the new status change. Not threadsafe...
    */
   protected void fireStatusChanged(ServiceStatus newStatus)
   {
      for (int i=0;i<statusListeners.size();i++)
      {
         ((DatacenterStatusListener) statusListeners.get(i)).datacenterStatusChanged(newStatus);
      }
   }
}

/*
$Log: DatacenterDelegate.java,v $
Revision 1.7  2003/09/07 18:50:13  mch
Added typesafe ServiceStatus

Revision 1.6  2003/08/31 15:22:07  mch
Removed unused QueryException

Revision 1.5  2003/08/27 23:55:02  mch
test bug fixes

Revision 1.4  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/


