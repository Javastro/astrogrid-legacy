/*
 * $Id: QueryStatusForwarder.java,v 1.1 2003/09/15 21:27:15 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.delegate.DatacenterStatusListener;

/**
 * Forwards query status's to Service Status listeners.
 *
 * @author M Hill
 */

public class QueryStatusForwarder implements QueryListener
{
   /** The listener that will get the forwarded status's */
   DatacenterStatusListener listener = null;

   /**
    * Will forward status changes to the given status listener
    */
   public QueryStatusForwarder(DatacenterStatusListener givenDatacenterListener)
   {
      this.listener = givenDatacenterListener;
   }

   /**
    * Called when the querier's status changes.
    * Forwards the status of the given querier to the listener
    */
   public void serviceStatusChanged(DatabaseQuerier querier)
   {
      listener.datacenterStatusChanged(querier.getHandle(), querier.getStatus());
   }


}

