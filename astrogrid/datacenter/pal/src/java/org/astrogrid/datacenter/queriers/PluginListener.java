/*
 * $Id: PluginListener.java,v 1.2 2004/10/18 13:11:30 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.queriers.status.QuerierStatus;

/**
 * An interface to listen to querier status changes
 *
 * @author M Hill
 */

public interface PluginListener
{
   /** Called when a new status is reached
    */
   public void pluginStatusChanged(QuerierStatus newStatus);

}

/*
$Log: PluginListener.java,v $
Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate


*/

