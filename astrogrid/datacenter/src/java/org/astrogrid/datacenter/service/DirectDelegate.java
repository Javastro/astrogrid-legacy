/*
 * $Id: DirectDelegate.java,v 1.4 2003/09/15 22:38:42 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.io.IOException;
import java.rmi.RemoteException;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.delegate.DatacenterDelegate;
import org.astrogrid.datacenter.delegate.DatacenterStatusListener;
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
   public Element query(Element adql) throws IOException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public Element getResults(String id) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public Element makeQuery(Element adql) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public Element startQuery(String queryId) throws RemoteException
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
   public QueryStatus getQueryStatus(String queryId)
   {
      return QueryStatus.UNKNOWN;
   }

   /**
    * Registers a web listener with this service.  It's a bit of a pain to
    * implement this properly using the dummy, so not doing it yet...
    */
   public void registerListener(String queryId, DatacenterStatusListener listener)
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

}

/*
$Log: DirectDelegate.java,v $
Revision 1.4  2003/09/15 22:38:42  mch
Split spawnQuery into make and start, so we can add listeners in between

Revision 1.3  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.2  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.1  2003/09/09 17:56:30  mch
New Delegate for direct (ie colocated) datacenters



*/

