/*
 * $Id: QuerierListener.java,v 1.2 2003/11/25 14:17:24 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers;



/**
 * This is an interface for any classes that want to listen to status changes
 * on the query service
 *
 * @author M Hill
 */

public interface QuerierListener
{
   /** Called by the service when it has a
    * status change
    */
   public void queryStatusChanged(Querier querier);
}

/*
$Log: QuerierListener.java,v $
Revision 1.2  2003/11/25 14:17:24  mch
Extracting Querier from DatabaseQuerier to handle non-database backends

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.1  2003/10/06 18:56:27  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.2  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.1  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.6  2003/09/10 17:57:31  mch
Tidied xml doc helpers and fixed (?) job/web listeners

Revision 1.5  2003/09/09 17:52:29  mch
ServiceStatus move and config key fix

Revision 1.4  2003/09/07 18:46:42  mch
Added stateful (threaded) queries and typesafe service status

Revision 1.2  2003/08/29 07:57:12  maven
- changed '&' to '&amp;'

Revision 1.1  2003/08/28 13:07:41  mch
Added service listener placeholders



*/

