/*
 * $Id: StatusHelper.java,v 1.3 2003/11/18 10:35:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.snippet;

import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A helper class for constructing and relaying the documents that are common
 * to both the client and server.
 *
 * @author M Hill
 */

public class StatusHelper
{
   /** Name of tag that holds status information */
   public static final String STATUS_TAG="Status";

   /**
    * Returns a status tag for the given service id with the value of the given
    * status
    */
   public static String makeStatusTag(String queryId, QueryStatus status)
   {
       return
          "<"+STATUS_TAG+"  "+QueryIdHelper.QUERY_ID_ATT+"='"+queryId+"'>"
         +status
         +"</"+STATUS_TAG+">\n";

   }
   /**
    * Returns a status tag for the given service status with the value of the given
    * status
    */
   public static String makeStatusTag(QueryStatus status)
   {
       return
          "<"+STATUS_TAG+">"
         +status
         +"</"+STATUS_TAG+">\n";

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
    *
   public static String makeJobNotificationTag(DatabaseQuerier querier)
   {
      return
            "<job name='"+querier.getHandle()+"'  time="+new Date()+"' >"+
               "<jobstep name='"+querier.getHandle()+"' status='"+querier.getStatus()+"'/>"+
            "</job>";
   }

   /**
    * Returns the status of the given service id, given by a status tag in
    * the given dom document
    * NWW: fixed for case when status tag is the root element
    */
   public static QueryStatus getServiceStatus(String queryId, Element domContainingStatuses)
   {
     if (STATUS_TAG.equals(domContainingStatuses.getLocalName())) {
         String queryIdAttr = domContainingStatuses.getAttribute(QueryIdHelper.QUERY_ID_ATT);
         if (queryId.equals(queryIdAttr)) {
             String status = domContainingStatuses.getNodeValue();
             if (status == null) {
                 status = domContainingStatuses.getFirstChild().getNodeValue();
             }
             return QueryStatus.getFor( status.trim());
         }
     }

      NodeList idNodes = domContainingStatuses.getElementsByTagName(STATUS_TAG);
      //run through nodes looking for one where the queryId attribute
      //corresponds to the given id
      for (int i=0;i<idNodes.getLength();i++)
      {
         String queryIdAttr =
            ((Element) idNodes.item(i)).getAttribute(QueryIdHelper.QUERY_ID_ATT);

         if (queryIdAttr.length() == 0)
         {
            //should now do something horrible, as we have a status with no
            //service id, but we don't really want to crash (after all it may
            //be some irrelevent part of the document), so lets just log
            //it
            LogFactory.getLog(StatusHelper.class).error(STATUS_TAG+" in document has no "+QueryIdHelper.QUERY_ID_ATT+" attribute");
         }
         else
         {
            if (queryIdAttr.equals(queryId))
            {
               String status = ((Element) idNodes.item(i)).getNodeValue();
               if (status == null)
               {
                  status = ((Element) idNodes.item(i)).getFirstChild().getNodeValue();
               }
               return QueryStatus.getFor(status.trim());
            }
         }

      }

      return null; //no status found for that service id

   }

   /**
    * Returns the status, given by a status tag in
    * the given dom document.
    */
   public static QueryStatus getServiceStatus(Element domContainingStatuses)
   {
      NodeList idNodes = domContainingStatuses.getElementsByTagName(STATUS_TAG);

      if (idNodes.getLength() == 0)
      {
          if (STATUS_TAG.equals(domContainingStatuses.getLocalName())) {
              String status = domContainingStatuses.getNodeValue();
              if (status ==  null) {
                  status = domContainingStatuses.getFirstChild().getNodeValue();
              }
              return QueryStatus.getFor(status.trim());
          }
         return null;
      }

      assert idNodes.getLength() ==1 : "Should only be 1 status in document...";

      String status = ((Element) idNodes.item(0)).getNodeValue();
      if (status == null)
      {
          status = ((Element) idNodes.item(0)).getFirstChild().getNodeValue();
      }
      return QueryStatus.getFor(status.trim());

   }

}


