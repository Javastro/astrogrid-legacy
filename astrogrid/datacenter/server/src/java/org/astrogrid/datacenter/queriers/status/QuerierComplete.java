/*
 * $Id: QuerierComplete.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

/** Indicates the final state where the search has been carried out on the database
 * the results processed and everything done and tidied up successfully */

public class QuerierComplete extends QuerierQueried implements QuerierClosed
{
   public QuerierComplete(Querier querier) {
      super(querier);
   }
   
   public QueryState getState() { return QueryState.FINISHED; }
   
}

/*
$Log: QuerierComplete.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
