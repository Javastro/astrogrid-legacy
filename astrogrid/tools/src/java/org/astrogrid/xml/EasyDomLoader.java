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

import java.io.IOException;

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
   public static Element loadElement(String filename) throws IOException
   {
      try
      {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         //dbf.setValidating(true);
         dbf.setIgnoringElementContentWhitespace(true);
         dbf.setNamespaceAware(true);
         
         DocumentBuilder builder = dbf.newDocumentBuilder();
         Document doc = builder.parse(new java.io.File(filename));
         
         //look through root nodes until we find the first element (ones before
         //may be comments)
         int i =0;
         while ((i<doc.getChildNodes().getLength()-1)
                && !(doc.getChildNodes().item(i) instanceof Element))
         {
            i++;
         }
         if (i>doc.getChildNodes().getLength()-1)
         {
            throw new IOException("No root element found");
         }
         
         Element rootNode = (Element) doc.getChildNodes().item(i);
         
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

