/*
 * $Id: QuerierQuerying.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.query.QueryState;

public class QuerierQuerying extends QuerierStatus
{
   public QueryState getState() { return QueryState.RUNNING_QUERY; }

   public String getProgress() {
      return "Query being applied";
   }
}

/*
$Log: QuerierQuerying.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
