/*
 * $Id: QuerierComplete.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.status;

import java.util.Vector;
import org.astrogrid.query.QueryState;

/** Indicates the final state where the search has been carried out on the database
 * the results processed and everything done and tidied up successfully */

public class QuerierComplete extends QuerierStatus implements QuerierClosed
{
   public QuerierComplete(QuerierStatus previousStatus) {
      super(previousStatus, COMPLETE);
   }
   
   public QueryState getState() { return QueryState.FINISHED; }
   
}

/*
$Log: QuerierComplete.java,v $
Revision 1.1  2009/05/13 13:20:26  gtr
*** empty log message ***

Revision 1.2  2007/03/21 18:59:41  kea
Preparatory work for v1.0 resources (not yet supported);  and also
cleaning up details of completed jobs to save memory.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2.30.2  2004/11/26 18:17:21  mch
More status persisting, browsing and aborting

Revision 1.2.30.1  2004/11/25 18:33:43  mch
more status (incl persisting) more tablewriting lots of fixes

Revision 1.2  2004/10/01 18:04:58  mch
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
