/*
 * $Id: QuerierQuerying.java,v 1.2 2004/03/15 19:16:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

public class QuerierQuerying extends QuerierStatus
{
   public QuerierQuerying(Querier querier) {
      super(querier);
   }
   
   public QueryState getState() { return QueryState.RUNNING_QUERY; }

}

/*
$Log: QuerierQuerying.java,v $
Revision 1.2  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
