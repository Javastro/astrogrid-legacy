/*
 * $Id: QuerierQueued.java,v 1.1 2009/05/13 13:20:27 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.status;

import org.astrogrid.query.QueryState;

public class QuerierQueued extends QuerierStatus
{
   public QuerierQueued(QuerierStatus previousStatus) {
      super(previousStatus, QUEUED);
   }
   
   public QueryState getState() { return QueryState.QUEUED; }

   /** Returns false */
   public boolean isFinished() { return false; }
}


/*
$Log: QuerierQueued.java,v $
Revision 1.1  2009/05/13 13:20:27  gtr
*** empty log message ***

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.1.28.1  2004/11/25 18:33:43  mch
more status (incl persisting) more tablewriting lots of fixes

Revision 1.1  2004/10/05 14:57:01  mch
Added queued

Revision 1.2  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
