/*
   $Id: DomHelper.java,v 1.1 2004/03/07 14:58:56 mch Exp $

   (c) Copyright...
*/

package org.astrogrid.util;


import java.io.*;

import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * An implementation-neutral class for loading Xml documents from files into
 * a DOM.
 * <P>
 * The problem with using Apache's XMLUtils or similar is that it requires a particular
 * DOM implementation installed.  This does not...
 *
 * @author M Hill
 */
public class DomHelper
{
   /**
    * Convenience class returns Document from given URL
    */
   public static Document newDocument(URL documentLocation) throws ParserConfigurationException, SAXException, IOException
   {
      return newDocument(new BufferedInputStream(documentLocation.openStream()));
   }
   
   /**
    * Convenience class returns Document from given <b>String of XML</b>.
    */
   public static Document newDocument(String xmlDocument) throws ParserConfigurationException, SAXException, IOException
   {
      return newDocument(new BufferedInputStream(new StringBufferInputStream(xmlDocument)));
   }

   /**
    * Convenience class returns Document from given file
    */
   public static Document newDocument(File documentFile) throws ParserConfigurationException, SAXException, IOException
   {
      return newDocument(new BufferedInputStream(new FileInputStream(documentFile)));
   }


   /**
    * Loads and returns the root DOM node (Element) from the given
    * XML file
    */
   public static Document newDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException
   {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         //dbf.setValidating(true);
         //dbf.setIgnoringElementContentWhitespace(true);  //not available in 1.3
         dbf.setNamespaceAware(true);
         
         DocumentBuilder builder = dbf.newDocumentBuilder();
         return builder.parse(in);
   }

   /**
    * Creates an empty document
   */
   public static Document newDocument() throws ParserConfigurationException {

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = dbf.newDocumentBuilder();
      return builder.newDocument();
      
   }
      
   /**
    * Writes document to stream
    */
   public static void DocumentToStream(Document doc, OutputStream out) {
      //naughtily implemented as an XMLUtil call until I work out how to do it independently
      XMLUtils.DocumentToStream(doc, out);
   }
   
   /**
    * Writes document to string
    */
   public static String DocumentToString(Document doc) {
      //naughtily implemented as an XMLUtil call until I work out how to do it independently
      return XMLUtils.DocumentToString(doc);
   }
   
   /**
    * Writes document to string
    */
   public static String ElementToString(Element body) {
      //naughtily implemented as an XMLUtil call until I work out how to do it independently
      return XMLUtils.ElementToString(body);
   }
   
   /**
    * Writes document to string in nice indented format, etc
    */
   public static void PrettyElementToStream(Element body, OutputStream out) {
      XMLUtils.PrettyElementToStream(body, out);
   }
   
   /**
    * Writes document to string in nice indented format, etc
    */
   public static void PrettyDocumentToStream(Document body, OutputStream out) {
      XMLUtils.PrettyDocumentToStream(body, out);
   }
}

