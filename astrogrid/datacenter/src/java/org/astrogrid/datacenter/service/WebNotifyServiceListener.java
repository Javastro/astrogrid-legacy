/*
 * $Id: WebNotifyServiceListener.java,v 1.1 2003/09/07 18:46:42 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.net.URL;
import org.astrogrid.datacenter.servicestatus.ServiceStatus;


/**
 * This is an implementation of a listener design for remote listeners (ie service
 * clients). So this must be capable of passing that information back to the remote
 * client.
 *
 * @todo this is just a placeholder at the moment, need to work out how
 * the notification thing will work, also non-blocking queries etc....
 *
 * @todo probably need a proper typesafe enumeration of the states.
 *
 * @author M Hill
 */

public class WebNotifyServiceListener implements ServiceListener
{
   private URL clientListener = null;

   /**
    * Create a listener which will send service updates to the given URL
    *
    * @todo design &amp; implement properly...
    */
   public WebNotifyServiceListener(URL aClientListener)
   {
      this.clientListener = aClientListener;
   }

   /** Called by the service when it has a
    * status change
    */
   public void serviceStatusChanged(ServiceStatus newStatus)
   {
      //try and connect to client

      //send document to client

   }
}

/*
$Log: WebNotifyServiceListener.java,v $
Revision 1.1  2003/09/07 18:46:42  mch
Added stateful (threaded) queries and typesafe service status

Revision 1.1  2003/09/05 13:22:12  nw
class formerly known as ServiceListener

Revision 1.2  2003/08/29 07:57:12  maven
- changed '&' to '&amp;'

Revision 1.1  2003/08/28 13:07:41  mch
Added service listener placeholders



*/

