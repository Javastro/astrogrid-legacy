/*
 * $Id: AxisDataServer.java,v 1.2 2003/08/27 22:42:52 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;

import java.util.Hashtable;
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
    */
   public Element getVOResource()
   {
      return null;
   }

   /**
    * Returns the while metadata file
    */
   public Element getMetadata()
   {
      return null;
   }

   /**
    * Returns the elements of the metadata corresponding to the given XPath
    */
   public Element getMetadata(String xpath)
   {
      return null;
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
   public Element runQuery(Element soapBody)
   {
      DataQueryService service = new DataQueryService();

      //services.put(service.getHandle(), service); no need to do this on a blocking query - and raises threadsafety issues

      return service.runQuery(soapBody);

   }

   public DataQueryService getService(String serviceID)
   {
      return (DataQueryService) services.get(serviceID);
   }

}

