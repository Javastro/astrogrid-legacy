package org.astrogrid.registry.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.io.InputStream;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.util.DomHelper;
/** @todo javadoc 
 * @todo fix exception handling
 * @todo factor commonality of methods into a private helper method*/
public class XSLHelper {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(XSLHelper.class);
   
   /**
    * The default name of our database.
    *
    */
   public static final String DEFAULT_DATABASE_XSL = "XSLDBProcess.xsl" ;

   /**
    * The default resource name for our JDO config file.
    *
    */
   public static final String DEFAULT_CASTOR_XML = "CastorXSLProcess.xsl" ;
   
   
   
   public XSLHelper() {
      
   }
   
   private InputStream loadStyleSheet(String name) {
       ClassLoader loader = this.getClass().getClassLoader();
       return loader.getResourceAsStream(name);
   }
      
   private InputStream loadDBXSL() {
      ClassLoader loader = this.getClass().getClassLoader();
      return loader.getResourceAsStream(DEFAULT_DATABASE_XSL);
   }

   private InputStream loadCastorXSL() {
      ClassLoader loader = this.getClass().getClassLoader();
      return loader.getResourceAsStream(DEFAULT_CASTOR_XML);
   }
   

   public String transformADQLToXQL(Node doc, String versionNumber) {
      
      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;
      
      ClassLoader loader = this.getClass().getClassLoader();
      InputStream is = null;
      is = loader.getResourceAsStream("ADQLToXQL-" + versionNumber + ".xsl");
    logger
            .info("transformADQLToXQL(Node, String) - the file resource = ADQLToXQL-"
                    + versionNumber + ".xsl");
      Source xslSource = new StreamSource(is);
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      
      try {
         builderFactory.setNamespaceAware(true);
         DocumentBuilder builder = builderFactory.newDocumentBuilder();
         resultDoc = builder.newDocument();
         
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         //DOMResult result = new DOMResult(resultDoc);
         StringWriter sw = new StringWriter();
         StreamResult result = new StreamResult(sw);
         
         Transformer transformer = transformerFactory.newTransformer(xslSource);         
         transformer.transform(xmlSource,result);
         String xqlResult = sw.toString();
         if (xqlResult.startsWith("<?")) {
            xqlResult = xqlResult.substring(xqlResult.indexOf("?>")+2);
         }
         
         
         xqlResult = xqlResult.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
         return xqlResult;
         //System.out.println("the resultwriter transform = " + sw.toString());
         //System.out.println("The result of adql to xql = " + DomHelper.DocumentToString(resultDoc));
      }catch(ParserConfigurationException pce) {
        logger.error("transformADQLToXQL(Node, String)", pce);
      }catch(TransformerConfigurationException tce) {
        logger.error("transformADQLToXQL(Node, String)", tce);
      }catch(TransformerException te) {
        logger.error("transformADQLToXQL(Node, String)", te);
      }
      //@todo never return null on error
      return null;
   }
   
   public Document transformToOAI(Node doc, String versionNumber) {
      String fileName = "Resourcev" + versionNumber + "-OAI.xsl";

      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;
      
      ClassLoader loader = this.getClass().getClassLoader();
      InputStream is = null;
      is = loader.getResourceAsStream(fileName);
      Source xslSource = new StreamSource(is);
            
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      try {
         builderFactory.setNamespaceAware(true);
         DocumentBuilder builder = builderFactory.newDocumentBuilder();
         resultDoc = builder.newDocument();
         //DocumentFragment df = resultDoc.createDocumentFragment();
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         
         DOMResult result = new DOMResult(resultDoc);
         Transformer transformer = transformerFactory.newTransformer(xslSource);
         
         transformer.transform(xmlSource,result);
         //System.out.println("the resultwriter transform = " + sw.toString());
      }catch(ParserConfigurationException pce) {
        logger.error("transformToOAI(Node, String)", pce);
      }catch(TransformerConfigurationException tce) {
        logger.error("transformToOAI(Node, String)", tce);
      }catch(TransformerException te) {
        logger.error("transformToOAI(Node, String)", te);
      }
      //@todo never return null on error.
      return resultDoc;
   }
   
