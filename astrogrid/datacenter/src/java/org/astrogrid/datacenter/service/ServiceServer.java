/*
 * $Id: ServiceServer.java,v 1.1 2003/09/07 18:42:19 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.net.URL;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * This abstract class provides the framework for managing the datacenter.  It
 * manages the running service instances.  Subclasses from this might implement
 * an axis/http server, or a socket-server, or a grid/ogsa server, etc.
 * @see org.astrogrid.datacenter.servers package for implementations
 * <p>
 * @author M Hill
 */

public abstract class ServiceServer
{
   /** List of service instances, keyed by their handle, so we can access them
    * for non-blocking calls
    *
   private Hashtable services = new Hashtable();
    */
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
    *
    */
   public Element runQuery(Element soapBody) throws QueryException, DatabaseAccessException, IOException
   {
      return DatabaseQuerier.doQueryGetVotable(soapBody);
   }


   /** For non-blocking queries, you might want to get the query status
    * if the querier has an error (status = errro) throws the exception
    * (dont liek this too general)
    */
   public Element getServiceStatus(String serviceID) throws Throwable
   {
      return ResultsHelper.makeStatusResponse(getService(serviceID));
   }

   /**
    * Returns the service corresponding to the given ID
    */
   public DatabaseQuerier getService(String serviceID)
   {
      return DatabaseQuerier.getQuerier(serviceID);
   }

   /**
    * Registers a client as a listener - ie it will receive notifications
    */
   public void registerServiceListener(String serviceID, URL listenerUrl)
   {
      getService(serviceID).registerServiceListener(new WebNotifyServiceListener(listenerUrl));
   }

}

