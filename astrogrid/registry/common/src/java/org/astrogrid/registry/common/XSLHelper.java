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
/** 
 * Class: XSLHelper
 * Description: A small XSL helper class that simply loads up xsl stylesheets and tranforms the XML. 
 * @todo fix exception handling
 * @todo factor commonality of methods into a private helper method*/
public class XSLHelper {
    
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(XSLHelper.class);
    
    private static final String XSL_DIRECTORY = "xsl/";
   
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
   
   
   /**
    * Empty constructor -- should delete later, this is automatic.
    *
    */
   public XSLHelper() {
      
   }
   
   /**
    * Method: loadStyleSheet
    * Description: Load a Stylesheet from the CLASSPath jars given a particular name.
    * @param name A string name of the xsl stylesheet file name. Normally in the classpath or in a jar file.
    * @return A InputStream to a xsl stylesheet.
    */
   private InputStream loadStyleSheet(String name) {
       ClassLoader loader = this.getClass().getClassLoader();
       return loader.getResourceAsStream(name);
   }
      
   /**
    * Method: loadDBXSL
    * Description: Load a particular known xsl stylesheet for processing xml from the db. Used mainly to go from Castor
    * to better more undertandable XML.
    * @deprecated - no longer in use, because Castor is no longer in use on the registry side. A client must do
    * Castor itself.
    * @return A InputStream to a xsl stylesheet. 
    */
   private InputStream loadDBXSL() {
      ClassLoader loader = this.getClass().getClassLoader();
      return loader.getResourceAsStream(DEFAULT_DATABASE_XSL);
   }

   /**
    * Method loadCastorXSL
    * Description: Load a particular known xsl stylesheet for processing xml for Castor.
    * @deprecated - no longer in use, because Castor is no longer in use on the registry side. A client must do
    * Castor itself.
    * @return A InputStream to a xsl stylesheet. 
    */   
   private InputStream loadCastorXSL() {
      ClassLoader loader = this.getClass().getClassLoader();
      return loader.getResourceAsStream(DEFAULT_CASTOR_XML);
   }
   

   
   /**
    * Method: transformResourceToResource
    * Description: A nice helper method normally used for clients, to allow of tranforming one XML conforming to a 
    * Registry schema back to another XML conforming to a different Registry schema version. ex: 0.10 to 0.9 is the
    * most commonly used for the portal.  Handy use if a client cannot be easily upgradable to a new version of the
    * registry. The source and target versions are passed in for forming the xsl stylesheet name.  Meaning multiple
    * version tranformations can be supported (version numbers come from the main vr namespace).
    * @param doc XML Document root node of the sourceVersion.
    * @param sourceVersion version number of the source of the XML. ex: 0.10
    * @param targetVersion version number of the result/target for the XML ex: 0.9
    * @return XML of the converted schema.
    */
   public Document transformResourceToResource(Node doc, String sourceVersion, String targetVersion) {
       //sourceVersion = sourceVersion.replace('.','_');
       //targetVersion = targetVersion.replace('.','_');
       
       String fileName = "VOResource-v" + sourceVersion + "-v" + targetVersion + ".xsl";

       
       Source xmlSource = new DOMSource(doc);
       Document resultDoc = null;
       
       ClassLoader loader = this.getClass().getClassLoader();
       InputStream is = null;
       System.out.println("the filename being loaded = " + XSL_DIRECTORY + fileName);
       is = loader.getResourceAsStream(XSL_DIRECTORY + fileName);
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
         logger.error("transformResourceToResource(Node, String)", pce);
         pce.printStackTrace();
       }catch(TransformerConfigurationException tce) {
         logger.error("transformResourceToResource(Node, String)", tce);
         tce.printStackTrace();
       }catch(TransformerException te) {
         logger.error("transformResourceToResource(Node, String)", te);
         te.printStackTrace();
       }
       //@todo never return null on error.
       if(resultDoc == null) {
           logger.error("IN tranformResouceToResource resultDoc was null");
           //System.out.println("IN tranformResouceToResource resultDoc was null");
       }else {
           logger.info("THE RESULTDOC IN transformResourceToResource = "  + DomHelper.DocumentToString(resultDoc));
           //System.out.println("THE RESULTDOC IN transformResourceToResource = "  + DomHelper.DocumentToString(resultDoc));
       }
       return resultDoc;
    }
   
   /**
    * Used for Castor, deprecating and hope to delete soon. Clients should use Castor themselves now, not let the
    * registry do it.
    * @deprecated No longer in use
    * @param doc
    * @return
    */
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

   /**
    * Used for Castor, deprecating and hope to delete soon. Clients should use Castor themselves now, not let the
    * registry do it.
    * @deprecated No longer in use
    * @param doc
    * @return
    */
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