/*
 * $Id: JobNotifyServiceListener.java,v 1.12 2004/03/02 01:37:20 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.webnotify.WebNotifier;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobMonitor;
import org.astrogrid.jes.delegate.impl.JobMonitorDelegate;

/**
 * Very much like the WebNotifyServiceListener, this one creates a special
 * Iteration 02 document specifically made for notifying the job execution system
 * @deprecated
 *
 * @author M Hill
 */

public class JobNotifyServiceListener implements QuerierListener
{
   private URL jobMonitor = null;

   Log log = LogFactory.getLog(JobNotifyServiceListener.class);
   
   /**
    * Create a listener which will send service updates to the given URL
    */
   public JobNotifyServiceListener(URL aClientListener)
   {
      jobMonitor = aClientListener;
   }

   /** Called by the service when it has a
    * status change. Uses the JobMonitorNotifier to send the change.
    */
   public void queryStatusChanged(Querier querier)
   {
//      try {
 
         JobMonitor jmd = JesDelegateFactory.createJobMonitor(jobMonitor.toString());

         
         //create Job Info bean
         
         log.debug("Querier ["+querier.getExtRef()+"] telling "+jobMonitor+" of status '"+querier.getStatus());
//         jmd.monitorJob(querier.getExtRef(), status, message);
//      }
//      catch (JesDelegateException e) {
//         LogFactory.getLog(WebNotifier.class).error("Failed to contact job monitor at "+jobMonitor, e);
//      }
   }
   
}

/*
$Log: JobNotifyServiceListener.java,v $
Revision 1.12  2004/03/02 01:37:20  mch
Updates from changes to StoreClient and AGSLs

Revision 1.11  2004/02/15 23:17:05  mch
Naughty Big Lump of Changes cont: fixes for It04.1 myspace

Revision 1.10  2003/12/01 16:43:52  nw
dropped _QueryId, back to string

Revision 1.9  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.8  2003/11/25 14:17:24  mch
Extracting Querier from DatabaseQuerier to handle non-database backends

Revision 1.7  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.6  2003/11/17 21:56:29  mch
Moved notification stuff to client part 2

Revision 1.5  2003/11/17 21:41:30  mch
Moved notification stuff to client

Revision 1.4  2003/11/17 20:48:44  mch
Adding Adql-like access to Nvo cone searches

Revision 1.3  2003/11/17 15:41:48  mch
Package movements

Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.3  2003/10/06 18:56:58  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.6  2003/09/24 16:40:18  mch
Changed job notification to include querier so fuller details can be sent

Revision 1.5  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.4  2003/09/16 15:23:16  mch
Listener fixes and rationalisation

Revision 1.3  2003/09/16 12:54:05  mch
DocHelper.wrap now throws IllegalArgumentException (runtime error) rather than SAXException, as XML is all softwired

Revision 1.2  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.1  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.1  2003/09/10 17:57:31  mch
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

