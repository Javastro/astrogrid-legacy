/*
 * $Id: DsaDomHelper.java,v 1.6 2004/11/11 20:42:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter;

import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Dom Helper methods that have yet to get moved to common
 * <p>
 * @author M Hill
 */

public class DsaDomHelper
{

   /** Sets the value of the element ot the given string *and removes all
    * element children* - see
    * getValue() of DomHelper
    */
   public static void setElementValue(Element element, String value) {
      //find the child text element(s)?
      NodeList children = element.getChildNodes();
      for (int c = 0; c < children.getLength(); c++) {
         if (children.item(c).getNodeType() == Node.TEXT_NODE ) {
            children.item(c).setNodeValue(value);
            return;
         }
      }
      //there isn't one, so add one
      element.appendChild(element.getOwnerDocument().createTextNode(value));
      
   }

   /** Returns the first child element of the given name of the given parent
    * element.  If there is more than one, throws an exception.  If there is
    * none, returns null
    @todo move to DomHelper */
   public static Element getSingleChildByTagName(Element parent, String tagName) {
      Element[] children = getChildrenByTagName(parent, tagName);
      if (children == null) {
         return null;
      }
      else if (children.length>1) {
         //more than one from is bad
         throw new IllegalArgumentException("More than one "+tagName+" element in "+parent.getNodeName());
      }
      else if (children.length>0) {
         return children[0];
      }
      else {
         return null;
      }
   }
   
   /** a bit like getChildrenByTagName but returns only those elements that are
    * direct children of the given parent
    @todo move to DomHelper
    */
   public static Element[] getChildrenByTagName(Element parent, String name) {
      if (parent == null) {
         return null;
      }
      Vector v =  new Vector();
      NodeList c = parent.getChildNodes();
      for (int n = 0; n < c.getLength(); n++) {
         if (c.item(n) instanceof Element) {
            //ignore namespaces for now
            if ( ((Element) c.item(n)).getLocalName().equals(name)) {
               v.add( c.item(n));
            }
         }
      }
      return (Element[]) v.toArray(new Element[] {});
   }
   
   /** Returns the child with the given name - creating it if it doesn't exist */
   public static Element ensuredGetSingleChild(Element parent, String childName)  {
      Element tag = DsaDomHelper.getSingleChildByTagName(parent, childName);
      if (tag == null) {
         tag = parent.getOwnerDocument().createElementNS(parent.getNamespaceURI(), childName);
         parent.appendChild(tag);
      }
      return tag;
   }
   
   
}


