/*
 * $Id WebNotifier.java $
 *
 */

package org.astrogrid.datacenter.webnotify;


/**
 *
 * @author M Hill
 */

import java.net.URL;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.encoding.XMLType;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.snippet.DocHelper;
import org.astrogrid.datacenter.snippet.StatusHelper;
import org.w3c.dom.Document;

public class WebNotifier
{
   private URL endPoint = null;
   
   public static String METHOD = "datacenterStatusChanged"; //method called on listener service
   
   public WebNotifier(URL givenEndPoint)
   {
      this.endPoint = givenEndPoint;
   }
   
   public void tellServer(String queryId, QueryStatus status) throws ServiceException
   {
      Document statusDoc = DocHelper.wrap(makeNotificationTag(queryId, status));
      
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
    * Returns a string XML element with the notification document
    */
   public static String makeNotificationTag(String queryId, QueryStatus status)
   {
      return StatusHelper.makeStatusTag(queryId, status);
   }
   
}

/*
 $Log: WebNotifier.java,v $
 Revision 1.2  2003/11/17 21:56:42  mch
 Moved notification stuff to client part 2

 Revision 1.1  2003/11/17 20:47:57  mch
 Adding Adql-like access to Nvo cone searches

 */
