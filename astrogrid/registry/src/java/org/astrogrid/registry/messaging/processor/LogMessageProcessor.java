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

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class LogMessageProcessor implements ElementProcessor {
  // Constants.
  private static String XSL_STYLESHEET = "astrogrid-xmlblaster.xsl";
  
  // Logger.
  private Category logger = Category.getInstance(getClass());
  
  // Document builder.
  private DocumentBuilder builder;
  
  // Stylesheet.
  private Document xslStyleSheet;
  
  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#init(javax.xml.rpc.server.ServletEndpointContext)
   */
  public void init(ServletEndpointContext servletEndpointContext) throws ProcessorException {
    ServletContext servletContext = servletEndpointContext.getServletContext();
    
    // Create XML namespace-aware parser.
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      builder = builderFactory.newDocumentBuilder();
    }
    catch(ParserConfigurationException e) {
      logger.error("[init] " + e.getMessage(), e);
      throw new ProcessorException(e.getMessage(), e);
    }

    // Load message key stylesheet.
    try {
      xslStyleSheet = builder.parse(new File(servletContext.getRealPath(XSL_STYLESHEET)));
    }
    catch(Exception e) {
      logger.error("[init] could not load stylesheet", e);
      throw new ProcessorException("could not load stylesheet", e);
    }
  }

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#destroy()
   */
  public void destroy() throws ProcessorException {
    xslStyleSheet = null;
    builder = null;
  }

  /**
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws ProcessorException {
    Element result = null;
    
    if(logger.isDebugEnabled()) {
      logger.debug("[process] element: " + XMLUtils.ElementToString(element));
    }
    
    try {
      ServletContext servletContext = servletEndpointContext.getServletContext();
      
      Document xmlDoc = getXmlDocument(element);
      
      if(logger.isDebugEnabled()) {
        logger.debug("[process] xml: " + XMLUtils.ElementToString(element));
        logger.debug("[process] xsl: " + XMLUtils.DocumentToString(xslStyleSheet));
      }

      String key = XsltTransformer.transform(xmlDoc, xslStyleSheet);
      logger.debug("[process] key: " + key);
      
      XmlBlasterUtil xmlBlaster = new XmlBlasterUtil();
      xmlBlaster.connect(null, null);
     
      PublishReturnQos pubRetQos = xmlBlaster.publishString(XMLUtils.ElementToString(element), key);
      
      xmlBlaster.disconnect(null);
    }
    catch(Exception e) {
      logger.error("[process] exception: " + e.getMessage(), e);
      throw new ProcessorException("error processing log message", e);
    }
    
    return result;
  }
  
  private Document getXmlDocument(Element element) throws Exception {
    Document result = builder.newDocument();
    Node node = result.importNode(element, true);
    result.appendChild(node);
    
    return result;
  }
}
