/*
 * $Id: ServiceListener.java,v 1.4 2003/09/07 18:46:42 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import org.astrogrid.datacenter.servicestatus.ServiceStatus;


/**
 * This is an interface for any classes that want to listen to status changes
 * on the query service
 *
 * @author M Hill
 */

public interface ServiceListener
{
   /** Called by the service when it has a
    * status change
    */
   public void serviceStatusChanged(ServiceStatus newStatus);
}

/*
$Log: ServiceListener.java,v $
Revision 1.4  2003/09/07 18:46:42  mch
Added stateful (threaded) queries and typesafe service status

Revision 1.2  2003/08/29 07:57:12  maven
- changed '&' to '&amp;'

Revision 1.1  2003/08/28 13:07:41  mch
Added service listener placeholders



*/

