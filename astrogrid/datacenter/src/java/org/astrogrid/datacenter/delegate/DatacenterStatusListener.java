/*
 * $Id: DatacenterStatusListener.java,v 1.9 2003/09/15 16:11:44 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

/**
 * Classes that implement this interface, and register with the
 * DatacenterDelegate,
 * will be informed when the status of the job changes, rather than having to
 * poll the service.
 *
 * @author M Hill
 */

public interface DatacenterStatusListener
{
   /** Called by the datacenter delegate when it has been notified of a
    * status change.  NB a delegate may have several services running at once.
    */
   public void datacenterStatusChanged(String serviceId, String newStatus);
}

/*
$Log: DatacenterStatusListener.java,v $
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

