/*
 * $Id WebNotifier.java $
 *
 */

package org.astrogrid.datacenter.webnotify;

import java.net.URL;
import java.util.Date;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.snippet.DocHelper;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;

/**
 * Class to notify JES job monitor server
 *
 * @author M Hill
 */

public class JobMonitorNotifier
{
   private URL endPoint = null;
   
   public JobMonitorNotifier(URL givenEndPoint)
   {
      this.endPoint = givenEndPoint;
   }
   
   public void tellServer(String queryId, QueryStatus status) throws ServiceException
   {
//         String
//            requestTemplate = Configuration.getProperty( MONITOR_REQUEST_TEMPLATE) ;

         //make list of method parameters
//       Object [] parms = new Object[]
//       {
//          MessageFormat.format(requestTemplate,
//                               new Object[] { newStatus.getText(), queryId }
//                               )
//       } ;
         Document statusDoc = DocHelper.wrap(makeJobNotificationTag(queryId, status));

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

    /**
      * Returns an Iteration 02 job notification tag with status included.  The
      * It02 template was this:
      * <pre>
  <?xml version="1.0" encoding="UTF8"?>
  <!-- Template for making SOAP requests to the JobMonitor -->
  <job name="{0}"
       userid="{1}"
       community="{2}"
       jobURN="{3}"
       time="{4}" >
     <jobstep name="{5}" stepNumber="{6}" status="{7}"/>
  </job>
     </pre>
      */
     public static String makeJobNotificationTag(String queryId, QueryStatus status)
     {
        return
              "<job name='"+queryId+"'  time="+new Date()+"' >"+
                 "<jobstep name='"+queryId+"' status='"+status+"'/>"+
              "</job>";
     }
   
}

/*
$Log: JobMonitorNotifier.java,v $
Revision 1.3  2003/11/17 21:56:42  mch
Moved notification stuff to client part 2

Revision 1.2  2003/11/17 21:41:16  mch
Moved notification stuff to client

Revision 1.1  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

*/
