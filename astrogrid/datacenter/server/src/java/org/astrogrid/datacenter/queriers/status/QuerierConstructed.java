/*
 * $Id: QuerierConstructed.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.query.QueryState;

public class QuerierConstructed extends QuerierStatus
{
   public QueryState getState() { return QueryState.CONSTRUCTED; }
}

/*
$Log: QuerierConstructed.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
