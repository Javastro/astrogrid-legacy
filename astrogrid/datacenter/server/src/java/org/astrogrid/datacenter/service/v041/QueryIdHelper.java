/*
 * $Id: QueryIdHelper.java,v 1.1 2004/03/12 20:05:45 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.v041;

import org.astrogrid.log.Log;
import org.astrogrid.util.DomHelper;
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
   public static final String QUERY_ID_TAG = "QueryId";

   /** The service id name when using as an attribute */
   public static final String QUERY_ID_ATT = "queryid";

   /** Makes a service tag with the given id as the tag value */
   public static String makeQueryIdTag(String id)
   {
      Log.affirm((id != null) && (id.length() >0), "Id is empty");

      String tagString = "<"+QUERY_ID_TAG+">"+id+"</"+QUERY_ID_TAG+">";

      return tagString;
      //return DomHelper.newDocument(tagString).getDocumentElement();
   }

   /** Finds the service tag in the given dom and returns its tag value
    * NWW: fixed for case when dom element is the one required */
   public static String getQueryId(Element domContainingId)
   {

       
      NodeList idNodes = domContainingId.getElementsByTagName(QUERY_ID_TAG);

      if (idNodes.getLength() == 0)
      {
          if (QUERY_ID_TAG.equals(domContainingId.getLocalName())) {
              return domContainingId.getFirstChild().getNodeValue();
          }
         //no service id tag found, look for attribute in top tag
         String attribute = domContainingId.getAttribute(QueryIdHelper.QUERY_ID_ATT);

         Log.affirm((attribute != null) && (attribute.length() != 0),
                    "No query id tag or attribute found in "+DomHelper.ElementToString(domContainingId));

         return attribute;
      }

      Log.affirm(idNodes.getLength() == 1, "Should only be 1 service id tag in an element");

      String nodeValue = idNodes.item(0).getNodeValue();

      if (nodeValue == null) //look at first non-whitespace child
      {
         //bug trace
         if (idNodes.item(0).getFirstChild() == null)
         {
            Log.trace(DomHelper.ElementToString( (Element) idNodes.item(0)));
         }


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




