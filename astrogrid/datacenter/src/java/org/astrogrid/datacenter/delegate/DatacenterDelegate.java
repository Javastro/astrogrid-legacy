/*
 * $Id: DatacenterDelegate.java,v 1.14 2003/09/15 16:11:44 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.common.ServiceIdHelper;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
   /** list of DatacenterStatusListener instances that have registered to listen
    * to status changes */
   private Vector statusListeners = new Vector();

   /** Creates a delegate given an endpoint (a url to the service). If the endPoint
    * is null, creates a dummy delegate that can be used to test against.
    */
   public static DatacenterDelegate makeDelegate(String givenEndPoint)
         throws MalformedURLException, ServiceException, IOException
   {
      if (givenEndPoint == null)
      {
         return new DummyDelegate();
      }
      else
      {
         if (givenEndPoint.toLowerCase().startsWith("socket"))
         {
            return new SocketDelegate(givenEndPoint);
         }
         else if (givenEndPoint.toLowerCase().startsWith("http"))
         {
            return new WebDelegate(new URL(givenEndPoint));
         }
         /**
         else if (givenEndPoint.toLowerCase().startsWith("direct"))
         {
            return new DirectDelegate(new URL(givenEndPoint));
         }
         else if (givenEndPoint.toLowerCase().startsWith("grid"))
         {
            return new GridDelegate(new URL(givenEndPoint));
         }
          */
      }

      throw new IllegalArgumentException("Don't know what delegate to start for '"+givenEndPoint);
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public abstract void setTimeout(int givenTimeout);

   /**
    * General purpose blocking query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may include VOTable or otherwise
    * depending on the results format specified in the ADQL
    */
   public abstract Element adqlQuery(Element adql) throws IOException;

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public abstract int adqlCountDatacenter(Element adql) throws IOException;

   /**
    * General purpose asynchronous query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * response document including the service id that corresponds to that query
    */
   public abstract Element spawnAdqlQuery(Element adql) throws IOException;

   /**
    * Examines the given response DOM element (returned by spawnAdqlQuery) and
    * returns the service ID
    */
   public String getIdFromResponse(Element response)
   {
      NodeList ids = response.getElementsByTagName(ServiceIdHelper.SERVICE_ID_TAG);

      Log.affirm(ids.getLength() == 1, "More than one service ID in response...");

      return ids.item(0).getNodeValue();
   }

   /**
    * Polls the status of the service, returning the results when they're
    * ready
    */
   public abstract Element getResults(String id) throws IOException;

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public abstract Element getRegistryMetadata() throws IOException;

   /**
    * returns the full metadata document (an XML document describing the data the
    * center serves)
    *
   public abstract Element getRMetadata() throws IOException;
    /**/

   /**
    * Polls the service and asks for the current status
    */
   public abstract ServiceStatus getServiceStatus(String id);

   /**
    * Register a status listener.  This will be informed of changes in status
    * to the service - IF the service supports such info.  Otherwise it will
    * just get client 'starting', 'working' and 'completed' messages based around the
    * basic http exchange.
    */
   public void registerStatusListener(DatacenterStatusListener aListener)
   {
      statusListeners.add(aListener);
   }

   /** informs all listeners of the new status change. Not threadsafe...
    */
   protected void fireStatusChanged(String id, String newStatus)
   {
      for (int i=0;i<statusListeners.size();i++)
      {
         ((DatacenterStatusListener) statusListeners.get(i)).datacenterStatusChanged(id, newStatus);
      }
   }

   /**
    * Register web listener with service
    */
   public abstract void registerWebListener(URL listenerUrl);
}

/*
$Log: DatacenterDelegate.java,v $
Revision 1.14  2003/09/15 16:11:44  mch
Fixes to handle updates when multiple queries are running through one delegate

Revision 1.13  2003/09/15 16:01:21  mch
Fixes to make maven happ(ier)

Revision 1.12  2003/09/15 15:23:59  mch
Added doc and framework for future grid/direct delegates

Revision 1.11  2003/09/15 12:17:46  mch
Renamed HtmlDelegate to WebDelegate

Revision 1.10  2003/09/11 16:10:50  mch
Correction for 'socket' protocol

Revision 1.9  2003/09/10 17:57:31  mch
Tidied xml doc helpers and fixed (?) job/web listeners

Revision 1.8  2003/09/09 17:50:07  mch
Class renames, configuration key fixes, registry/metadata methods and spawning query methods

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


