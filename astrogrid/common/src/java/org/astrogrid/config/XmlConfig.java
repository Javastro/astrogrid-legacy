/*
 * $Id: XmlConfig.java,v 1.1 2003/10/07 16:42:36 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.config;


import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.w3c.dom.xpath.*;

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
   /** Constant used to search JNDI for file */
   public static final String JNDI = "org.astrogrid.config.xmlurl";

   /** Constant used to search system environment for properties file */
   public static final String SYS_ENV = "AG_XMLCONFIG";

   /** Default filename (look in working directory) */
   public static final String DEFAULT_FILENAME = "AstroGridNode.xml";
   
   /** The XML properties */
   private Element xmlProperties = null;
   
   /** Constructor */
   public XmlConfig()
   {
   }
   
   /**
    * Autoload looks for the properties file in jndi, then the environment variables,
    * then the classpath, then the local (working) directory
    */
   public void autoLoad() {
      
      loadJndiUrl(JNDI);

      loadSysEnvUrl(SYS_ENV);
  
      try {
         
         loadFile(DEFAULT_FILENAME);
      }
      catch (IOException ioe) {
            log.error("Load failed from default file '"+DEFAULT_FILENAME,ioe);
      }
   }
   
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
    */
   public static Node[] getProperty(String xpath)
   {
      try {
         XPathEvaluator evaluator = (XPathEvaluator) metadataDom; //Document should implement XPathEvaluator
         XPathResult results = evaluator.evaluate(xpath, metadataDom,
               null, XPathResult.NODE_SET_TYPE, null);

         XPathSetSnapshot set = results.getSetSnapshot(false);
         
         Node[] nodes = new Node[set.getLength()];
         
         for (int i=0;i<nodes.length;i++) {
            nodes[i] = set.item(i);
         }
         return nodes;
      }
      catch (XPathException e) {
         throw new IllegalArgumentException(xpath + " is not a correct XPath expression",e);
      }
      catch (DOMException e) {
         throw new RuntimeException("Error in configuration DOM", e);
      }
   }


}

