/*
 * $Id: StatusHelper.java,v 1.1 2003/09/10 17:57:31 mch Exp $
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
   public static String makeStatusTag(String serviceId, String status)
   {
       return
          "<"+STATUS_TAG+"  "+ServiceIdHelper.SERVICE_ID_ATT+"='"+serviceId+">\n"
         +status
         +"</"+STATUS_TAG+">\n";

   }

   /**
    * Returns an Iteration 02 job notification tag with status included
    */
   public static String makeJobNotificationTag(String serviceId, String status) throws SAXException
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
   public static String getServiceStatus(String serviceId, Element domContainingStatuses)
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
               return ((Element) idNodes.item(i)).getNodeValue();
            }
         }
         
      }
      
      return null; //no status found for that service id

   }
}

