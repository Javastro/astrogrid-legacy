/*
 * $Id: DelegateQueryListener.java,v 1.3 2003/12/15 14:30:14 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import org.astrogrid.datacenter.query.QueryStatus;

/**
 * Defines what a class must implement in order to listen to changes on the
 * <b>delegate</b> query.
 *
 * @author M Hill
 */

public interface DelegateQueryListener
{
   /** Called by the delegate query when it has been notified of a
    * status change.
    */
   public void delegateQueryChanged(DatacenterQuery query, QueryStatus newStatus);
}

/*
$Log: DelegateQueryListener.java,v $
Revision 1.3  2003/12/15 14:30:14  mch
Removed misleading cvs history from a class rename

Revision 1.2  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure


*/

