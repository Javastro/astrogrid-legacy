package org.astrogrid.registry.messaging.processor;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.queue.OpenJmsConstants;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public abstract class QueueMessageProcessorBase implements ElementProcessor {
  // Logger.
  protected Category logger = Category.getInstance(getClass());
  
  // OpenJMS connection properties.
  protected Properties openJmsProperties;
  
  // Sending properties.
  protected boolean isSenderTransactional;
  protected int senderAck;
  protected int senderDelivery;

  // Receiving properties.
  protected boolean isReceiverTransactional;
  protected int receiverAck;

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#init(javax.xml.rpc.server.ServletEndpointContext)
   */
  public void init(ServletEndpointContext servletEndpointContext) throws ProcessorException {
    ServletContext servletContext = servletEndpointContext.getServletContext();
    
    // Read OpenJMS properties.
    openJmsProperties = new Properties();
    String realPath = servletContext.getRealPath(
      OpenJmsConstants.OPENJMS_PROPERTIES_FILENAME);

    logger.debug("[init] OpenJMS properties location: " + realPath);
    
    try {
      openJmsProperties.load(new FileInputStream(realPath));
    }
    catch(IOException e) {
      logger.debug("[init] no OpenJMS properties ... using defaults");
    }
    
    isSenderTransactional = Boolean.valueOf(
      openJmsProperties.getProperty(
        OpenJmsConstants.SENDER_TRANSACTIONAL_PROPERTY,
        OpenJmsConstants.SENDER_TRANSACTIONAL_DEFAULT)).booleanValue();
    logger.debug("[init] sender transactional: " + isSenderTransactional);
    
    try {
      senderAck = Integer.parseInt(
        openJmsProperties.getProperty(
          OpenJmsConstants.SENDER_ACKNOWLEDGEMENT_PROPERTY,
          OpenJmsConstants.SENDER_ACKNOWLEDGEMENT_DEFAULT));
    }
    catch(NumberFormatException e) {
      logger.error("[init] invalid sender acknowledgment");
    }
    logger.debug("[init] sender acknowledgement: " + senderAck);
    
    try {
      senderDelivery = Integer.parseInt(
        openJmsProperties.getProperty(
          OpenJmsConstants.SENDER_DELIVERY_MODE_PROPERTY,
          OpenJmsConstants.SENDER_DELIVERY_MODE_DEFAULT));
    }
    catch(NumberFormatException e) {
      logger.error("[init] invalid sender delivery mode");
    }
    logger.debug("[init] sender delivery mode: " + senderDelivery);
    
    isReceiverTransactional = Boolean.valueOf(
      openJmsProperties.getProperty(
        OpenJmsConstants.RECEIVER_TRANSACTIONAL_PROPERTY,
        OpenJmsConstants.RECEIVER_TRANSACTIONAL_DEFAULT)).booleanValue();
    logger.debug("[init] receiver transactional: " + isReceiverTransactional);
    
    try {
      receiverAck = Integer.parseInt(
        openJmsProperties.getProperty(
          OpenJmsConstants.RECEIVER_ACKNOWLEDGEMENT_PROPERTY,
          OpenJmsConstants.RECEIVER_ACKNOWLEDGEMENT_DEFAULT));
    }
    catch(NumberFormatException e) {
      logger.error("[init] invalid receiver acknowledgement");
    }
    logger.debug("[init] receiver acknowledgement: " + receiverAck);
  }

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#destroy()
   */
  public void destroy() throws ProcessorException {
    openJmsProperties = null;
  }
}
