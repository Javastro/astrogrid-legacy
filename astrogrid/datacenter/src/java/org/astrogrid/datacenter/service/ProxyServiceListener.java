/*
 * $Id: ProxyServiceListener.java,v 1.1 2003/09/05 13:22:12 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.net.URL;


/**
 * This is an implementation of a listener design, because any listeners will
 * be remote (ie made a listen/please notify request to the web service) and
 * so this must be capable of passing that information back to the remote
 * client.
 *
 * @todo this is just a placeholder at the moment, need to work out how
 * the notification thing will work, also non-blocking queries etc....
 *
 * @todo probably need a proper typesafe enumeration of the states.
 *
 * @author M Hill
 */

public class ProxyServiceListener implements ServiceListener
{
   private URL clientListener = null;

   /**
    * Create a listener which will send service updates to the given URL
    *
    * @todo design &amp; implement properly...
    */
   public ProxyServiceListener(URL aClientListener)
   {
      this.clientListener = aClientListener;
   }

   /** Called by the service when it has a
    * status change
    */
   public void serviceStatusChanged(String newStatus)
   {
   }
}

/*
$Log: ProxyServiceListener.java,v $
Revision 1.1  2003/09/05 13:22:12  nw
class formerly known as ServiceListener

Revision 1.2  2003/08/29 07:57:12  maven
- changed '&' to '&amp;'

Revision 1.1  2003/08/28 13:07:41  mch
Added service listener placeholders



*/

