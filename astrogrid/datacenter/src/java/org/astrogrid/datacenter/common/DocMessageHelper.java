/*
 * $Id: DocMessageHelper.java,v 1.2 2003/09/09 18:31:18 mch Exp $
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

public class DocMessageHelper
{
   public static final String SERVICE_ID_TAG = "ServiceId";

   public static Element makeServiceIdTag(String id) throws IOException, ParserConfigurationException, SAXException
   {
      String tagString = "<"+SERVICE_ID_TAG+">"+id+"</"+SERVICE_ID_TAG+">";

      return XMLUtils.newDocument(tagString).getDocumentElement();

   }

   public static String getServiceId(Element domContainingId)
   {
      NodeList idNodes = domContainingId.getElementsByTagName(SERVICE_ID_TAG);

      Log.affirm(idNodes.getLength() == 1, "Should only be 1 service id tag in an element");

      return idNodes.item(0).getNodeValue();
   }

   public static final String COMMUNITY_TAG = "Community";


   /**
    * Status doc
    */
   public static Element makeStatusDoc(String serviceId, String status) throws SAXException
   {
      try
      {
         /*
         String doc =
          "<DatacenterStatus>\n"
         +"   "+makeServiceIdTag(serviceId)+"\n"
         +"   <STATUS>"+status+"</STATUS>\n"
         +"</StatusResponse>\n";
          */

         //It02 workflow-specific version
         String doc =
            "<?xml version='1.0' encoding='UTF8'?> "+
            "<job name='"+serviceId+"'  >"+
               "<jobstep name='"+serviceId+"' status='"+status+"'/>"+
            "</job>";

         return XMLUtils.newDocument(doc).getDocumentElement();
      }
      catch (java.io.IOException e)
      {
         throw new SAXException(e);
      }
      catch (javax.xml.parsers.ParserConfigurationException e)
      {
         throw new SAXException(e);
      }
   }
}

