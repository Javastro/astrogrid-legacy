/*
 * $Id: WebNotifyServiceListener.java,v 1.3 2003/09/09 18:33:09 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.net.URL;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
   private String serviceId = null;

   /**
    * Create a listener which will send service updates to the given URL
    *
    * @todo design &amp; implement properly...
    */
   public WebNotifyServiceListener(String aServiceId, URL aClientListener)
   {
      this.clientListener = aClientListener;
      this.serviceId = aServiceId;
   }

   /** Called by the service when it has a
    * status change. Opens a connection to the URL and sends it a document, which
    * is defined in ????
    */
   public void serviceStatusChanged(ServiceStatus newStatus)
   {
      Log.trace("WebNotifyServiceListener.serviceStatusChanged("+newStatus+")") ;

      try {
//         String
//            requestTemplate = Configuration.getProperty( MONITOR_REQUEST_TEMPLATE) ;

         //make list of method parameters
//       Object [] parms = new Object[]
//       {
//          MessageFormat.format(requestTemplate,
//                               new Object[] { newStatus.getText(), serviceId }
//                               )
//       } ;
         Element statusDoc = DocMessageHelper.makeStatusDoc(serviceId, newStatus.getText());

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
      catch (SAXException e)
      {
         //really should NOT happen!
         throw new RuntimeException("Failed to create standard status document", e);
      }

      Log.trace("exit WebNotifyServiceListener.serviceStatusChanged("+newStatus+")") ;


   }
}

/*
$Log: WebNotifyServiceListener.java,v $
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

