/*
 * $Id: QueryIdHelper.java,v 1.1 2003/09/15 22:05:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.common;

import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A helper class for constructing and extracting service id tags and
 * attributes
 *
 * @author M Hill
 */

public class QueryIdHelper
{
   /** The service id name when used as a tag tag name */
   public static final String QUERY_ID_TAG = "ServiceId";

   /** The service id name when using as an attribute */
   public static final String QUERY_ID_ATT = "serviceid";

   /** Makes a service tag with the given id as the tag value */
   public static String makeQueryIdTag(String id)
   {
      String tagString = "<"+QUERY_ID_TAG+">"+id+"</"+QUERY_ID_TAG+">";

      return tagString;
      //return XMLUtils.newDocument(tagString).getDocumentElement();
   }

   /** Finds the service tag in the given dom and returns its tag value */
   public static String getQueryId(Element domContainingId)
   {
      NodeList idNodes = domContainingId.getElementsByTagName(QUERY_ID_TAG);

      if (idNodes.getLength() == 0)
      {
         //no service id tag found, look for attribute in top tag
         return domContainingId.getAttribute(QueryIdHelper.QUERY_ID_ATT);
      }

      Log.affirm(idNodes.getLength() == 1, "Should only be 1 service id tag in an element");

      String nodeValue = idNodes.item(0).getNodeValue();

      if (nodeValue == null) //look at first non-whitespace child
      {
         nodeValue = idNodes.item(0).getFirstChild().getNodeValue();
      }

      return nodeValue;
   }

   /** Makes a tag with the given name, and a service id attribute set to the
    * given id
    */
   public static String makeTagWithQueryIdAttr(String tagName, String id)
   {
      return "<"+tagName+"  "+QUERY_ID_ATT+"='"+id+"'>";
   }
}


