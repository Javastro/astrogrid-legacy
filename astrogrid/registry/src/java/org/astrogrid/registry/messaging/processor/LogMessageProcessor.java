package org.astrogrid.registry.messaging.processor;

import java.io.File;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.log.XmlBlasterUtil;
import org.astrogrid.registry.util.XsltTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlBlaster.client.qos.PublishReturnQos;
import org.xmlBlaster.util.XmlBlasterException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class LogMessageProcessor implements ElementProcessor {
  // Constants.
  private static String XSL_STYLESHEET = "astrolog-xmlblaster.xsl";
  
  // Logger.
  private Category logger = Category.getInstance(LogMessageProcessor.class);
  
  // Document builder.
  // *** ACCESS VIA getNSAwareDocumentBuilder() ***
  private DocumentBuilder builder;
  
  // Stylesheet.
  // *** ACCESS VIA getXslStyleSheet() ***
  private Document xslStyleSheet;
  
  /**
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws Exception {
    String key = null;
    
    try {
      ServletContext servletContext = servletEndpointContext.getServletContext();
      
      Document xmlDoc = getXmlDocument(element);
      Document stylesheetDoc = getXslStyleSheet(servletContext);
      
      if(logger.isDebugEnabled()) {
        logger.debug("[process] xml: " + XMLUtils.ElementToString(element));
        logger.debug("[process] xsl: " + XMLUtils.DocumentToString(stylesheetDoc));
      }

      key = XsltTransformer.transform(xmlDoc, stylesheetDoc);
      logger.debug("[process] key: " + key);
      
      try {
        XmlBlasterUtil xmlBlaster = new XmlBlasterUtil();
        xmlBlaster.connect(null, null);
       
        PublishReturnQos pubRetQos = xmlBlaster.publishString(XMLUtils.ElementToString(element), key);
        
        xmlBlaster.disconnect(null);
      }
      catch(XmlBlasterException e) {
        throw new ProcessorException("failed to log message: " + e.getMessage(), e);
      }
      
      // TODO return success or failure
    }
    catch(Exception e) {
      logger.error("[process] exception: " + e.getMessage(), e);
      throw e;
    }
    
    return null;
  }
  
  private Document getXmlDocument(Element element) throws Exception {
    Document result = getNSAwareDocumentBuilder().newDocument();
    Node node = result.importNode(element, true);
    result.appendChild(node);
    
    return result;
  }
  
  private synchronized Document getXslStyleSheet(ServletContext servletContext) throws Exception {
    if(xslStyleSheet == null) {
      xslStyleSheet = getNSAwareDocumentBuilder().parse(
        new File(servletContext.getRealPath(XSL_STYLESHEET)));
    }
    
    return xslStyleSheet;
  }

  private synchronized DocumentBuilder getNSAwareDocumentBuilder() throws ParserConfigurationException {
    if(builder == null) {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      builder = builderFactory.newDocumentBuilder();
    }
    
    return builder;
  }
}
