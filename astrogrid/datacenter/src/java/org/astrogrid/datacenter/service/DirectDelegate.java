/*
 * $Id: DirectDelegate.java,v 1.1 2003/09/09 17:56:30 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.w3c.dom.Element;

/**
 * An implementation of the delegate that is created when the datacenter
 * is colocated, and so we can run the services directly on the database
 * rather than
 * having to use some tcp/ip connection.
 *
 * NB this is in the service package to remove any cross dependencies
 *
 * @see DatacenterDelegate
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class DirectDelegate extends DatacenterDelegate
{
   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public DirectDelegate() throws IOException
   {
   }

   /**
    * Does nothing
    */
   public void setTimeout(int givenTimeout) {} //does nothing


  /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    */
   public Element adqlQuery(Element adql) throws IOException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public Element getResults(String id) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public Element spawnAdqlQuery(Element adql) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }


   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int adqlCountDatacenter(Element adql)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public Element getRegistryMetadata() throws IOException
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Polls the service and asks for the current status
    */
   public ServiceStatus getServiceStatus(String id)
   {
      return ServiceStatus.UNKNOWN;
   }

   /**
    * Registers a web listener with this service.  It's a bit of a pain to
    * implement this properly using the dummy, so not doing it yet...
    */
   public void registerWebListener(URL listenerUrl)
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

}

/*
$Log: DirectDelegate.java,v $
Revision 1.1  2003/09/09 17:56:30  mch
New Delegate for direct (ie colocated) datacenters



*/

