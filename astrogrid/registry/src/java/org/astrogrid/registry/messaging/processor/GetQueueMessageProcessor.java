package org.astrogrid.registry.messaging.processor;

import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Properties;

import javax.jms.TextMessage;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.queue.OpenJmsUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class GetQueueMessageProcessor implements ElementProcessor {
  // Constants.
  private static final String QUEUE_NAME = "queue-name";
  private static final String OPENJMS_PROPERTIES_FILENAME = "openjms.properties";
  
  // Logger.
  private Category logger = Category.getInstance(GetQueueMessageProcessor.class);
  
  // OpenJMS connection properties.
  // *** ACCESS ONLY THROUGH getOpenJmsProperties() ***
  private Properties openJmsProperties;
  
  // Document builder.
  // *** ACCESS VIA getNSAwareDocumentBuilder() ***
  private DocumentBuilder builder;

  /**
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws Exception {
    Element result = null;
        
    if(logger.isDebugEnabled()) {
      logger.debug("[process] element: " + XMLUtils.ElementToString(element));
    }
    
    String queueName = element.getAttribute(QUEUE_NAME);
    
    logger.debug("[process] queue name: " + queueName);

    OpenJmsUtil openJms = new OpenJmsUtil(
      getOpenJmsProperties(servletEndpointContext.getServletContext()));
    openJms.connect(queueName);
    
    logger.debug("[process] connected");

    TextMessage textMessage = openJms.getTextMessage();
    
    openJms.disconnect();

    if(textMessage != null) {
      logger.debug("[process] message text: " + textMessage.getText());
    
      Document doc = getXmlDocument(textMessage.getText());
    
      if(logger.isDebugEnabled()) {
        logger.debug("[process] document: " + XMLUtils.DocumentToString(doc));
      }
      
      result = doc.getDocumentElement();
    }
    
    return result;
  }
  
  private synchronized Properties getOpenJmsProperties(ServletContext servletContext) throws ProcessorException {
    if(openJmsProperties == null) {
      Properties props = new Properties();
      
      try {
        props.load(new FileInputStream(servletContext.getRealPath(OPENJMS_PROPERTIES_FILENAME)));
      }
      catch(Exception e) {
        throw new ProcessorException("could not load OpenJMS properties: " + e.getMessage(), e);
      }
      
      openJmsProperties = props;
    }
    
    logger.debug("[getOpenJmsProperties] properties: " + openJmsProperties);
    
    return openJmsProperties;
  }

  private Document getXmlDocument(String content) throws Exception {
    logger.debug("[getXmlDocument] content: " + content);
    
    Document result = null;
    StringReader reader = new StringReader(content);

    try {
      InputSource is = new InputSource(reader);
      result = getNSAwareDocumentBuilder().parse(is);
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
  
  private synchronized DocumentBuilder getNSAwareDocumentBuilder() throws ParserConfigurationException {
    if(builder == null) {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      builder = builderFactory.newDocumentBuilder();
    }
    
    return builder;
  }
}
