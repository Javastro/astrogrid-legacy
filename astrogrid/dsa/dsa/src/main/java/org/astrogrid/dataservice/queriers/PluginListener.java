/*
 * $Id: PluginListener.java,v 1.1.1.1 2009/05/13 13:20:25 gtr Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.dataservice.queriers;

import org.astrogrid.dataservice.queriers.status.QuerierStatus;

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
Revision 1.1.1.1  2009/05/13 13:20:25  gtr


Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate


*/

