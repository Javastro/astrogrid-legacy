/*
 * $Id: QuerierConstructed.java,v 1.3 2004/03/15 19:16:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.util.Date;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

public class QuerierConstructed extends QuerierStatus
{
   public QuerierConstructed(Querier querier) {
      super(querier);
      addDetail("Querier started at "+new Date());
   }
   
   public QueryState getState() { return QueryState.CONSTRUCTED; }
}

/*
$Log: QuerierConstructed.java,v $
Revision 1.3  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.2  2004/03/15 17:11:31  mch
Better information

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
