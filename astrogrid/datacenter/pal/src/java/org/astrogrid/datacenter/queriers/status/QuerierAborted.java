/*
 * $Id: QuerierAborted.java,v 1.2 2004/10/01 18:04:58 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.status.TaskStatus;

public class QuerierAborted extends QuerierStatus implements QuerierClosed {
   
   public QuerierAborted(QuerierStatus previousStatus) {
      super(previousStatus);
   }
   
   public QueryState getState() { return QueryState.ABORTED; }
   
   /** Returns a standard TaskStatus stage string */
   public String getStage() {    return TaskStatus.ABORTED; }
   
   /** Returns true as an aborted querier is finished */
   public boolean isFinished() { return true; }
}

/*
$Log: QuerierAborted.java,v $
Revision 1.2  2004/10/01 18:04:58  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
