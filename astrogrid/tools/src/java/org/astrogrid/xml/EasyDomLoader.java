/*
   EasyXmlLoader.java

   Date         Author      Changes
   17 Oct 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.tools.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.*;

import org.astrogrid.log.Log;
/**
 *  A class for easily loading Xml documents into a DOM.

 *  @author           : M Hill
 */
public class EasyDomLoader
{
   /**
    * Loads and returns the root DOM node (Element) from the given
    * XML file
    */
   public static Element loadElement(String filename)
   {
      try
      {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         //dbf.setValidating(true);
         dbf.setIgnoringElementContentWhitespace(true);
         dbf.setNamespaceAware(true);
         
         DocumentBuilder builder = dbf.newDocumentBuilder();
         Document doc = builder.parse(new java.io.File(filename));
         
         Element rootNode = (Element) doc.getChildNodes().item(0);
         
         if (rootNode == null)
         {
            Log.logError("No root node in XML document '"+filename+"'");
            return null;
         }
         
         return rootNode;
         
      }
      catch (Exception e)
      {
         Log.logError("Failed to load parameters from '"+filename+"'",e);
         return null;
      }
   }
   
}

