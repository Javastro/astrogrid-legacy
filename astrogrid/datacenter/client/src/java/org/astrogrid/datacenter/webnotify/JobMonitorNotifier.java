/*
 * $Id WebNotifier.java $
 *
 */

package org.astrogrid.datacenter.webnotify;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.service.ServiceStatusHelper;
import org.astrogrid.datacenter.snippet.DocHelper;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;

/**
 *
 * @author M Hill
 */

public class JobMonitorNotifier
{
   private String endPoint = null;
   
   public JobMonitorNotifier(String givenEndPoint)
   {
      this.endPoint = givenEndPoint;
   }
   
   public void notifyServer()
   {
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

         call.setTargetEndpointAddress( endPoint ) ;
         call.setOperationName( "monitorJob" ) ;  // Set method to invoke
         call.addParameter("monitorJobXML", XMLType.XSD_STRING,ParameterMode.IN);
         call.setReturnType(XMLType.XSD_STRING);

         call.invokeOneWay( parms ) ;


      }
      catch (ServiceException e)
      {
         Log.logError("Could not connect to client "+endPoint+" to send status update", e);
      }

   }
   
}

/*
$Log: JobMonitorNotifier.java,v $
Revision 1.1  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

*/
