/*
 * $Id: QuerierAborted.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryState;

public class QuerierAborted extends QuerierStatus implements QuerierClosed
{
   public QuerierAborted() {   }
   
   public QueryState getState() { return QueryState.ABORTED; }
   
}

/*
$Log: QuerierAborted.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
