/*
 * $Id: QuerierConstructed.java,v 1.2 2004/10/01 18:04:59 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.util.Date;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.status.TaskStatus;

public class QuerierConstructed extends QuerierStatus
{
   String id = null;
   Date startTime = new Date();
   Account owner = null;
   
   public QuerierConstructed(Querier querier) {
      super();
      addDetail("Querier started at "+new Date());
      id = querier.getId();
      owner = querier.getUser();
   }

   public String getId() { return id; }
   
   public Account getOwner() { return owner; }
   
   public Date getStartTime() { return startTime; }
   
   public QueryState getState() { return QueryState.CONSTRUCTED; }
   
   /** Returns a standard TaskStatus stage string */
   public String getStage() {    return TaskStatus.INITIALISED; }
   
   /** Returns false */
   public boolean isFinished() { return false; }

}

/*
$Log: QuerierConstructed.java,v $
Revision 1.2  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.3  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.2  2004/03/15 17:11:31  mch
Better information

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
