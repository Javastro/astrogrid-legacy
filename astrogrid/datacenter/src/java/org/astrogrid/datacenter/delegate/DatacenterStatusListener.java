/*
 * $Id: DatacenterStatusListener.java,v 1.5 2003/08/28 13:08:45 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import org.astrogrid.datacenter.servicestatus.ServiceStatus;

/**
 * Classes that implement this interface, and register with the
 * DatacenterDelegate,
 * will be informed when the status of the job changes, rather than having to
 * poll the service.
 *
 * @author M Hill
 */

public interface DatacenterStatusListener extends ServiceStatus
{
   /** Called by the datacenter delegate when it has been notified of a
    * status change
    */
   public void datacenterStatusChanged(String newStatus);
}

/*
$Log: DatacenterStatusListener.java,v $
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

