/*
 * $Id: StatusHelper.java,v 1.5 2003/09/15 15:19:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.astrogrid.log.Log;

/**
 * A helper class for constructing and relaying the documents that are common
 * to both the client and server.
 *
 * @author M Hill
 */

public class StatusHelper
{
   public final static String STATUS_TAG="Status";

   /**
    * Returns a status tag for the given service id with the value of the given
    * status
    */
   public static String makeStatusTag(String serviceId, ServiceStatus status)
   {
       return
          "<"+STATUS_TAG+"  "+ServiceIdHelper.SERVICE_ID_ATT+"='"+serviceId+"'>"
         +status
         +"</"+STATUS_TAG+">\n";

   }

   /**
    * Returns an Iteration 02 job notification tag with status included
    */
   public static String makeJobNotificationTag(String serviceId, String status)
   {
      return
            "<job name='"+serviceId+"'  >"+
               "<jobstep name='"+serviceId+"' status='"+status+"'/>"+
            "</job>";
   }

   /**
    * Returns the status of the given service id, given by a status tag in
    * the given dom document
    */
   public static ServiceStatus getServiceStatus(String serviceId, Element domContainingStatuses)
   {
      NodeList idNodes = domContainingStatuses.getElementsByTagName(STATUS_TAG);

      //run through nodes looking for one where the serviceid attribute
      //corresponds to the given id
      for (int i=0;i<idNodes.getLength();i++)
      {
         String serviceIdAttr =
            ((Element) idNodes.item(i)).getAttribute(ServiceIdHelper.SERVICE_ID_ATT);

         if (serviceIdAttr.length() == 0)
         {
            //should now do something horrible, as we have a status with no
            //service id, but we don't really want to crash (after all it may
            //be some irrelevent part of the document), so lets just log
            //it
            Log.logError(STATUS_TAG+" in document has no "+ServiceIdHelper.SERVICE_ID_ATT+" attribute");
         }
         else
         {
            if (serviceIdAttr.equals(serviceId))
            {
               String status = ((Element) idNodes.item(i)).getNodeValue();
               if (status == null)
               {
                  status = ((Element) idNodes.item(i)).getFirstChild().getNodeValue();
               }
               return ServiceStatus.getFor(status.trim());
            }
         }

      }

      return null; //no status found for that service id

   }

   /**
    * Returns the status, given by a status tag in
    * the given dom document.
    */
   public static ServiceStatus getServiceStatus(Element domContainingStatuses)
   {
      NodeList idNodes = domContainingStatuses.getElementsByTagName(STATUS_TAG);

      if (idNodes.getLength() == 0)
      {
         return null;
      }

      Log.affirm(idNodes.getLength() ==1, "Should only be 1 status in document...");

      String status = ((Element) idNodes.item(0)).getNodeValue();
      if (status == null)
      {
          status = ((Element) idNodes.item(0)).getFirstChild().getNodeValue();
      }
      return ServiceStatus.getFor(status.trim());

   }

}

