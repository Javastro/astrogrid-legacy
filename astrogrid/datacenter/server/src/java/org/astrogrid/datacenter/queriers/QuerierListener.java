/*
 * $Id: QuerierListener.java,v 1.4 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers;



/**
 * An interface to listen to status changes
 * on a running querier
 *
 * @author M Hill
 * @see Querier
 */

public interface QuerierListener
{
   /** Called by the service when it has a
    * status change
    */
   public void queryStatusChanged(Querier querier);
}

/*
$Log: QuerierListener.java,v $
Revision 1.4  2004/03/12 04:45:26  mch
It05 MCH Refactor

Revision 1.3  2004/01/14 17:57:32  nw
improved documentation

Revision 1.2  2003/11/25 14:17:24  mch
Extracting Querier from DatabaseQuerier to handle non-database backends

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure


*/

