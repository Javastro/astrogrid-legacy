/*
 * $Id: JobNotifyServiceListener.java,v 1.2 2003/11/17 12:16:33 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;
import org.astrogrid.datacenter.service.*;


import java.net.URL;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.StatusHelper;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;

/**
 * Very much like the WebNotifyServiceListener, this one creates a special
 * Iteration 02 document specifically made for notifying the job execution system
 * @deprecated
 *
 * @author M Hill
 */

public class JobNotifyServiceListener extends WebNotifyServiceListener
{

   /**
    * Create a listener which will send service updates to the given URL
    *
    * @todo design &amp; implement properly...
    */
   public JobNotifyServiceListener(URL aClientListener)
   {
      super(aClientListener);
   }

   /** Called by the service when it has a
    * status change. Opens a connection to the URL and sends it a document, which
    * is defined in ????
    */
   public void serviceStatusChanged(DatabaseQuerier querier)
   {
      Log.trace("JobNotifyServiceListener.serviceStatusChanged("+querier.getStatus()+")") ;

      try {
//         String
//            requestTemplate = Configuration.getProperty( MONITOR_REQUEST_TEMPLATE) ;

         //make list of method parameters
//       Object [] parms = new Object[]
//       {
//          MessageFormat.format(requestTemplate,
//                               new Object[] { newStatus.getText(), queryId }
//                               )
//       } ;
         Document statusDoc = DocHelper.wrap(ServiceStatusHelper.makeJobNotificationTag(querier));

         Object[] parms = new Object[]
         {
            XMLUtils.DocumentToString(statusDoc.getOwnerDocument())
         };

         Call call = (Call) new Service().createCall() ;

         call.setTargetEndpointAddress( clientListener ) ;
         call.setOperationName( "monitorJob" ) ;  // Set method to invoke
         call.addParameter("monitorJobXML", XMLType.XSD_STRING,ParameterMode.IN);
         call.setReturnType(XMLType.XSD_STRING);

         call.invokeOneWay( parms ) ;


      }
      catch (ServiceException e)
      {
         Log.logError("Could not connect to client "+clientListener+" to send status update", e);
      }

      Log.trace("exit JobNotifyServiceListener.serviceStatusChanged("+querier.getStatus()+")") ;


   }
}

/*
$Log: JobNotifyServiceListener.java,v $
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

