package org.astrogrid.xml;


import java.io.*;

import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * An implementation-neutral class for loading Xml documents from files into
 * a DOM or vice versa; or rather, a facade around whatever does the actual work.
 * <P>
 * Early versions of this class used Apache's XMLUtils to serialize
 * DOMs to strings;  however, from original comments,
 *
 *   "The problem with using Apache's XMLUtils or similar is that it
 *    requires a particular DOM implementation installed that matches
 *    what it was compiled against, and changing implementation can
 *    result in a lot of required code change."
 *
 * I have eliminated the use of XMLUtils in favour of standard Java 1.4 xml
 * API classes; the price is that we no longer supply "pretty-printing" for
 * DOM objects (which was provided by XMLUtils).
 *
 * @author M Hill
 * @author K Andrews
 */
public class DomHelper
{
    /** Convenience routine for returning the value of an element of the given
    * name that is a *direct* child of the given parent Element.  Returns empty string
    * if child can't be found.
     */
    public static String getValueOf(Element parent, String child) {

      assert parent != null : "Attempted to get value of "+child+" of null parent element";

      NodeList nodes = parent.getChildNodes();

      if ((nodes==null) || (nodes.getLength()==0)) {
          return "";
      }

      for (int i = 0; i < nodes.getLength(); i++) {
         String localName = nodes.item(i).getLocalName();
         if (localName == null) {
            localName = "";
         }
         if ((nodes.item(i) instanceof Element) && (localName.equals(child))) {
            return getValueOf( (Element) nodes.item(i));
         }
      }
      return "";
    }

    /** Convenience routine for returning the value of an element.  Allows for values that are
     * child nodes (which seems to happen when the value is on a separate line from
     * the opening tag) .  Returns empty string if element is empty.
     */
    public static String getValueOf(Element element) {

      assert element != null : "Attempted to get value of null element";

       if (element.getNodeValue() != null) {
          return element.getNodeValue();
       }
       if (element.hasChildNodes()) {
          return element.getChildNodes().item(0).getNodeValue();
       }
       return "";
    }

   /**
    * Convenience method returns Document from given URL
    */
   public static Document newDocument(URL documentLocation) throws SAXException, IOException
   {
      return newDocument(new BufferedInputStream(documentLocation.openStream()));
   }

   /**
    * Convenience method parses Document from given <b>String of XML</b>.
    */
   public static Document newDocument(String xmlDocument) throws SAXException, IOException
   {
      return newDocument(new InputSource(new StringReader(xmlDocument)));
   }

   /**
    * Convenience method parses Document from given file
    */
   public static Document newDocument(File documentFile) throws SAXException, IOException
   {
      return newDocument(new BufferedInputStream(new FileInputStream(documentFile)));
   }

   /**
    * Convenience method returns Document from given stream
    */
   public static Document newDocument(InputStream in) throws SAXException, IOException
   {
      return newDocument(new InputSource(in));
   }


