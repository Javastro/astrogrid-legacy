package org.astrogrid.registry.common;

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

public class XSLHelper {
   
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
      
   private InputStream loadDBXSL() {
      ClassLoader loader = this.getClass().getClassLoader();
      return loader.getResourceAsStream(DEFAULT_DATABASE_XSL);
   }

   private InputStream loadCastorXSL() {
      ClassLoader loader = this.getClass().getClassLoader();
      return loader.getResourceAsStream(DEFAULT_CASTOR_XML);
   }
   
   
   
   public Document transformDatabaseProcess(Document doc) {
      
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
         pce.printStackTrace();
      }catch(TransformerConfigurationException tce) {
         tce.printStackTrace();
      }catch(TransformerException te) {
         te.printStackTrace();
      }
      return resultDoc;
      
   }
   
   public Document transformCastorProcess(Document doc) {
      
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
         pce.printStackTrace();
      }catch(TransformerConfigurationException tce) {
         tce.printStackTrace();
      }catch(TransformerException te) {
         te.printStackTrace();
      }
      return resultDoc;
   }
   
   
   
}