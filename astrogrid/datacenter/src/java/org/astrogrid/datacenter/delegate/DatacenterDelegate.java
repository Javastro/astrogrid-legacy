/*
 * $Id: DatacenterDelegate.java,v 1.3 2003/08/27 22:40:55 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import org.astrogrid.datacenter.delegate.datasetAgent.DatasetAgentServiceLocator;
import org.astrogrid.datacenter.delegate.datasetAgent.DatasetAgentSoapBindingStub;
import org.w3c.dom.Element;

/**
 * A convenience class for java clients of datacenters.  They can create and
 * use this class to manage all the connections/calls/etc without having to
 * mess around with all the SOAP messages directly.
 *
 * An instance of one of these corresponds to one connection to one database
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class DatacenterDelegate
{
   private Vector statusListeners = new Vector();

   private URL endpoint = null;
   DatasetAgentSoapBindingStub binding;

   /** Use the factory method makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   private DatacenterDelegate(String givenEndPoint) throws MalformedURLException, ServiceException
   {
      this.endpoint = new URL(givenEndPoint);

      binding = (DatasetAgentSoapBindingStub)
            new DatasetAgentServiceLocator().getDatasetAgent( endpoint );
   }

   /** Creates a delegate given an endpoint (a url to the service)
    */
   private static DatacenterDelegate makeDelegate(String givenEndPoint) throws MalformedURLException, ServiceException
   {
      return new DatacenterDelegate(givenEndPoint);
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout)
   {
      binding.setTimeout(givenTimeout);
   }

   /**
    * Convenience method for making a simple query taking key/value pairs and
    * returning all elements that match all of them.  Tests the cgi-like query.
    */
   public Element pairsQueryDatacenter(Hashtable keyValuePairs)
   {
      return null;
   }

   /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    */
   public Element adqlQueryDataceneter(Element adql) throws RemoteException
   {
      binding.runQuery(adql.toString());

      return null; //for now
   }

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int adqlCountDatacenter(Element adql)
   {
      return 0;
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public Element getRegistryMetadata()
   {
      return null;
   }

   /**
    * Polls the service and asks for the current status
    */
   public String getStatus()
   {
      return DatacenterStatusListener.UNKNOWN;
   }

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

}

/*
$Log: DatacenterDelegate.java,v $
Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/