   public Document transformExistResult(Node doc, String versionNumber, String responseElement) {
      
      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;
      
      ClassLoader loader = this.getClass().getClassLoader();
      InputStream is = null;

      is = loader.getResourceAsStream("ExistRegistryResult" + versionNumber + ".xsl");
      
      Source xslSource = new StreamSource(is);
      
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      
      try {
         builderFactory.setNamespaceAware(true);
         DocumentBuilder builder = builderFactory.newDocumentBuilder();
         resultDoc = builder.newDocument();
         //DocumentFragment df = resultDoc.createDocumentFragment();
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         
         DOMResult result = new DOMResult(resultDoc);
         Transformer transformer = transformerFactory.newTransformer(xslSource);
         
         transformer.transform(xmlSource,result);
         if(responseElement != null && responseElement.trim().length() > 0) {
             Element currentRoot = resultDoc.getDocumentElement();
             Element root = resultDoc.createElement(responseElement);
             root.appendChild(currentRoot);
             resultDoc.appendChild(root);
         }
      }catch(ParserConfigurationException pce) {
        logger.error("transformExistResult(Node, String, String)", pce);
      }catch(TransformerConfigurationException tce) {
        logger.error("transformExistResult(Node, String, String)", tce);
      }catch(TransformerException te) {
        logger.error("transformExistResult(Node, String, String)", te);
      }
      return resultDoc;
   }
   
  
   public Document transformUpdate(Node doc,String versionNumber) {
       
       Source xmlSource = new DOMSource(doc);
       Document resultDoc = null;
       String styleSheetName = "UpdateProcess_" + versionNumber + ".xsl";
    logger.info("transformUpdate(Node, String) - the stylesheet name = "
            + styleSheetName);
       Source xslSource = new StreamSource(loadStyleSheet(styleSheetName));
       DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
       try {
          builderFactory.setNamespaceAware(true);
          DocumentBuilder builder = builderFactory.newDocumentBuilder();
          resultDoc = builder.newDocument();
          //DocumentFragment df = resultDoc.createDocumentFragment();
          TransformerFactory transformerFactory = TransformerFactory.newInstance();
          
          DOMResult result = new DOMResult(resultDoc);
          Transformer transformer = transformerFactory.newTransformer(xslSource);
          
          transformer.transform(xmlSource,result);
          
       }catch(ParserConfigurationException pce) {
        logger.error("transformUpdate(Node, String)", pce);
       }catch(TransformerConfigurationException tce) {
        logger.error("transformUpdate(Node, String)", tce);
       }catch(TransformerException te) {
        logger.error("transformUpdate(Node, String)", te);
       }
    logger
            .info("transformUpdate(Node, String) - THIS IS AFTER THE TRANSFORMUPDATE");
       DomHelper.DocumentToStream(resultDoc,System.out);
       return resultDoc;
    }   
   
   
   public Document transformDatabaseProcess(Node doc) {
      
      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;
      
      Source xslSource = new StreamSource(loadDBXSL());
      
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      
      try {
         builderFactory.setNamespaceAware(true);
         DocumentBuilder builder = builderFactory.newDocumentBuilder();
         resultDoc = builder.newDocument();
         //DocumentFragment df = resultDoc.createDocumentFragment();
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         
         DOMResult result = new DOMResult(resultDoc);
         Transformer transformer = transformerFactory.newTransformer(xslSource);
         
         transformer.transform(xmlSource,result);
         
      }catch(ParserConfigurationException pce) {
        logger.error("transformDatabaseProcess(Node)", pce);
      }catch(TransformerConfigurationException tce) {
        logger.error("transformDatabaseProcess(Node)", tce);
      }catch(TransformerException te) {
        logger.error("transformDatabaseProcess(Node)", te);
      }
      return resultDoc;
      
   }
   
   public Document transformCastorProcess(Node doc) {
      
      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;
      
      Source xslSource = new StreamSource(loadCastorXSL());
      
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      
      try {
         builderFactory.setNamespaceAware(true);
         DocumentBuilder builder = builderFactory.newDocumentBuilder();
         resultDoc = builder.newDocument();
         //DocumentFragment df = resultDoc.createDocumentFragment();
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         
         DOMResult result = new DOMResult(resultDoc);
         Transformer transformer = transformerFactory.newTransformer(xslSource);
         
         transformer.transform(xmlSource,result);
         
      }catch(ParserConfigurationException pce) {
        logger.error("transformCastorProcess(Node)", pce);
      }catch(TransformerConfigurationException tce) {
        logger.error("transformCastorProcess(Node)", tce);
      }catch(TransformerException te) {
        logger.error("transformCastorProcess(Node)", te);
      }
      return resultDoc;
   }
   
   
   
}