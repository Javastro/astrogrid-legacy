/*
 * $Id: AxisDataServer.java,v 1.3 2003/08/28 13:24:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;

/**
 * This class is the public web interface, called by Axis
 * when Axis receives the SOAP message from the client.
 *<p>
 * I'm not sure whether Axis creates a new instance of one of these every time
 * it receives the message, (I don't think it does) so the class itself is
 * stateless.  Instead, all state depends on the DataService instances, one of
 * which is created for each data query call.
 * <p>
 * This is what was DatasetAgent in the It02 but with the extensions to handle
 * all the service methods required (eg metadata etc)
 * @author M Hill
 */

public class AxisDataServer
{
   /** List of service instances, keyed by their handle, so we can access them
    * for non-blocking calls
    */
   private Hashtable services = new Hashtable();

   /**
    * Empty constructor
    */
   public AxisDataServer()
   {
   }

   /**
    * Returns the metadata in the registry form (VOResource)
    * @todo implement
    */
   public Element getVOResource()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the while metadata file
    * @todo implement
    */
   public Element getMetadata()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Returns the elements of the metadata corresponding to the given XPath
    * @todo implement
    */
   public Element getMetadata(String xpath)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Runs a blocking query - ie, starts the query, waits for it to finish
    * and then returns the results.
    * @todo: check how Axis handles
    * threading... will each call block all other calls until this one is
    * complete? if so we need to make DataQueryService a thread and work out
    * how to return the results...
    *
    */
   public Element runQuery(Element soapBody) throws QueryException, DatabaseAccessException, IOException
   {
      DataQueryService service = new DataQueryService();

      //services.put(service.getHandle(), service); no need to do this on a blocking query - and raises threadsafety issues

      return service.runQuery(soapBody);

   }

   /** For non-blocking queries, you might want to get the query status
    */
   public String getServiceStatus(String serviceID)
   {
      return getService(serviceID).getStatus();

   }

   /**
    * Returns the service corresponding to the given ID
    */
   public DataQueryService getService(String serviceID)
   {
      return (DataQueryService) services.get(serviceID);
   }

   /**
    * Registers a client as a listener - ie it will receive notifications
    */
   public void registerServiceListener(String serviceID, URL listenerUrl)
   {
      getService(serviceID).registerServiceListener(new ServiceListener(listenerUrl));
   }

}

