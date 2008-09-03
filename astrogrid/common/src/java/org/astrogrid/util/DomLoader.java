/*
   $Id: DomLoader.java,v 1.3 2008/09/03 11:41:32 gtr Exp $

   (c) Copyright...
*/

package org.astrogrid.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * An implementation-neutral class for loading Xml documents from files into
 * a DOM.
 * <P>
 * The problem with using Apache's XMLUtils or similar is that it requires a particular
 * DOM implementation installed.  This does not...
 *
 * @deprecated - use DomHelper which does more of the stuff that XMLUtils does
 *
 * @author M Hill
 */
public class DomLoader
{
   /**
    * Convenience class returns Document from given URL
    */
   public static Document readDocument(URL documentLocation) throws ParserConfigurationException, SAXException, IOException
   {
      return readDocument(new BufferedInputStream(documentLocation.openStream()));
   }
   
   /**
    * Convenience class returns Document from given <b>String of XML</b>.
    */
   public static Document readDocument(String xmlDocument) throws ParserConfigurationException, SAXException, IOException
   {
     // Read the input as a steam of bytes, not characters. readDocument(InputStream) is responsible
     // for collating bytes into characters.
      return readDocument(new BufferedInputStream(new ByteArrayInputStream(xmlDocument.getBytes())));
   }

   /**
    * Convenience class returns Document from given file
    */
   public static Document readDocument(File documentFile) throws ParserConfigurationException, SAXException, IOException
   {
      return readDocument(new BufferedInputStream(new FileInputStream(documentFile)));
   }


   /**
    * Loads and returns the root DOM node (Element) from the given XML file.
    * The given stream must be a byte stream; the current method undertakes to
    * convert bytes to characters, respecting the encoding written at the head
    * of the document.
    *
    * @param in Byte stream containing the text of the XML document.
    * @return The document as a DOM tree.
    */
   public static Document readDocument(InputStream in) throws ParserConfigurationException, SAXException, IOException
   {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
         //dbf.setValidating(true);
         //dbf.setIgnoringElementContentWhitespace(true);  //not available in 1.3
         dbf.setNamespaceAware(true);
         
         DocumentBuilder builder = dbf.newDocumentBuilder();
         return builder.parse(in);
   }

}

