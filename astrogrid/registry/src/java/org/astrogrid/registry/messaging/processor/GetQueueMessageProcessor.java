package org.astrogrid.registry.messaging.processor;

import java.io.File;
import java.io.StringReader;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.messaging.queue.OpenJmsConstants;
import org.astrogrid.registry.messaging.queue.OpenJmsFactory;
import org.astrogrid.registry.messaging.queue.OpenJmsReceiverInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class GetQueueMessageProcessor extends QueueMessageProcessorBase implements ElementProcessor {
  // Constants.
  private static String QUEUE_MESSAGES_DOC_FILE = "astrogrid-queue-messages.xml";

  // Base document for returning queue messages.
  private Document queueMessageBase;
  
  // Namespace-aware document builder.
  private DocumentBuilder nsBuilder;

  // Document builder.
  private DocumentBuilder builder;

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#init(javax.xml.rpc.server.ServletEndpointContext)
   */
  public void init(ServletEndpointContext servletEndpointContext) throws ProcessorException {
    super.init(servletEndpointContext);
    
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

    // Get base queue messages document.
    try {
      queueMessageBase = nsBuilder.parse(new File(servletContext.getRealPath(QUEUE_MESSAGES_DOC_FILE)));
    }
    catch(Exception e) {
      logger.error("[init] could not parse queue messages base", e);
      throw new ProcessorException("could not parse queue messages base", e);
    }
  }

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#destroy()
   */
  public void destroy() throws ProcessorException {
    nsBuilder = null;
    
    super.destroy();
  }

  /**
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws ProcessorException {
    Element result = null;
        
    if(logger.isDebugEnabled()) {
      logger.debug("[process] element: " + XMLUtils.ElementToString(element));
    }
    
    String queueName = element.getAttribute(OpenJmsConstants.QUEUE_NAME);
    
    logger.debug("[process] queue name: " + queueName);

    OpenJmsReceiverInterface messageReceiver = null;
    try {
      messageReceiver = OpenJmsFactory.getOpenJmsReceiver(openJmsProperties);
      messageReceiver.connect(queueName, isSenderTransactional, senderAck);
      messageReceiver.start();
    
      logger.debug("[process] connected and ready to receive");

      TextMessage textMessage = messageReceiver.getTextMessage();
    
      logger.debug("[process] got text message");

      if(textMessage != null) {
        logger.debug("[process] message text: " + textMessage.getText());
    
        Document doc = getXmlDocument(textMessage.getText());
    
        if(logger.isDebugEnabled()) {
          logger.debug("[process] document: " + XMLUtils.DocumentToString(doc));
        }
      
        result = doc.getDocumentElement();
        
        // Acknowledge message receipt if necessary.
        if(isReceiverTransactional) {
          messageReceiver.commit();
        }
        else if(receiverAck == Session.CLIENT_ACKNOWLEDGE ||
                receiverAck == Session.DUPS_OK_ACKNOWLEDGE)
          textMessage.acknowledge();
      }
    }
    catch(Exception e) {
      logger.error("[process] error getting queue message", e);
      throw new ProcessorException("error getting queue message", e);
    }
    finally {
      try {
        if(messageReceiver != null ) {
          messageReceiver.stop();
          messageReceiver.disconnect();
          logger.debug("[process] stopped and disconnected");
        } 
      }
      catch(Exception e) {
        logger.error("[process] error closing queue: " + e.getMessage(), e);
        throw new ProcessorException("[process] error closing queue: " + e.getMessage(), e);
      }
    }
    
    return result;
  }

  private Document getXmlDocument(String content) throws Exception {
    logger.debug("[getXmlDocument] content: " + content);
    
    Document result = (Document) queueMessageBase.cloneNode(true);
    Element resultElement = result.getDocumentElement();

    try {
      Element msgElement = getXmlElement(content);
      Node msgNode = result.importNode(msgElement, true);
      resultElement.appendChild(msgNode);
    }
    catch(SAXException e) {
      logger.error("couldn't parse xml message: " + e.getMessage(), e);
      throw e;
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
