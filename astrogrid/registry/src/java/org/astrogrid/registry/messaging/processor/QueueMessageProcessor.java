package org.astrogrid.registry.messaging.processor;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.queue.OpenJmsUtil;
import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueueMessageProcessor implements ElementProcessor {
  // Constants.
  private static final String QUEUE_NAME = "queue-name";
  private static final String OPENJMS_PROPERTIES_FILENAME = "openjms.properties";
  
  // Logger.
  private Category logger = Category.getInstance(QueueMessageProcessor.class);
  
  // OpenJMS connection properties.
  private Properties openJmsProperties;
  
  /**
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws Exception {
    if(logger.isDebugEnabled()) {
      logger.debug("[process] element: " + XMLUtils.ElementToString(element));
    }
    
    String queueName = element.getAttribute(QUEUE_NAME);
    
    logger.debug("[process] queue name: " + queueName);

    OpenJmsUtil openJms = new OpenJmsUtil(
      getOpenJmsProperties(servletEndpointContext.getServletContext()));
    openJms.connect(queueName);
    
    openJms.sendTextMessage(XMLUtils.ElementToString(element));
    
    openJms.disconnect();
    
    return null;
  }
  
  private Properties getOpenJmsProperties(ServletContext servletContext) throws ProcessorException {
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

}
