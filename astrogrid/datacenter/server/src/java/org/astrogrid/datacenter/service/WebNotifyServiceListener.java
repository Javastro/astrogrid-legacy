/*
 * $Id: WebNotifyServiceListener.java,v 1.4 2003/11/21 17:37:56 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.webnotify.WebNotifier;

/**
 * This is an implementation of a listener design for remote listeners (ie service
 * clients). So this must be capable of passing that information back to the remote
 * client.
 *
 * @author M Hill
 * @modified Noel Winstanley - made into a bean, so is SOAP-transportable.
 * @modified MCH no longer transported via soap..., my only silly fault that it was in the first place
 */

public class WebNotifyServiceListener implements QuerierListener
{
   private WebNotifier notifier = null;
   
   /**
    * Create a listener which will send service updates to the given URL
    */
   public WebNotifyServiceListener(URL aClientListener)
   {
      notifier = new WebNotifier(aClientListener);
   }

   /** Called by the service when it has a
    * status change. Uses the JobMonitorNotifier to send the change.
    */
   public void queryStatusChanged(DatabaseQuerier querier)
   {
      try {
         notifier.tellServer(querier.getHandle(), querier.getStatus());
      }
      catch (ServiceException e) {
         LogFactory.getLog(WebNotifier.class).error("Failed to contact service using "+notifier, e);
      }
         
   }

   /**
 * @return
 *
public String getClientListener() {
    return clientListener.toString();
}

/**
 * @param url
 *
public void setClientListener(String url) {
    try {
        clientListener = new URL(url);
    } catch (MalformedURLException e) {
        throw new IllegalArgumentException("Malformed URL:" + e.getMessage());
    }
}
    /**/
}

/*
$Log: WebNotifyServiceListener.java,v $
Revision 1.4  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.3  2003/11/17 21:56:29  mch
Moved notification stuff to client part 2

Revision 1.2  2003/11/17 15:41:48  mch
Package movements

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.8  2003/10/06 18:56:58  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.7  2003/09/25 01:22:50  nw
altered getter/setter types from URL to String to make this soap-portable

Revision 1.6  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.5  2003/09/16 15:23:16  mch
Listener fixes and rationalisation

Revision 1.4  2003/09/16 13:24:27  nw
added default constructor and getter/setter pair - makes
it sufficiently beanlike to be transported via SOAP

Revision 1.3  2003/09/16 12:53:58  mch
DocHelper.wrap now throws IllegalArgumentException (runtime error) rather than SAXException, as XML is all softwired

Revision 1.2  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.1  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.6  2003/09/15 16:42:03  mch
Fixes to make maven happy(er)

Revision 1.5  2003/09/15 11:17:14  mch
StatusHelper.makeStatusTag now takes typesafe status

Revision 1.4  2003/09/10 17:57:31  mch
Tidied xml doc helpers and fixed (?) job/web listeners

Revision 1.3  2003/09/09 18:33:09  mch
Reintroduced web notification from old job impl code

Revision 1.2  2003/09/09 17:52:29  mch
ServiceStatus move and config key fix

Revision 1.1  2003/09/07 18:46:42  mch
Added stateful (threaded) queries and typesafe service status

Revision 1.1  2003/09/05 13:22:12  nw
class formerly known as ServiceListener

Revision 1.2  2003/08/29 07:57:12  maven
- changed '&' to '&amp;'

Revision 1.1  2003/08/28 13:07:41  mch
Added service listener placeholders



*/

