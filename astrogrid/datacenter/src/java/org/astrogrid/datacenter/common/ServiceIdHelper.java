/*
 * $Id: ServiceIdHelper.java,v 1.1 2003/09/10 17:57:31 mch Exp $
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
 * A helper class for constructing and extracting service id tags and
 * attributes
 *
 * @author M Hill
 */

public class ServiceIdHelper
{
   /** The service id name when used as a tag tag name */
   public static final String SERVICE_ID_TAG = "ServiceId";

   /** The service id name when using as an attribute */
   public static final String SERVICE_ID_ATT = "serviceid";
   
   /** Makes a service tag with the given id as the tag value */
   public static String makeServiceIdTag(String id)
   {
      String tagString = "<"+SERVICE_ID_TAG+">"+id+"</"+SERVICE_ID_TAG+">";

      return tagString;
      //return XMLUtils.newDocument(tagString).getDocumentElement();
   }

   /** Finds the service tag in the given dom and returns its tag value */
   public static String getServiceId(Element domContainingId)
   {
      NodeList idNodes = domContainingId.getElementsByTagName(SERVICE_ID_TAG);

      Log.affirm(idNodes.getLength() == 1, "Should only be 1 service id tag in an element");

      return idNodes.item(0).getNodeValue();
   }

   /** Makes a tag with the given name, and a service id attribute set to the
    * given id
    */
   public static String makeTagWithServiceIdAttr(String tagName, String id)
   {
      return "<"+tagName+"  "+SERVICE_ID_ATT+"='"+id+"'>";
   }
}

