package org.astrogrid.registry.messaging.processor;

import java.io.File;
import java.io.StringReader;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.log.XmlBlasterUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlBlaster.util.MsgUnit;
import org.xmlBlaster.util.XmlBlasterException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class GetLogMessagesProcessor implements ElementProcessor {
  // Constants.
  private static String LOG_MESSAGES_DOC_FILE = "astrolog-messages-base.xml";
  private static String XPATH_QUERY = "xpath-query";
  
  // Logger.
  private Category logger = Category.getInstance(GetLogMessagesProcessor.class);
  
  // Document base.
  // *** ACCESS VIA getDocumentBase() ***
  private Document documentBase;
  
  // Document builder.
  // *** ACCESS VIA getNSAwareDocumentBuilder() ***
  private DocumentBuilder builder;
  
  /**
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws Exception {
    Element result = null;
    
    try {
      String xpathQuery = element.getAttribute(XPATH_QUERY);
      logger.debug("[process] xpath query: " + xpathQuery);

      XmlBlasterUtil xmlBlaster = new XmlBlasterUtil();
      xmlBlaster.connect(null, null);
      logger.debug("[process] connected");
      

      MsgUnit[] msgUnits = xmlBlaster.getMessages(xpathQuery);
      logger.debug("[process] message units: " + msgUnits);
      
      if(msgUnits != null && msgUnits.length > 0) {
        Document doc = buildDocumentFromMsgUnits(servletEndpointContext.getServletContext(), msgUnits);
        result = doc.getDocumentElement();
        
        if(logger.isDebugEnabled()) {
          logger.debug("[process] result: " + XMLUtils.ElementToString(result));     
        }
      }
      
      xmlBlaster.disconnect(null);
    }
    catch(XmlBlasterException e) {
      throw new ProcessorException("failed to log message: " + e.getMessage(), e);
    }
      
    return result;
  }
  
  private Document buildDocumentFromMsgUnits(ServletContext servletContext, MsgUnit[] msgUnits) throws Exception {
    Document result = getDocumentBase(servletContext);
    Element resultElement = result.getDocumentElement();

    Node msgNode = null;    
    Element msgElement = null;
    String msgContent = null;
    MsgUnit msgUnit = null;
    for(int msgUnitIndex = 0; msgUnitIndex < msgUnits.length; msgUnitIndex++) {
      msgUnit = msgUnits[msgUnitIndex];
      
      msgContent = msgUnit.getContentStr();

      try {
        msgElement = getXmlElement(msgContent);
        msgNode = result.importNode(msgElement, true);
        resultElement.appendChild(msgNode);
      }
      catch(Exception e) {
        logger.error("[buildDocumentFromMsgUnits] error building xml: " + e.getMessage(), e);
        throw e;
      }
    }
    
    if(logger.isDebugEnabled()) {
      logger.debug("[buildDocumentFromMsgUnits] result: " + XMLUtils.DocumentToString(result));
    }
    
    return result;
  }

  private Element getXmlElement(String content) throws Exception {
    logger.debug("[getXmlElement] content: " + content);
    
    Element result = null;
    StringReader reader = new StringReader(content);

    try {
      InputSource is = new InputSource(reader);
      Document doc = getNSAwareDocumentBuilder().parse(is);
      if(doc != null) {
        result = doc.getDocumentElement();
      }
    }
    catch(SAXException e) {
      logger.error("couldn't parse xml message: " + e.getMessage(), e);
      throw e;
    }
    finally {    
      reader.close();
    }
    
    return result;
  }

  private synchronized Document getDocumentBase(ServletContext servletContext) throws Exception {
    if(documentBase == null) {
      documentBase = getNSAwareDocumentBuilder().parse(
        new File(servletContext.getRealPath(LOG_MESSAGES_DOC_FILE)));
    }
    
    return documentBase;
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
