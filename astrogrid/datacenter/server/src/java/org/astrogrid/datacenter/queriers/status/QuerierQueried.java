/*
 * $Id: QuerierQueried.java,v 1.3 2004/03/15 19:16:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.util.Date;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

/** Defines the state after a query has been run on the database but before,
 * for example, the results have been processed.  States after this one should
 * subclass this and update details such as where the results have gone to, etc */

public class QuerierQueried extends QuerierStatus
{
   
   public QuerierQueried(Querier querier) {
      super(querier);
      addDetail("Finished Query phase at "+new Date());
   }
   
   public QueryState getState() { return QueryState.QUERY_COMPLETE; }
   
}

/*
$Log: QuerierQueried.java,v $
Revision 1.3  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.2  2004/03/15 17:11:31  mch
Better information

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
