package org.astrogrid.registry.messaging.processor;

import java.util.Properties;

import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.registry.messaging.queue.OpenJmsConstants;
import org.astrogrid.registry.messaging.queue.OpenJmsFactory;
import org.astrogrid.registry.messaging.queue.OpenJmsSenderInterface;
import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueueMessageProcessor extends QueueMessageProcessorBase implements ElementProcessor {
  public QueueMessageProcessor() {
    openJmsProperties = new Properties();
  }
  
  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#init(javax.xml.rpc.server.ServletEndpointContext)
   */
  public void init(ServletEndpointContext servletEndpointContext) throws ProcessorException {
    super.init(servletEndpointContext);
  }

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#destroy()
   */
  public void destroy() throws ProcessorException {
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
    
    OpenJmsSenderInterface messageSender = null;
    try {
      messageSender = OpenJmsFactory.getOpenJmsSender(openJmsProperties);
      messageSender.connect(queueName, isSenderTransactional, senderAck);
      messageSender.start();
    
      logger.debug("[process] connected and ready to send");

      String content = XMLUtils.ElementToString(element);
      logger.debug("[process] message content: " + content);
      messageSender.sendTextMessage(content, senderDelivery);
      
      logger.debug("[process] message sent");
    }
    catch(Exception e) {
      logger.error("[process] error processing queue message", e);
      throw new ProcessorException("error processing queue message", e);
    }
    finally {
      try {
        if(messageSender != null ) {
          messageSender.stop();
          messageSender.disconnect();
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
}
