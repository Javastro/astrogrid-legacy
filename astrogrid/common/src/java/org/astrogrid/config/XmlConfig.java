/*
 * $Id: XmlConfig.java,v 1.3 2003/10/07 22:41:10 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * An XML configuration file accessor
 * <p>
 * NB - XPath is not at all settled at the moment, so this may break quite
 * easily.  The org.w3c.dom.xpath package can be found here:
 * http://www.w3.org/TR/2003/CR-DOM-Level-3-XPath-20030331/java-binding.zip
 *
 * @author M Hill
 */

public class XmlConfig extends Config
{
   /** The XML properties */
   private Element xmlProperties = null;
   
   /** Constructor */
   public XmlConfig()
   {
   }

   /** Returns the default configuration filename  */
   protected String getDefaultFilename() { return "AstroGrid.cfg.xml"; }
   
   /** Returns the jndi key used to find the url of the configuration file */
   protected String getJndiKey()         { return "org.astrogrid.config.xmlurl"; }
   
   /** Returns the System Environment variable used to find the url of the configuration file */
   protected String getSysEnvKey()       { return "AG_XMLCONFIG"; }
   
   
   /**
    * Loads the properties at the given inputstream.
    */
   protected void loadStream(InputStream in) throws IOException
   {
      try
      {
         xmlProperties = XMLUtils.newDocument(in).getDocumentElement();
      }
      catch (SAXException e) {
         throw new IOException("Configuration not valid xml "+e);
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Application not setup properly",e);
      }
         
   }


   /**
    * Returns the list of nodes that match the given xpath. See class javadoc
    * about XPath comments
    * @todo get the right xpath stuff.  At the moment just retuns a list of
    * those nodes with elements that match the given 'xpath' name
    */
   public  Node[] getProperties(String xpath)
   {
      NodeList set = xmlProperties.getElementsByTagName(xpath);
      
      /*
      try {
         XPathEvaluator evaluator = (XPathEvaluator) xmlProperties; //Document should implement XPathEvaluator
         XPathResult results = evaluator.evaluate(xpath, xmlProperties,
               null, XPathResult.NODE_SET_TYPE, null);

         XPathSetSnapshot set = results.getSetSnapshot(false);
         
      }
      catch (XPathException e) {
         throw new IllegalArgumentException(xpath + " is not a correct XPath expression: "+e);
      }
      catch (DOMException e) {
         throw new RuntimeException("Error in configuration DOM", e);
      }
       */

      Node[] nodes = new Node[set.getLength()];
         
         for (int i=0;i<nodes.length;i++) {
            nodes[i] = set.item(i);
         }
         return nodes;
      
   }

   /**
    * Convenience routine for getting a single property, along with checks
    * that there is only the one property
    */
   public Node getProperty(String xpath)
   {
      Node[] properties = getProperties(xpath);
      
      if (properties.length == 0) {
         return null;
      }

      assert (properties.length == 1) : "More than one property in XmlConfig matches xpath "+xpath;
      
      return properties[0];
   }

   /**
    * Convenience routine for getting a single property that is a string, ignoring
    * whitespace & lines
    * @todo do proper check for whitespace elements
    */
   public String getPropertyValue(String xpath)
   {
      Node propertyNode = getProperty(xpath);

      if (propertyNode.getFirstChild() == null) {
         //no children - value of this node is the value we need
         return propertyNode.getNodeValue();
      
      }
      
      //children - usually happens when the value is written on a new line with
      //the tags above and below.  Whitespace is normally stripped so ignore it
      return propertyNode.getFirstChild().getNodeValue();
   }
    /**/
   
}

