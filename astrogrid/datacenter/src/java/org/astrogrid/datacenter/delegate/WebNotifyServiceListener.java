/*
 * $Id: WebNotifyServiceListener.java,v 1.3 2003/09/16 12:53:58 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.net.URL;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.common.StatusHelper;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This is an implementation of a listener design for remote listeners (ie service
 * clients). So this must be capable of passing that information back to the remote
 * client.
 *
 * @author M Hill
 */

public class WebNotifyServiceListener implements DatacenterStatusListener
{
   /** URL of client listener - this is a web service that will receive
    * a document containing the new status */
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
    * status change. Opens a connection to the URL and sends it a document, which
    * is defined in StatusHelper
    */
   public void datacenterStatusChanged(String id, QueryStatus newStatus)
   {
      Log.trace("WebNotifyServiceListener.serviceStatusChanged("+newStatus+")") ;

      try {
         Document statusDoc = DocHelper.wrap(StatusHelper.makeStatusTag(id, newStatus));

         Object[] parms = new Object[]
         {
            XMLUtils.DocumentToString(statusDoc)
         };

         Call call = (Call) new Service().createCall() ;

         call.setTargetEndpointAddress( clientListener ) ;
         call.setOperationName( "serviceStatusChanged" ) ;  // Set method to invoke (same as ServiceListener)
         call.addParameter("serviceStatusXML", XMLType.XSD_STRING,ParameterMode.IN);
         call.setReturnType(XMLType.XSD_STRING);

         call.invokeOneWay( parms ) ;
      }
      catch (ServiceException e)
      {
         Log.logError("Could not connect to client "+clientListener+" to send status update", e);
      }

      Log.trace("exit WebNotifyServiceListener.serviceStatusChanged("+newStatus+")") ;


   }
}

/*
$Log: WebNotifyServiceListener.java,v $
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