   /**
    * Parses and returns the Document at the given source
    */
   public static Document newDocument(InputSource in) throws SAXException, IOException
   {
      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         //dbf.setValidating(true);
         //dbf.setIgnoringElementContentWhitespace(true);  //not available in 1.3
         dbf.setNamespaceAware(true);

         DocumentBuilder builder = dbf.newDocumentBuilder();
         return builder.parse(in);
      }
      catch (ParserConfigurationException e) {
         //this is really a one-off environment configuration error, and rather
         //than having to check all the time for it in code, I think it is better
         //to throw it as a RuntimeException.
         throw new RuntimeException(e);
      }
   }

   /**
    * Creates an empty document
   */
   public static Document newDocument()  {

      try {
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = dbf.newDocumentBuilder();
         return builder.newDocument();
      }
      catch (ParserConfigurationException e) {
         //this is really a one-off environment configuration error, and rather
         //than having to check all the time for it in code, I think it is better
         //to throw it as a RuntimeException.
         throw new RuntimeException(e);
      }
   }

   /**
    * Writes document to stream
    */
   public static void DocumentToStream(Document doc, OutputStream out) throws IOException {
     try {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(out));
      }
      catch (TransformerException e) {
         throw new IOException(e.getMessage());
      }
   }

   /**
    * Writes document to writer
    */
   public static void DocumentToWriter(Document doc, Writer out) throws IOException {
     try {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(doc), new StreamResult(out));
     }
     catch (TransformerException e) {
        throw new IOException(e.getMessage());
     }
   }

   /**
    * Writes document to string
    */
   public static String DocumentToString(Document doc) throws IOException {
     try {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        StringWriter sw = new StringWriter();
        trans.transform(new DOMSource(doc), new StreamResult(sw));
        return sw.toString();
      }
      catch (TransformerException e) {
         throw new IOException(e.getMessage());
      }
   }

   /**
    * Writes document to string
    */
   public static void ElementToStream(Element body,OutputStream out) throws IOException {
     try {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
        trans.transform(new DOMSource(body), new StreamResult(out));
      }
      catch (TransformerException e) {
         throw new IOException(e.getMessage());
      }
   }


   /**
    * Writes document to string
    */
   public static String ElementToString(Element body) throws IOException {
       try {
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer trans = tf.newTransformer();
           trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
           trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
           StringWriter sw = new StringWriter();
           trans.transform(new DOMSource(body), new StreamResult(sw));
           return sw.toString();
       }
       catch (TransformerException e) {
         throw new IOException(e.getMessage());
       }
   }

   public static String getNodeAttrValue(Element elem,String tagName) {
      return getNodeAttrValue(elem, tagName, null);
   }


   public static String getNodeAttrValue(Element elem,String tagName, String namespacePrefix) {
      String val = elem.getAttribute(tagName);
      if(val == null || val.trim().length() <= 0) {
         val = elem.getAttribute("xmlns:" + tagName);
      }
      if( (val == null || val.trim().length() <= 0) && namespacePrefix != null) {
         val = elem.getAttributeNS(namespacePrefix,tagName);
      }//if
      return val;
   }


   public static String getNodeTextValue(Node nd,String tagName) throws IOException {
      NodeList nl = getNodeListTags(nd,tagName);
      if(nl.getLength() > 0 && nl.item(0).getFirstChild() != null) {
         return nl.item(0).getFirstChild().getNodeValue();
      }
      return null;
   }


   public static String getNodeTextValue(Node nd,String tagName, String namespacePrefix)  throws IOException {
      NodeList nl = getNodeListTags(nd,tagName,namespacePrefix);
      if(nl.getLength() > 0 && nl.item(0).getFirstChild() != null) {
         return nl.item(0).getFirstChild().getNodeValue();
      }
      return null;
   }

   /**
    * Finds a NodeList based on a tagname.
    * @param doc
    * @param tagName
    * @param prefix
    * @return
    */
   public static NodeList getNodeListTags(Node nd,String tagName) throws IOException
   {
      return getNodeListTags(nd,tagName,null);
   }


   /**
    * Finds a NodeList based on a tagname and/or it's prefix/namespace
    * @param doc
    * @param tagName
    * @param prefix
    * @return
    */
   public static NodeList getNodeListTags(Node nd,String tagName, String namespacePref) throws IOException
   {
      if(nd instanceof Document) {
         return getNodeListTags((Document)nd,tagName,namespacePref);
      }else if(nd instanceof Element) {
         return getNodeListTags((Element)nd,tagName,namespacePref);
      }else {
         throw new IOException("The Node is not an instance of Document or Element");
      }
   }

   private static NodeList getNodeListTags(Document doc, String tagName, String namespacePref) {
      NodeList nl = doc.getElementsByTagName(tagName);

      if(nl.getLength() == 0 && namespacePref != null) {
         nl = doc.getElementsByTagNameNS(namespacePref,tagName );
      }
      if(nl.getLength() == 0 && namespacePref != null) {
         nl = doc.getElementsByTagName(namespacePref + ":" + tagName );
      }
      return nl;
   }

   private static NodeList getNodeListTags(Element elem, String tagName, String namespacePref) {
      NodeList nl = elem.getElementsByTagName(tagName);

      if(nl.getLength() == 0 && namespacePref != null) {
         nl = elem.getElementsByTagNameNS(namespacePref,tagName );
      }
      if(nl.getLength() == 0 && namespacePref != null) {
         nl = elem.getElementsByTagName(namespacePref + ":" + tagName );
      }
      return nl;
   }



   /** Sets the value of the element; ie sets the NodeValue of any child that is
    * a TEXTNODE, or creates one
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
   */
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

   /** Convenience routine to get the direct child elements as an array
    */
   public static Element[] getChildren(Element parent) {
      if (parent == null) {
         return null;
      }
      Vector v =  new Vector();
      NodeList c = parent.getChildNodes();
      for (int n = 0; n < c.getLength(); n++) {
         if (c.item(n) instanceof Element) {
            //ignore namespaces for now
            v.add( c.item(n));
         }
      }
      return (Element[]) v.toArray(new Element[] {});
   }


   /** Returns the given element's child with the given name - creating it
    * if it doesn't exist */
   public static Element ensuredGetSingleChild(Element parent, String childName)  {
      Element tag = getSingleChildByTagName(parent, childName);
      if (tag == null) {
         tag = parent.getOwnerDocument().createElementNS(parent.getNamespaceURI(), childName);
         parent.appendChild(tag);
      }
      return tag;
   }



}


