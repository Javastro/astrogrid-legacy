/*
 DomDumper.java

 Date       Author      Changes
 $date$     M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.tools.xml;

import java.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.apache.axis.utils.XMLUtils;

/**
 * Used to dump a DOM to file.  Mostly for debugging, etc.
 *
 * @version %I%
 * @author M Hill
 */

public class DomDumper
{
   /**
    * Axis-based version
    */
   public static void dumpNode(Node aNode, OutputStream out) throws IOException
   {
      out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
      if (aNode.getNodeType() == Node.DOCUMENT_NODE)
      {
         XMLUtils.DocumentToStream((Document)aNode, out);
      }
      else if (aNode.getNodeType() == Node.ELEMENT_NODE)
      {
         XMLUtils.ElementToStream((Element)aNode, out);
      }
      else
      {
         throw new IOException(
            "Error - expected Document or Element node to dump.");
      }
   }
    /**/

   /*
   // ORIGINAL VERSION (REQUIRES JAVA 1.4)
   public static void dumpNode(Node aNode, OutputStream out) throws IOException
   {
      try
      {
         // Use a Transformer for output
         TransformerFactory tFactory = TransformerFactory.newInstance();
         Transformer transformer = tFactory.newTransformer();
         
         DOMSource source = new DOMSource(aNode);
         StreamResult result = new StreamResult(out);
         transformer.transform(source, result);      //xmlOut.writeLine(anElement.getNodeName());
      }
      catch (Exception e)
      {
         throw new IOException(""+e);
      }
   }
   /**/
}

