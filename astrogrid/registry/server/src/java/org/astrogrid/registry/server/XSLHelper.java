package org.astrogrid.registry.server;

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
    * Empty constructor -- should delete later, this is automatic.
    *
    */
   public XSLHelper() {
      
   }
   
   /**
    * Load a Stylesheet from the CLASSPath jars given a particular name.
    * @param name A string name of the xsl stylesheet file name. Normally in the classpath or in a jar file.
    * @return A InputStream to a xsl stylesheet.
    */
   private InputStream loadStyleSheet(String name) {
       logger.info("Loading from Classloader = " + name);
       ClassLoader loader = this.getClass().getClassLoader();
       return loader.getResourceAsStream(name);
   }
      

   /**
    * Method: transformADQLToXQL
    * Description: Transforms a particular adql version into XQL (XQuery) for use with the eXist db. Other
    * parameters are passed in to help build the XQuery such as the root node to be queried on.  And the namespaces
    * that need to be declared for queries. Supports multiple versions of ADQL if a stylesheet is provided.
    * @param doc ADQL Select/Where XML Node to be processed through a XSL stylesheet
    * @param versionNumber Version number of ADQL used as part of the adql stylesheet name.
    * @param rootNode Actually a String of the root Resource (with prefix) to be queries on.
    * @param namespaceDeclare A long string of all the 'declare namespace' for the XQL.
    * @return A XQL (XQuery) String to be used for querying on the XML database.
    */
   public String transformADQLToXQL(Node doc, String versionNumber,String rootNode, String namespaceDeclare) {
      
      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;      
      
      logger
            .info("transformADQLToXQL(Node, String) - the file resource = ADQLToXQL-"
                    + versionNumber + ".xsl");
      Source xslSource = new StreamSource(loadStyleSheet(XSL_DIRECTORY + "ADQLToXQL-" + versionNumber + ".xsl"));
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
         transformer.setParameter("resource_elem", rootNode);
         transformer.setParameter("declare_elems", namespaceDeclare);         
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
      
   /**
    * Method: transformToOAI
    * Description: Transforms XML from a given query in the XML database to a OAI GetRecord XML. Used mainly by 
    * thrid party tool which shows various OAI xml verbs in the correct XML format. Handles multiple versions of
    * XML from the Registry depending on the version number from the main vr namespace.
    * @param doc XML Root Node of a document from a  query of the given database.
    * @param versionNumber version number from the vr namespace, which is the main Resource namespace defined.
    * @return XML Document object of a OAI GetRecord XML.
    */
   public Document transformToOAI(Node doc, String versionNumber) {
      String fileName = "Resourcev" + versionNumber + "-OAI.xsl";

      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;
      
      ClassLoader loader = this.getClass().getClassLoader();
      InputStream is = null;
      is = loader.getResourceAsStream(XSL_DIRECTORY + fileName);
      Source xslSource = new StreamSource(loadStyleSheet(XSL_DIRECTORY + fileName));
            
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
   
   /**
    * Method: transformExistResult
    * Description: When querying eXist XML database (at the moment on Rest style) it will put a known <exist:Result> root
    * element around the results of the query (so the xml will be valid xml).  This transforms that XML to simply get rid of
    * that root element and put a different root element around.  Also for convenience a responseElement parameter is used
    * for Web Service methods to wrap the Results with another Element for use in the Soap:body on the response.
    * @param doc XML Root node results from a query of the XML database.
    * @param versionNumber version number from main vr namespace.
    * @param responseElement An optional string to wrap around another element for web service methods.
    * @return XML document to be returned.
    */
   public Document transformExistResult(Node doc, String versionNumber, String responseElement) {
      
      Source xmlSource = new DOMSource(doc);
      Document resultDoc = null;
      
      ClassLoader loader = this.getClass().getClassLoader();
      InputStream is = null;

      is = loader.getResourceAsStream(XSL_DIRECTORY + "ExistRegistryResult" + versionNumber + ".xsl");
      
      Source xslSource = new StreamSource(loadStyleSheet(XSL_DIRECTORY + "ExistRegistryResult" + versionNumber + ".xsl"));
      
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
   
   /**
    * Method: transformUpdate
    * Description: Allows the registry to transform the XML before going into the database. It is multiple versioned
    * based on the vr namespace and can be applied to regular updates (from Astrogrid) or on harvests from other
    * registries. 
    * This usefullness is best described in some examples:
    * ex 1: 0.9 - used substitution groups hence same xml could be expressed in 2 or 3 ways meaning xql queries very
    * difficult, so a xsl stylesshet was applied to make the xml the same.
    * ex 2: 0.10 - no more subtituion groups, but the way the main Resource element was described any number of XML name 
    * with any number of namespaces could be of type Resouce.  So XSL is used to again put it in the way that is 
    * consistent in the db.
    * @param doc XML to be transformed.
    * @param versionNumber version number of main vr namespace.
    * @param harvestUpdate is this used on a harvest, if so use a different xsl stylesheet name.
    * @return XML to be updated into the database.
    */
   public Document transformUpdate(Node doc,String versionNumber,boolean harvestUpdate) {
       
       Source xmlSource = new DOMSource(doc);
       Document resultDoc = null;
       String harvestName = "";
       if(harvestUpdate)
           harvestName = "Harvest_";
       String styleSheetName = "UpdateProcess_" + harvestName + versionNumber + ".xsl";
       logger.info("transformUpdate(Node, String) - the stylesheet name = "
            + styleSheetName);
       Source xslSource = new StreamSource(loadStyleSheet(XSL_DIRECTORY + styleSheetName));
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
   
}