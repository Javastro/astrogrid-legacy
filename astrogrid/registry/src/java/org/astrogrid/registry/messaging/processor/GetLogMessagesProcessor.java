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
import org.xmlBlaster.util.MsgUnit;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class GetLogMessagesProcessor implements ElementProcessor {
  // Constants.
  private static String LOG_MESSAGES_DOC_FILE = "astrogrid-log-messages.xml";
  private static String XPATH_QUERY = "query";
  
  // Logger.
  private Category logger = Category.getInstance(getClass());
  
  // Base document for returning log messages.
  private Document logMessageBase;
  
  // Namespace-aware document builder.
  private DocumentBuilder nsBuilder;
  
  // Document builder.
  private DocumentBuilder builder;
  
  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#init(javax.xml.rpc.server.ServletEndpointContext)
   */
  public void init(ServletEndpointContext servletEndpointContext) throws ProcessorException {
    ServletContext servletContext = servletEndpointContext.getServletContext();
    
    // Create XML parsers.
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builder = builderFactory.newDocumentBuilder();

      builderFactory.setNamespaceAware(true);
      nsBuilder = builderFactory.newDocumentBuilder();
    }
    catch(ParserConfigurationException e) {
      logger.error("[init] " + e.getMessage(), e);
      throw new ProcessorException(e.getMessage(), e);
    }

    // Get base log messages document.
    try {
      logMessageBase = nsBuilder.parse(new File(servletContext.getRealPath(LOG_MESSAGES_DOC_FILE)));
    }
    catch(Exception e) {
      logger.error("[init] could not parse log messages base", e);
      throw new ProcessorException("could not parse log messages base", e);
    }
  }
  
  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#destroy()
   */
  public void destroy() throws ProcessorException {
    nsBuilder = null;
    logMessageBase = null;
  }

  /**
   * Run a given XPath query over the message log and return the matching messages.
   * 
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws ProcessorException {
    Element result = null;
    
    if(logger.isDebugEnabled()) {
      logger.debug("[process] element: " + XMLUtils.ElementToString(element));
    }
    
    try {
      String xpathQuery = element.getAttribute(XPATH_QUERY);
      logger.debug("[process] xpath query: " + xpathQuery);

      XmlBlasterUtil xmlBlaster = new XmlBlasterUtil();
      xmlBlaster.connect(null, null);
      logger.debug("[process] connected");
      

      MsgUnit[] msgUnits = xmlBlaster.getMessages(xpathQuery);
      logger.debug("[process] message units: " + msgUnits);
      
      if(msgUnits != null && msgUnits.length > 0) {
        Document doc = buildDocumentFromMsgUnits(msgUnits);
        result = doc.getDocumentElement();
        
        if(logger.isDebugEnabled()) {
          logger.debug("[process] result: " + XMLUtils.ElementToString(result));     
        }
      }
      
      xmlBlaster.disconnect(null);
    }
    catch(Exception e) {
      logger.error("[process] failed to log message: " + e.getMessage(), e);
      throw new ProcessorException("failed to log message: " + e.getMessage(), e);
    }
      
    return result;
  }
  
  private Document buildDocumentFromMsgUnits(MsgUnit[] msgUnits) throws Exception {
    Document result = (Document) logMessageBase.cloneNode(true);
    Element resultElement = result.getDocumentElement();

    String msgContent = null;
    Element msgElement = null;
    Node msgNode = null;    
    for(int msgUnitIndex = 0; msgUnitIndex < msgUnits.length; msgUnitIndex++) {
      try {
        msgContent = msgUnits[msgUnitIndex].getContentStr();
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
      Document doc = builder.parse(is);
      if(doc != null) {
        result = doc.getDocumentElement();
      }
    }
    catch(Exception e) {
      // Catch allows finally to clean up.
      throw e;
    }
    finally {    
      reader.close();
    }
    
    return result;
  }
}
