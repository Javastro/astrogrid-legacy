/*
 * $Id: DelegateQueryListener.java,v 1.1 2003/10/06 18:55:21 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import org.astrogrid.datacenter.common.QueryStatus;

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
Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.11  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.10  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.9  2003/09/15 16:11:44  mch
Fixes to handle updates when multiple queries are running through one delegate

Revision 1.8  2003/09/15 16:06:11  mch
Fixes to make maven happ(ier)

Revision 1.7  2003/09/09 17:50:07  mch
Class renames, configuration key fixes, registry/metadata methods and spawning query methods

Revision 1.6  2003/09/07 18:50:34  mch
Added typesafe ServiceStatus

Revision 1.5  2003/08/28 13:08:45  mch
Added service listener placeholders

Revision 1.4  2003/08/27 22:40:28  mch
todo javadocced

Revision 1.3  2003/08/27 17:34:29  mch
Comment etc changes after reading Maven report :-)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:48  mch
initial checkin


*/

